package fantasyapp.masterDraftServer;

import fantasyapp.StaticDB;
import fantasyapp.repository.*;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.mongodb.client.MongoDatabase;
import domain.repository.*;
import org.redisson.api.RedissonClient;
import usecase.notification.NotificationFactory;
import util.DistributedLock;
import util.RedisLock;

public class RouterModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MongoDatabase.class).toInstance(StaticDB.database);
        bind(RedissonClient.class).toInstance(StaticDB.redis);
        bind(DistributedLock.class).to(RedisLock.class).in(Singleton.class);
        requestStaticInjection(NotificationFactory.class);

        bind(NameRepository.class).to(CachedNameRepository.class).in(Singleton.class);
        bind(ScheduledDraftRepository.class).to(DBScheduledDraftRepository.class).in(Singleton.class);
        bind(DraftRepository.class).to(DBDraftRepository.class).in(Singleton.class);
        bind(TeamRepository.class).to(DBTeamRepository.class).in(Singleton.class);
        bind(LeagueRepository.class).to(DBLeagueRepository.class).in(Singleton.class);
    }

}
