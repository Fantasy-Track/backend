package fantasyapp;

import com.google.inject.Guice;
import com.google.inject.Injector;
import usecase.meetLocking.LockMeets;

public class MeetLockingTest {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new JobsModule());
        LockMeets lockMeets = injector.getInstance(LockMeets.class);
        lockMeets.lockMeetsToday();
    }
}
