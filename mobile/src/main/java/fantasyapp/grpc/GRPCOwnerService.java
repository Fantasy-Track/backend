package fantasyapp.grpc;

import com.fantasytrack.protos.OwnerGrpc;
import com.fantasytrack.protos.OwnerService;
import fantasyapp.grpc.serializer.OwnerSerializer;
import com.google.inject.Inject;
import domain.entity.Owner;
import domain.exception.ApplicationException;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.createOwner.EditOwnerInfo;

public class GRPCOwnerService extends OwnerGrpc.OwnerImplBase {

    private Logger logger = LoggerFactory.getLogger(GRPCOwnerService.class);
    private final EditOwnerInfo editOwnerInfo;

    @Inject
    public GRPCOwnerService(EditOwnerInfo editOwnerInfo) {
        this.editOwnerInfo = editOwnerInfo;
    }

    @Override
    public void getOwner(OwnerService.OwnerRequest request, StreamObserver<OwnerService.OwnerResponse> responseObserver) {
        try {
            Owner owner = editOwnerInfo.getOwnerById(request.getOwnerId());
            responseObserver.onNext(OwnerSerializer.serializeOwner(owner));
            responseObserver.onCompleted();
        } catch (ApplicationException e) {
            logger.warn("Couldn't find owner", e);
            responseObserver.onError(GrpcError.makeError(e));
        }
    }

    @Override
    public void editOwnerName(OwnerService.OwnerNameRequest request, StreamObserver<OwnerService.OwnerResponse> responseObserver) {
        try {
            Owner owner = editOwnerInfo.editOwnerName(Authenticator.ownerKey.get(), request.getOwnerName());
            responseObserver.onNext(OwnerSerializer.serializeOwner(owner));
            responseObserver.onCompleted();
            logger.info("Edited owner name: " + request.getOwnerName());
        } catch (ApplicationException e) {
            logger.info("Couldn't edit owner name", e);
            responseObserver.onError(GrpcError.makeError(e));
        }
    }
}
