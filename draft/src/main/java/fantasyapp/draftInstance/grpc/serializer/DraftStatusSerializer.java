package fantasyapp.draftInstance.grpc.serializer;

import domain.repository.DraftStatus;
import com.fantasytrack.protos.DraftService;

public class DraftStatusSerializer {

    static DraftService.DraftStatus serializeDraftStatus(DraftStatus draftStatus) {
        switch (draftStatus) {
            case FINISHED:
                return DraftService.DraftStatus.FINISHED;
            case RUNNING:
                return DraftService.DraftStatus.RUNNING;
            case NOT_STARTED:
                return DraftService.DraftStatus.NOT_STARTED;
            default:
                assert false;
                return DraftService.DraftStatus.NOT_STARTED;
        }
    }

}
