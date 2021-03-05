package usecase.createOwner;

import lombok.Builder;

@Builder
public class CreateOwnerRequest {

    public final String id;
    public final String name;

}
