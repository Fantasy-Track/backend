package fantasyapp.draftInstance;

import fantasyapp.StaticDB;
import fantasyapp.draftInstance.grpc.DraftStateUpdater;
import fantasyapp.repository.*;
import fantasyapp.repository.listeners.DraftStatusListener;
import fantasyapp.repository.listeners.TurnChangeListener;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.mongodb.client.MongoDatabase;
import domain.repository.*;
import org.redisson.api.RedissonClient;
import usecase.DraftContext;
import usecase.SnakeTurnRotation;
import usecase.TimeoutBehavior;
import usecase.TurnRotation;
import usecase.autoPick.BestProjectionAutoPick;
import usecase.draftInfoFetcher.DraftInfoFetcher;
import usecase.draftStateSaver.DraftStateSaver;
import usecase.draftStateSaver.DraftStateSnapshotBuilder;
import usecase.notification.NotificationFactory;
import usecase.port.DraftInfoPort;
import util.DistributedLock;
import util.RedisLock;

public class DraftInstanceModule extends AbstractModule {

    private final String LEAGUE_ID;

    public DraftInstanceModule(String league_id) {
        LEAGUE_ID = league_id;
    }

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(LeagueId.class).toInstance(LEAGUE_ID);
        bind(MongoDatabase.class).toInstance(StaticDB.database);
        bind(RedissonClient.class).toInstance(StaticDB.redis);
        bind(DraftContext.class).toInstance(new DraftContext(LEAGUE_ID));

        requestStaticInjection(NotificationFactory.class);
        bind(ContractRepository.class).to(DBContractRepository.class).in(Singleton.class);
        bind(TeamRepository.class).to(DBTeamRepository.class).in(Singleton.class);
        bind(SettingsRepository.class).to(PreloadedSettingsRepository.class).in(Singleton.class);
        bind(PickOrderRepository.class).to(InMemoryPickOrder.class).in(Singleton.class);
        bind(TurnRepository.class).to(InMemoryTurnRepository.class).in(Singleton.class);
        bind(NameRepository.class).to(CachedNameRepository.class).in(Singleton.class);
        bind(AthleteRepository.class).to(DBAthleteRepository.class).in(Singleton.class);
        bind(AgentRepository.class).to(DBAgentRepository.class).in(Singleton.class);
        bind(LeagueRepository.class).to(DBLeagueRepository.class).in(Singleton.class);
        bind(DistributedLock.class).to(RedisLock.class).in(Singleton.class);

        bind(TurnRotation.class).to(SnakeTurnRotation.class).in(Singleton.class);
        bind(TimeoutBehavior.class).to(BestProjectionAutoPick.class).in(Singleton.class);

        bind(TurnChangeListener.class).to(DraftStateUpdater.class).in(Singleton.class);
        bind(DraftStatusListener.class).to(DraftStateUpdater.class).in(Singleton.class);
        bind(DraftStateSaver.class).to(DraftStateSnapshotBuilder.class).in(Singleton.class);

        bind(DraftInfoPort.class).to(DraftInfoFetcher.class).in(Singleton.class);

        bind(DraftStateUpdater.class).in(Singleton.class);
    }

}
