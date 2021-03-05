package usecase.team;

import lombok.Builder;

@Builder
public class TeamDTO {

    public final String id;
    public final String name;
    public final String ownerId;
    public final String ownerName;
    public final String leagueId;
    public final String leagueName;
    public final double fantasyPoints;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TeamDTO) {
            return ((TeamDTO) obj).id.equals(this.id);
        }
        return false;
    }

}
