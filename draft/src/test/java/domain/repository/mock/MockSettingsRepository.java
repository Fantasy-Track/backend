package domain.repository.mock;

import domain.repository.DraftStatus;
import domain.repository.SettingsRepository;

import java.time.Instant;

public class MockSettingsRepository implements SettingsRepository {

    private DraftStatus draftStatus = DraftStatus.NOT_STARTED;
    private final int totalRounds;

    public MockSettingsRepository(int totalRounds) {
        this.totalRounds = totalRounds;
    }

    @Override
    public int getTurnMillis() {
        return 3000;
    }

    @Override
    public int getRounds() {
        return totalRounds;
    }

    @Override
    public DraftStatus getDraftStatus() {
        return draftStatus;
    }

    @Override
    public void setDraftStatus(DraftStatus draftStatus) {
        this.draftStatus = draftStatus;
    }

    @Override
    public Instant getStartTime() {
        return Instant.now();
    }

}
