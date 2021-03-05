package usecase.league;

import com.google.inject.Inject;
import domain.entity.League;
import domain.entity.LeagueStatus;
import domain.exception.ApplicationException;
import domain.repository.LeagueRepository;
import usecase.createTeam.CreateTeam;
import usecase.createTeam.CreateTeamRequest;

import java.util.List;
import java.util.Set;

public class CreateLeague {

    private static int TEAM_SIZE = 9;

    private LeagueValidator validator;
    private RemoteIndexer remoteIndexer;
    private LeagueRepository leagueRepository;
    private CreateTeam createTeam;

    @Inject
    public CreateLeague(LeagueValidator validator, RemoteIndexer remoteIndexer, LeagueRepository leagueRepository, CreateTeam createTeam) {
        this.validator = validator;
        this.remoteIndexer = remoteIndexer;
        this.leagueRepository = leagueRepository;
        this.createTeam = createTeam;
    }

    //TODO consider using a distributed lock to prevent id clashing
    public CreateLeagueDTO createLeague(CreateLeagueRequest request) throws Exception {
        request.validateNoNulls();
        validator.validateLeagueSetup(request);

        List<String> athleteIds = remoteIndexer.indexAthletesInSchool(request.schoolId); //TODO throw more comprehensive error
        validator.validateAthletesInLeague(athleteIds, TEAM_SIZE);

        String leagueId = Base62IDGen.generateUniqueLeagueId(leagueRepository);
        String teamId = createOwningTeam(request, leagueId);

        League league = insertLeague(request, athleteIds, leagueId, teamId);
        remoteIndexer.indexMeets(request.schoolId, league.id, league.draftSettings.startTime); //TODO throw more comprehensive error

        return CreateLeagueDTO.builder()
                .leagueId(leagueId)
                .owningTeamId(teamId)
                .build();
    }

    private String createOwningTeam(CreateLeagueRequest request, String leagueId) throws ApplicationException {
        return createTeam.createInNonExistingLeague(CreateTeamRequest.builder()
                .leagueId(leagueId)
                .ownerId(request.ownerId)
                .teamName(request.teamName)
                .build());
    }

    private League insertLeague(CreateLeagueRequest request, List<String> athleteIds, String leagueId, String teamId) {
        League league = League.builder()
                .id(leagueId)
                .owningTeam(teamId)
                .schoolId(request.schoolId)
                .name(request.name.trim())
                .status(LeagueStatus.PRE_DRAFT)
                .athletes(athleteIds)
                .maxTeams(Math.min(athleteIds.size() / TEAM_SIZE, 8)) // sets default max teams to 8 or the most the league can handle
                .leagueSettings(League.LeagueSettings.builder()
                        .teamSize(TEAM_SIZE)
                        .build())
                .draftSettings(League.DraftSettings.builder()
                        .turnDurationMillis(70000)
                        .rounds(TEAM_SIZE)
                        .startTime(request.draftTime)
                        .build())
                .build();
        leagueRepository.addLeague(league);
        return league;
    }

}
