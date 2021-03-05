package usecase.league;

import com.google.inject.Inject;
import domain.entity.Draft;
import domain.entity.League;
import domain.exception.LeagueNotExists;
import domain.repository.DraftRepository;
import domain.repository.LeagueRepository;
import domain.repository.NameRepository;
import domain.repository.TeamRepository;
import usecase.dto.LeagueDTO;

public class GetLeague {

    private DraftRepository draftRepository; // draft status should maybe be a separate system
    private TeamRepository teamRepository;
    private LeagueRepository leagueRepository;
    private NameRepository nameRepository;

    @Inject
    public GetLeague(DraftRepository draftRepository, TeamRepository teamRepository, LeagueRepository leagueRepository, NameRepository nameRepository) {
        this.draftRepository = draftRepository;
        this.teamRepository = teamRepository;
        this.leagueRepository = leagueRepository;
        this.nameRepository = nameRepository;
    }

    public LeagueDTO getLeagueInfo(String leagueId) throws LeagueNotExists {
        League league = leagueRepository.getLeagueById(leagueId);
        if (league == null) throw new LeagueNotExists();

        Draft draft = draftRepository.getDraftById(leagueId);

        return LeagueDTO.builder()
                .id(leagueId)
                .name(nameRepository.getLeagueName(leagueId))
                .status(league.status)
                .owningTeamId(league.owningTeam)
                .maxTeams(league.maxTeams)
                .numTeams(teamRepository.countTeamsInLeague(leagueId))
                .draftIPAddress(draft != null ? draft.ipAddress : null)
                .draftTime(league.draftSettings.startTime)
                .turnDuration(league.draftSettings.turnDurationMillis)
                .build();
    }

}
