package usecase.dto;

import domain.repository.DraftStatus;

import java.util.List;

public class DraftStateDTO {

    private TurnDTO turnDTO;
    private List<ContractDTO> contractDTOs;
    private DraftStatus draftStatusDTO;

    public TurnDTO getTurnDTO() {
        return turnDTO;
    }

    public void setTurnDTO(TurnDTO turnDTO) {
        this.turnDTO = turnDTO;
    }

    public List<ContractDTO> getContractDTOs() {
        return contractDTOs;
    }

    public void setContractDTOs(List<ContractDTO> contractDTOs) {
        this.contractDTOs = contractDTOs;
    }

    public DraftStatus getDraftStatusDTO() {
        return draftStatusDTO;
    }

    public void setDraftStatusDTO(DraftStatus draftStatusDTO) {
        this.draftStatusDTO = draftStatusDTO;
    }
}
