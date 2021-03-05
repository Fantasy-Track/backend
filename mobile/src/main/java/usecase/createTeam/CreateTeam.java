package usecase.createTeam;

import com.google.inject.Inject;
import domain.entity.Team;
import domain.exception.ApplicationException;
import domain.repository.TeamRepository;
import util.DistributedLock;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class CreateTeam {

    private TeamNameValidator nameValidator;
    private JoinLeagueValidator requestValidator;
    private TeamRepository teamRepository;
    private DistributedLock lock;

    @Inject
    public CreateTeam(TeamNameValidator nameValidator, JoinLeagueValidator requestValidator, TeamRepository teamRepository, DistributedLock lock) {
        this.nameValidator = nameValidator;
        this.requestValidator = requestValidator;
        this.teamRepository = teamRepository;
        this.lock = lock;
    }

    // This is the method that should be called for joining a league
    public String createTeam(CreateTeamRequest request) throws ApplicationException {
        AtomicReference<String> teamId = new AtomicReference<>();
        lock.lockAndRun(DistributedLock.TEAM, request.leagueId, () -> {
            nameValidator.validateName(request.teamName, request.leagueId);
            requestValidator.validateOwnerJoiningLeague(request.ownerId, request.leagueId);
            teamId.set(createAndSaveTeam(request));
        });
        return teamId.get();
    }

    // call this method to create the initial team in a league
    public String createInNonExistingLeague(CreateTeamRequest request) throws ApplicationException {
        AtomicReference<String> teamId = new AtomicReference<>();
        lock.lockAndRun(DistributedLock.TEAM, request.leagueId, () -> {
            nameValidator.validateName(request.teamName, request.leagueId);
            teamId.set(createAndSaveTeam(request));
        });
        return teamId.get();
    }

    private String createAndSaveTeam(CreateTeamRequest request) {
        Team team = buildTeam(request);
        teamRepository.addTeam(team);
        return team.id;
    }

    private Team buildTeam(CreateTeamRequest request) {
        return Team.builder()
                .leagueId(request.leagueId)
                .id(UUID.randomUUID().toString())
                .ownerId(request.ownerId)
                .name(request.teamName.trim())
                .fantasyPoints(0)
                .build();
    }

}
