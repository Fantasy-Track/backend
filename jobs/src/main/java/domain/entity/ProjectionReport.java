package domain.entity;

import lombok.Builder;

@Builder
public class ProjectionReport {

    public final String athleteId;
    public final double projection;
    public final String eventId;

    @Override
    public String toString() {
        return "ProjectionReport{" +
                "athleteId='" + athleteId + '\'' +
                ", projection=" + projection +
                ", eventId='" + eventId + '\'' +
                '}';
    }
}
