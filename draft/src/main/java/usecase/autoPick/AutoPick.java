package usecase.autoPick;

import fantasyapp.masterDraftServer.DraftRegistrar;
import domain.entity.Athlete;
import domain.entity.Turn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.util.annotation.NonNull;
import usecase.ContractEditor;
import usecase.ContractRequest;
import usecase.DraftContext;
import usecase.TimeoutBehavior;

public abstract class AutoPick implements TimeoutBehavior {

    private Logger logger = LoggerFactory.getLogger(AutoPick.class);
    private ContractEditor contractEditor;
    private DraftContext context;

    public AutoPick(ContractEditor contractEditor, DraftContext context) {
        this.contractEditor = contractEditor;
        this.context = context;
    }

    public final void pickForTurn(@NonNull Turn turn) {
        synchronized (DraftRegistrar.draftLocks.get(context.leagueId)) {
            if (turn.isHasPicked()) return;
            try {
                logger.info("Autopicking for turn: " + turn);
                Athlete athlete = decidePick();
                ContractRequest contractRequest = ContractRequest.builder()
                        .athleteId(athlete.id)
                        .teamId(turn.teamId)
                        .leagueId(context.leagueId)
                        .build();
                contractEditor.signContract(contractRequest);
            } catch (Exception e) {
                logger.error("Error with auto pick for turn: " + turn, e);
                System.exit(-1);
            }
            turn.setHasPicked(true);
        }
    }

    @Override
    public void handleTimeout(@NonNull  Turn turn) {
        pickForTurn(turn);
    }

    protected abstract Athlete decidePick();

}