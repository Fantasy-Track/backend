package util;

import com.google.inject.Inject;
import domain.exception.ApplicationException;
import org.redisson.api.RedissonClient;

import java.util.concurrent.locks.Lock;

public class RedisLock extends DistributedLock {

    private RedissonClient client;

    @Inject
    public RedisLock(RedissonClient client) {
        this.client = client;
    }

    @Override
    public void lockAndRun(String lockName, String leagueId, VoidFunc function) throws ApplicationException {
        Lock lock = client.getLock(lockName + "_" + leagueId);
        lock.lock();
        try {
            function.call();
        } finally {
            lock.unlock();
        }
    }
}
