package usecase;

import fantasyapp.masterDraftServer.DraftRegistrar;
import com.google.inject.Inject;
import domain.entity.Turn;
import domain.exception.ApplicationException;
import domain.exception.NotCurrentlyPicking;
import domain.repository.DraftStatus;
import domain.repository.SettingsRepository;
import domain.repository.TurnRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DraftAthlete {

    private final Logger logger = LoggerFactory.getLogger(DraftAthlete.class);

    private TurnRepository turnRepository;
    private ContractEditor contractEditor;
    private DraftContext context;
    private SettingsRepository settingsRepository;

    @Inject
    public DraftAthlete(TurnRepository turnRepository, ContractEditor contractEditor, DraftContext context, SettingsRepository settingsRepository) {
        this.turnRepository = turnRepository;
        this.contractEditor = contractEditor;
        this.context = context;
        this.settingsRepository = settingsRepository;
    }

    public void draft(ContractRequest contract) throws ApplicationException {
        synchronized (DraftRegistrar.draftLocks.get(context.leagueId)) {
            logger.info("User requesting contract: " + contract);
            Turn currTurn = turnRepository.getCurrentTurn();
            if (settingsRepository.getDraftStatus() != DraftStatus.RUNNING
                    || currTurn == null
                    || !(currTurn.teamId.equals(contract.teamId))
                    || currTurn.isHasPicked()) {
                throw new NotCurrentlyPicking();
            }
            contractEditor.signContract(contract);
            currTurn.setHasPicked(true);
            DraftRegistrar.draftLocks.get(context.leagueId).notify();
        }
    }

}
