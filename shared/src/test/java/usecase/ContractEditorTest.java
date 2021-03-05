package usecase;

import domain.entity.Contract;
import domain.exception.ApplicationException;
import domain.exception.AthleteNotOnTeam;
import domain.repository.ContractRepository;
import org.mockito.ArgumentCaptor;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usecase.trading.TradeInvalidator;
import usecase.trading.TradeValidator;

import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class ContractEditorTest {

    private ContractValidator validator;
    private ContractRepository contractRepository;
    private ContractEditor contractEditor;

    @BeforeMethod
    public void setUp() {
        validator = mock(ContractValidator.class);
        contractRepository = mock(ContractRepository.class);
        contractEditor = new ContractEditor(validator, contractRepository);
    }

    // potentially unneeded test
    @Test
    public void testSignContract(){
        ContractRequest contractRequest = ContractRequest.builder()
                .athleteId("A1")
                .teamId("T1")
                .leagueId("test")
                .build();

        try {
            contractEditor.signContract(contractRequest);
        } catch (ApplicationException e) {
            Assert.fail();
        }

        ArgumentCaptor<Contract> captor = ArgumentCaptor.forClass(Contract.class);
        verify(contractRepository).signContract(captor.capture());
        assertEquals(captor.getValue().eventId, "-5");
        assertEquals(captor.getValue().athleteId, "A1");
        assertEquals(captor.getValue().teamId, "T1");
        assertEquals(captor.getValue().leagueId, "test");
    }

    @Test
    public void testDeleteContractSuccess() throws AthleteNotOnTeam {
        when(contractRepository.isAthleteOnTeam("A1", "T1")).then(invocation -> true);

        contractEditor.deleteContract("A1", "T1");
        verify(contractRepository).deleteContract("A1", "T1");
    }

    @Test
    public void testDeleteContractFail() {
        when(contractRepository.isAthleteOnTeam("A1", "T1")).then(invocation -> false);

        try {
            contractEditor.deleteContract("A1", "T1");
            Assert.fail();
        } catch (AthleteNotOnTeam ignored) { }
        verify(contractRepository, times(0)).deleteContract("A1", "T1");
    }
}