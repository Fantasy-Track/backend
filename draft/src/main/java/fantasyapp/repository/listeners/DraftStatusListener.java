package fantasyapp.repository.listeners;

import domain.repository.DraftStatus;

public interface DraftStatusListener {

    void draftStatusChanged(DraftStatus status);

}
