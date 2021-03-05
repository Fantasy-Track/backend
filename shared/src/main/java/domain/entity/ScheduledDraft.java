package domain.entity;

import lombok.Builder;

import java.time.Instant;

@Builder
public class ScheduledDraft {

    public final String id;
    public final Instant time;

}
