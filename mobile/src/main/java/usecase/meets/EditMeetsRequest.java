package usecase.meets;

import lombok.Builder;

import java.util.List;

@Builder
public class EditMeetsRequest {

    public final List<String> enableMeetIds;
    public final List<String> disableMeetIds;
    public final String leagueId;
    public final String teamId;

}
