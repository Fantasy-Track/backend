package domain.entity;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public class Meet {

    public final String id, athleticId;
    public final String leagueId;
    public final String name;
    public final Instant date;
    public final boolean hasResults;
    public final boolean enabled;
    public final boolean locked;
    public final boolean rescore;
    public final List<Contract> savedContracts;

    @Override
    public String toString() {
        return "Meet{" +
                "id='" + id + '\'' +
                ", athleticId='" + athleticId + '\'' +
                ", leagueId='" + leagueId + '\'' +
                ", name='" + name + '\'' +
                ", date=" + date +
                '}';
    }
}
