package usecase.draftStateSaver;

import fantasyapp.draftInstance.LeagueId;
import com.google.inject.Inject;
import domain.entity.Contract;
import domain.entity.Turn;
import domain.repository.ContractRepository;
import domain.repository.DraftStatus;
import domain.repository.SettingsRepository;
import domain.repository.TurnRepository;
import usecase.dto.ContractDTO;
import usecase.dto.DraftStateDTO;
import usecase.dto.TurnDTO;

import java.util.List;
import java.util.stream.Collectors;

public class DraftStateSnapshotBuilder implements DraftStateSaver {

    private TurnRepository turnRepository;
    private TurnAssembler turnAssembler;
    private DraftStateAssembler draftStateAssembler;

    private ContractRepository contractRepository;
    private ContractAssembler contractAssembler;
    private SettingsRepository settingsRepository;
    private String leagueId;

    @Inject
    public DraftStateSnapshotBuilder(TurnAssembler turnAssembler, TurnRepository turnRepository,
                                     DraftStateAssembler draftStateAssembler, ContractRepository contractRepository,
                                     ContractAssembler contractAssembler, SettingsRepository settingsRepository, @LeagueId String leagueId) {
        this.turnRepository = turnRepository;
        this.turnAssembler = turnAssembler;
        this.draftStateAssembler = draftStateAssembler;
        this.contractRepository = contractRepository;
        this.contractAssembler = contractAssembler;
        this.settingsRepository = settingsRepository;
        this.leagueId = leagueId;
    }

    @Override
    public DraftStateDTO makeSnapshot() {
        DraftStatus draftStatusDTO = settingsRepository.getDraftStatus();

        Turn turn = turnRepository.getCurrentTurn();
        TurnDTO turnDTO = (turn != null && draftStatusDTO == DraftStatus.RUNNING) ? turnAssembler.writeDTO(turn) : null;

        List<Contract> contracts = contractRepository.getLeagueContracts(leagueId);
        List<ContractDTO> contractDTOS = contracts.stream().map(contract -> contractAssembler.writeDTO(contract)).collect(Collectors.toList());

        return draftStateAssembler.writeDTO(turnDTO, contractDTOS, draftStatusDTO);
    }

}
