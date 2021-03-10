package usecase.team;

import com.google.inject.Inject;
import domain.entity.League;
import domain.entity.LeagueStatus;
import domain.entity.Team;
import domain.exception.*;
import domain.repository.LeagueRepository;
import domain.repository.TeamRepository;
import lombok.Builder;
import usecase.notification.NotificationFactory;
import util.DistributedLock;

public class DeleteTeam {

    private TeamRepository teamRepository;
    private LeagueRepository leagueRepository;
    private DistributedLock lock;

    @Inject
    public DeleteTeam(TeamRepository teamRepository, LeagueRepository leagueRepository, DistributedLock lock) {
        this.teamRepository = teamRepository;
        this.leagueRepository = leagueRepository;
        this.lock = lock;
    }

    public void deleteTeam(DeleteTeamRequest request) throws ApplicationException {
        lock.lockAndRun(DistributedLock.TEAM, request.leagueId, () -> {
            League league = leagueRepository.getLeagueById(request.leagueId);
            Team team = teamRepository.getTeamById(request.teamToDeleteId);

            if (league.status != LeagueStatus.PRE_DRAFT) {
                throw new CannotDoActionNow();
            } else if (team == null || !team.leagueId.equals(league.id)) {
                throw new TeamNotInLeague();
            } else if (league.owningTeam.equals(request.teamToDeleteId)) {
                throw new LeagueManagerCannotLeave();
            } else if (!request.actingTeamId.equals(request.teamToDeleteId) && !request.actingTeamId.equals(league.owningTeam)) {
                throw new UnauthorizedException();
            }
            teamRepository.deleteTeam(request.teamToDeleteId);
            NotificationFactory.handler.unregisterDeviceFromToken(request.fcmToken, request.teamToDeleteId);
            NotificationFactory.handler.unregisterDeviceFromToken(request.fcmToken, request.leagueId);
        });
    }

    @Builder
    public static class DeleteTeamRequest {
        public final String teamToDeleteId;
        public final String actingTeamId;
        public final String leagueId;
        public final String fcmToken;
    }

}
