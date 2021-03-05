package usecase;

import lombok.Builder;

@Builder
public class ContractRequest {

    public String teamId;
    public String athleteId;
    public String leagueId;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ContractRequest)) return false;
        return teamId.equals(((ContractRequest) obj).teamId) && athleteId.equals(((ContractRequest) obj).athleteId) && leagueId.equals(((ContractRequest) obj).leagueId);
    }

    @Override
    public String toString() {
        return "ContractRequest{" +
                "teamId='" + teamId + '\'' +
                ", athleteId='" + athleteId + '\'' +
                '}';
    }
}
