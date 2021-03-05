package usecase.dto;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class AthleteOwnershipDTO extends AthleteDTO {

    public final boolean owned;
    public final String teamId;
    public final String teamName;
    public final String positionId;

}
