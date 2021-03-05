package usecase.draftStateSaver;

import usecase.dto.DraftStateDTO;

public interface DraftStateSaver {

    DraftStateDTO makeSnapshot();

}
