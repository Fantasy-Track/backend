package fantasyapp;

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

public class JobsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MongoDatabase.class).toInstance(StaticDB.database);
        bind(RedissonClient.class).toInstance(StaticDB.redis);
        requestStaticInjection(NotificationFactory.class);
        bind(DistributedLock.class).to(RedisLock.class).in(Singleton.class);
        bind(TeamRepository.class).to(DBTeamRepository.class).in(Singleton.class);
        bind(AthleteRepository.class).to(DBAthleteRepository.class).in(Singleton.class);
        bind(LeagueRepository.class).to(DBLeagueRepository.class).in(Singleton.class);
        bind(ContractRepository.class).to(DBContractRepository.class).in(Singleton.class);
        bind(MeetRepository.class).to(DBMeetRepository.class).in(Singleton.class);
        bind(ResultRepository.class).to(DBResultRepository.class).in(Singleton.class);
        bind(NameRepository.class).to(CachedNameRepository.class).in(Singleton.class);
        bind(DraftRepository.class).to(DBDraftRepository.class).in(Singleton.class);
        bind(TradeRepository.class).to(DBTradeRepository.class).in(Singleton.class);
        bind(AthleteUpdateRepository.class).to(DBAthleteUpdateRepository.class).in(Singleton.class);
    }

}
