package domain.repository;

import java.time.Instant;

public interface SettingsRepository {

    int getTurnMillis();

    int getRounds();

    DraftStatus getDraftStatus();

    void setDraftStatus(DraftStatus draftStatus);

    Instant getStartTime();

}
