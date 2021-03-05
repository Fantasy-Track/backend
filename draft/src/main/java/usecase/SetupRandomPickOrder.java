package usecase;

import fantasyapp.draftInstance.LeagueId;
import domain.exception.ApplicationException;
import domain.repository.PickOrderRepository;
import domain.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DistributedLock;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SetupRandomPickOrder {

    private final Logger logger = LoggerFactory.getLogger(SetupRandomPickOrder.class);

    private final PickOrderRepository pickOrderRepository;
    private final TeamRepository teamRepository;
    private final DistributedLock lock;

    private final String leagueId;

    @Inject
    public SetupRandomPickOrder(PickOrderRepository pickOrderRepository, TeamRepository teamRepository, DistributedLock lock, @LeagueId String leagueId) {
        this.pickOrderRepository = pickOrderRepository;
        this.teamRepository = teamRepository;
        this.lock = lock;
        this.leagueId = leagueId;
    }

    public void setup() throws ApplicationException {
        lock.lockAndRun(DistributedLock.TEAM, leagueId, () -> {
            List<String> teamIds = teamRepository.getTeamsInLeague(leagueId)
                    .stream().map(team -> team.id).collect(Collectors.toList());
            System.out.println(teamIds);
            Collections.shuffle(teamIds);
            pickOrderRepository.savePickOrder(teamIds);
            logger.info("Setup random pick order: " + teamIds);
        });
    }

}
