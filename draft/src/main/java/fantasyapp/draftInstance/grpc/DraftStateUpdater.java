package fantasyapp.draftInstance.grpc;

import fantasyapp.draftInstance.grpc.serializer.DraftStateSerializer;
import fantasyapp.repository.listeners.DraftStatusListener;
import fantasyapp.repository.listeners.TurnChangeListener;
import domain.entity.Turn;
import domain.repository.DraftStatus;
import com.fantasytrack.protos.DraftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.draftStateSaver.DraftStateSaver;
import usecase.dto.DraftStateDTO;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DraftStateUpdater implements TurnChangeListener, DraftStatusListener {

    private Logger logger = LoggerFactory.getLogger(DraftStateUpdater.class);

    private DraftService.DraftState serializedDraftState;
    private DraftStateSaver stateSaver;

    private Map<Integer, SerializedStateListener> serializedStateListeners = new HashMap<>();

    @Inject
    public DraftStateUpdater(DraftStateSaver stateSaver) {
        this.stateSaver = stateSaver;
    }

    public void registerStateListenerAndGetLatest(SerializedStateListener listener) {
        serializedStateListeners.put(listener.hashCode(), listener);
        if (serializedDraftState != null) {
            listener.sendDraftState(serializedDraftState);
        }
        logger.info("Registered state listener. Total: " + serializedStateListeners.size());
    }

    public void unregisterStateListener(SerializedStateListener listener) {
        serializedStateListeners.remove(listener.hashCode());
        logger.info("Unregistered state listener. Total: " + serializedStateListeners.size());
    }

    @Override
    public void turnChanged(Turn turn) {
        logger.info("Turn changed, creating snapshot: " + turn.teamId);
        updateAndSendDraftState();
    }

    @Override
    public void draftStatusChanged(DraftStatus status) {
        logger.info("Status changed, creating snapshot: " + status.toString());
        updateAndSendDraftState();
    }

    private void updateAndSendDraftState() {
        DraftStateDTO draftStateDTO = stateSaver.makeSnapshot();
        serializedDraftState = DraftStateSerializer.serializeDraftState(draftStateDTO);
        serializedStateListeners.values().forEach((listener -> {
            try {
                listener.sendDraftState(serializedDraftState);
                //TODO consider unregistering the listener here
            } catch (Exception e) {
                logger.warn("Error sending update to client: " + Arrays.toString(e.getStackTrace()));
            }
        }));
    }

}