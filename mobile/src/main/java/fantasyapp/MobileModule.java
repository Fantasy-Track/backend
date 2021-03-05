package fantasyapp;

import fantasyapp.StaticDB;
import fantasyapp.grpc.GrpcJobsClient;
import fantasyapp.repository.*;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.mongodb.client.MongoDatabase;
import domain.repository.*;
import org.redisson.api.RedissonClient;
import usecase.league.LeaguePointsUpdater;
import usecase.league.RemoteIndexer;
import usecase.notification.NotificationFactory;
import util.DistributedLock;
import util.RedisLock;

public class MobileModule extends AbstractModule {

    private final String jobsAddress;
    private final int jobsPort;

    public MobileModule(String jobsAddress, int jobsPort) {
        this.jobsAddress = jobsAddress;
        this.jobsPort = jobsPort;
    }

    @Override
    protected void configure() {
        GrpcJobsClient jobsClient = new GrpcJobsClient(jobsAddress, jobsPort);

        requestStaticInjection(NotificationFactory.class);
        bind(MongoDatabase.class).toInstance(StaticDB.database);
        bind(RedissonClient.class).toInstance(StaticDB.redis);

        bind(RemoteIndexer.class).toProvider(() -> jobsClient).in(Singleton.class);
        bind(LeaguePointsUpdater.class).toProvider(() -> jobsClient).in(Singleton.class);

        bind(DistributedLock.class).to(RedisLock.class).in(Singleton.class);
        bind(TeamRepository.class).to(DBTeamRepository.class).in(Singleton.class);
        bind(ScheduledDraftRepository.class).to(DBScheduledDraftRepository.class).in(Singleton.class);
        bind(AthleteRepository.class).to(DBAthleteRepository.class).in(Singleton.class);
        bind(OwnerRepository.class).to(DBOwnerRepository.class).in(Singleton.class);
        bind(LeagueRepository.class).to(DBLeagueRepository.class).in(Singleton.class);
        bind(EditLeagueRepository.class).to(DBEditLeagueRepository.class).in(Singleton.class);
        bind(StatsRepository.class).to(DBStatsRepository.class).in(Singleton.class);
        bind(QuotaRepository.class).to(DBQuotaRepository.class).in(Singleton.class);
        bind(ContractRepository.class).to(DBContractRepository.class).in(Singleton.class);
        bind(MeetRepository.class).to(DBMeetRepository.class).in(Singleton.class);
        bind(ResultRepository.class).to(DBResultRepository.class).in(Singleton.class);
        bind(NameRepository.class).to(CachedNameRepository.class).in(Singleton.class);
        bind(RosterRepository.class).to(DBRosterRepository.class).in(Singleton.class);
        bind(DraftRepository.class).to(DBDraftRepository.class).in(Singleton.class);
        bind(TradeRepository.class).to(DBTradeRepository.class).in(Singleton.class);
    }

}
