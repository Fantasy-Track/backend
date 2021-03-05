package fantasyapp.draftInstance.grpc;

import domain.repository.DraftStatus;
import com.fantasytrack.protos.DraftService;

public class DraftStatusMapper {

    public static DraftService.DraftStatus mapDraftStatusToDTO(DraftStatus draftStatus) {
        switch (draftStatus) {
            case RUNNING:
                return DraftService.DraftStatus.RUNNING;
            case NOT_STARTED:
                return DraftService.DraftStatus.NOT_STARTED;
            case FINISHED:
                return DraftService.DraftStatus.FINISHED;
            default:
                assert false;
                return null;
        }
    }

}
