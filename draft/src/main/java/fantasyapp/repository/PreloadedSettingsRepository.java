package fantasyapp.repository;

import fantasyapp.dao.DraftSettingsDAO;
import fantasyapp.dao.LeagueDAO;
import fantasyapp.draftInstance.LeagueId;
import fantasyapp.repository.listeners.DraftStatusListener;
import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import domain.repository.DraftStatus;
import domain.repository.SettingsRepository;

import java.time.Instant;

import static com.mongodb.client.model.Filters.eq;

public class PreloadedSettingsRepository implements SettingsRepository {

    private DraftStatusListener draftStatusListener;

    private String leagueId;
    private DraftStatus draftStatus = DraftStatus.NOT_STARTED;

    private DraftSettingsDAO settingsDAO;

    @Inject
    public PreloadedSettingsRepository(MongoDatabase database, DraftStatusListener draftStatusListener, @LeagueId String leagueId) {
        this.leagueId = leagueId;
        this.draftStatusListener = draftStatusListener;
        preload(database);
    }

    private void preload(MongoDatabase database) {
        MongoCollection<LeagueDAO> leagues = database.getCollection("leagues", LeagueDAO.class);
        LeagueDAO league = leagues.find(eq("_id", leagueId)).first();
        assert league != null;
        this.settingsDAO = league.getDraftSettings();
    }

    @Override
    public int getTurnMillis() {
        return settingsDAO.getTurnDurationMillis();
    }

    @Override
    public int getRounds() {
        return settingsDAO.getRounds();
    }

    @Override
    public DraftStatus getDraftStatus() {
        return draftStatus;
    }

    @Override
    public void setDraftStatus(DraftStatus draftStatus) {
        this.draftStatus = draftStatus;
        if (draftStatusListener != null) {
            draftStatusListener.draftStatusChanged(draftStatus);
        }
    }

    @Override
    public Instant getStartTime() {
        return settingsDAO.getStartTime();
    }


}
