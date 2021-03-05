package usecase.draftStateSaver;

import domain.repository.DraftStatus;
import usecase.dto.ContractDTO;
import usecase.dto.DraftStateDTO;
import usecase.dto.TurnDTO;

import java.util.List;

public class DraftStateAssembler {

    public DraftStateDTO writeDTO(TurnDTO turnDTO, List<ContractDTO> contractDTOs, DraftStatus draftStatusDTO) {
        DraftStateDTO dto = new DraftStateDTO();
        dto.setTurnDTO(turnDTO);
        dto.setContractDTOs(contractDTOs);
        dto.setDraftStatusDTO(draftStatusDTO);
        return dto;
    }

}
