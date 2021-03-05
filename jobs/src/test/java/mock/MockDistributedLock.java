package mock;

import domain.exception.ApplicationException;
import util.DistributedLock;
import util.VoidFunc;

public class MockDistributedLock extends DistributedLock {
    @Override
    public void lockAndRun(String lockName, String leagueId, VoidFunc function) throws ApplicationException {
        function.call();
    }
}
