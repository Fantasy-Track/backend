package mock;

import domain.repository.StatsRepository;

public class MockStatsRepo implements StatsRepository {

    @Override
    public double getTotalTeamPoints(String teamId) {
        if (teamId.equals("T1")) {
            return 98.0;
        }
        return 0;
    }

}
