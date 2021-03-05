package fantasyapp.grpc.serializer;

import com.fantasytrack.protos.OwnerService;
import domain.entity.Owner;

public class OwnerSerializer {

    public static OwnerService.OwnerResponse serializeOwner(Owner owner) {
        return OwnerService.OwnerResponse.newBuilder()
                .setOwnerId(owner.id)
                .setOwnerName(owner.name)
                .build();
    }

}
