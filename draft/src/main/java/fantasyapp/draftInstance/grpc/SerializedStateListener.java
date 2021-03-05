package fantasyapp.draftInstance.grpc;

import com.fantasytrack.protos.DraftService;

public interface SerializedStateListener {

    void sendDraftState(DraftService.DraftState draftState);

}
