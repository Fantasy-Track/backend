package domain.repository;

import domain.entity.ScheduledDraft;

import java.time.Instant;
import java.util.List;

public interface ScheduledDraftRepository {

    List<ScheduledDraft> getDraftsBefore(Instant time);

    void deleteScheduledDraft(String id);

    ScheduledDraft getDraftForLeague(String leagueId);

}
