package usecase;

import domain.entity.Contract;
import domain.exception.AgentNotFree;
import domain.exception.ApplicationException;
import domain.exception.AthleteNotExists;
import domain.repository.AthleteRepository;
import domain.repository.ContractRepository;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContractValidatorTest {

    private ContractRepository contractRepository;
    private ContractValidator validator;

    @BeforeMethod
    public void setUp() {
        contractRepository = mock(ContractRepository.class);
        AthleteRepository athleteRepository = mock(AthleteRepository.class);
        when(athleteRepository.isAthleteInLeague("A1", "test")).then(invocation -> true);

        validator = new ContractValidator(contractRepository, athleteRepository);
    }

    @Test
    public void testValidateContractSuccess() {
        when(contractRepository.isAthleteFreeAgent("A1", "test")).then(invocation -> true);

        Contract contract = Contract.builder()
                .teamId("T1")
                .athleteId("A1")
                .eventId("-5")
                .leagueId("test")
                .dateSigned(Instant.now())
                .build();
        try {
            validator.validateContract(contract);
        } catch (ApplicationException e) {
            Assert.fail();
        }
    }

    @Test
    public void testAthleteNotFree() throws ApplicationException {
        when(contractRepository.isAthleteFreeAgent("A1", "test")).then(invocation -> false);

        Contract contract = Contract.builder()
                .teamId("imaginaryTeam")
                .dateSigned(Instant.now())
                .athleteId("A1")
                .leagueId("test")
                .eventId("-5")
                .build();

        try {
            validator.validateContract(contract);
            Assert.fail();
        } catch (AgentNotFree e) {
            // Expected Behavior
        }
    }

    @Test
    public void testAthleteNotExists() throws ApplicationException {
        Contract contract = Contract.builder()
                .teamId("T1")
                .dateSigned(Instant.now())
                .athleteId("imaginaryAthlete")
                .leagueId("test")
                .eventId("-5")
                .build();

        try {
            validator.validateContract(contract);
            Assert.fail();
        } catch (AthleteNotExists e) {
            // Expected Behavior
        }
    }
}