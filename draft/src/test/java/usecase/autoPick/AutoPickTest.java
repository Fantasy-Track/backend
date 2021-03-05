package usecase.autoPick;

import fantasyapp.masterDraftServer.DraftRegistrar;
import domain.entity.Athlete;
import domain.exception.ApplicationException;
import domain.repository.AgentRepository;
import domain.repository.mock.MockAgentRepository;
import domain.repository.mock.TurnBank;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.annotations.Test;
import usecase.ContractEditor;
import usecase.ContractRequest;
import usecase.DraftContext;
import usecase.mock.MockAutoPick;

import static org.testng.Assert.assertEquals;

public class AutoPickTest {

    @Test
    public void testDumbAutoPick() {
        AgentRepository agentRepository = new MockAgentRepository();
        ContractEditor contractEditor = Mockito.mock(ContractEditor.class);

        DumbAutoPick autoPick = new DumbAutoPick(contractEditor, agentRepository, new DraftContext("test"));

        Athlete agent = autoPick.decidePick();

        assertEquals(agent.id, "A");
    }

    @Test
    public void testAutoPick() throws ApplicationException {
        ContractEditor contractEditor = Mockito.mock(ContractEditor.class);

        AutoPick autoPick = new MockAutoPick(contractEditor, Athlete.builder().id("K").build());

        DraftRegistrar.draftLocks.put("test", new Object());
        autoPick.pickForTurn(TurnBank.TURN1);

        ArgumentCaptor<ContractRequest> argument = ArgumentCaptor.forClass(ContractRequest.class);
        Mockito.verify(contractEditor, Mockito.times(1)).signContract(argument.capture());

        assertEquals(argument.getValue().teamId, "T1");
        assertEquals(argument.getValue().athleteId, "K");
    }


}