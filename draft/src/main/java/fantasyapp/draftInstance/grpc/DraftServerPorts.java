package fantasyapp.draftInstance.grpc;

import usecase.port.DraftInfoPort;
import usecase.port.EventQueueInput;

import javax.inject.Inject;

public class DraftServerPorts {

    public final DraftStateUpdater draftStateUpdater;
    public final DraftInfoPort draftInfoPort;
    public final EventQueueInput queueInputPort;

    @Inject
    public DraftServerPorts(DraftStateUpdater draftStateUpdater, DraftInfoPort draftInfoPort, EventQueueInput queueInputPort) {
        this.draftStateUpdater = draftStateUpdater;
        this.draftInfoPort = draftInfoPort;
        this.queueInputPort = queueInputPort;
    }


}
