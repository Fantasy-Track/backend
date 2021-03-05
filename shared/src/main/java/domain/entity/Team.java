package domain.entity;

import lombok.Builder;

@Builder
public class Team {

    public final String id;
    public final String leagueId;
    public final String ownerId;
    public final String name;
    public final double fantasyPoints;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Team) {
            return ((Team) obj).id.equals(this.id);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id='" + id + '\'' +
                ", leagueId='" + leagueId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", name='" + name + '\'' +
                ", fantasyPoints=" + fantasyPoints +
                '}';
    }
}
