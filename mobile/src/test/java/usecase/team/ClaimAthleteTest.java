package usecase.team;

import domain.entity.Contract;
import domain.entity.League;
import domain.entity.LeagueStatus;
import domain.exception.AgentNotFree;
import domain.exception.ApplicationException;
import domain.exception.CannotDoActionNow;
import domain.exception.TeamFull;
import domain.repository.ContractRepository;
import domain.repository.LeagueRepository;
import mock.MockDistributedLock;
import org.mockito.ArgumentCaptor;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usecase.ContractEditor;
import usecase.ContractRequest;
import usecase.trading.ClaimAthlete;
import usecase.trading.ClaimAthleteRequest;
import usecase.trading.TradeInvalidator;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class ClaimAthleteTest {

    private ClaimAthlete claimAthlete;
    private ContractEditor contractEditor;
    LeagueRepository leagueRepository;

    private static ClaimAthleteRequest request = ClaimAthleteRequest.builder()
            .athleteId("A1")
            .leagueId("test")
            .teamId("T1")
            .build();

    @BeforeMethod
    public void setUp() {
        contractEditor = mock(ContractEditor.class);
        leagueRepository = mock(LeagueRepository.class);
        when(leagueRepository.getLeagueById("test")).then(invocation -> League.builder().leagueSettings(League.LeagueSettings.builder().teamSize(2).build()).status(LeagueStatus.POST_DRAFT).build());

        ContractRepository contractRepository = mock(ContractRepository.class);
        when(contractRepository.getTeamContracts("T1")).then(invocation -> List.of(Contract.builder().build()));

        claimAthlete = new ClaimAthlete(contractEditor, contractRepository, leagueRepository, new MockDistributedLock());
    }

    @Test
    public void testTeamFull() throws ApplicationException {
        when(leagueRepository.getLeagueById("test")).then(invocation -> League.builder().leagueSettings(League.LeagueSettings.builder().teamSize(1).build()).status(LeagueStatus.POST_DRAFT).build());

        try {
            claimAthlete.claimAthlete(request);
            Assert.fail();
        } catch (TeamFull ignored) { }
    }

    @Test
    public void testLeagueNotPostDraft() throws ApplicationException {
        when(leagueRepository.getLeagueById("test")).then(invocation -> League.builder().leagueSettings(League.LeagueSettings.builder().teamSize(2).build()).status(LeagueStatus.DRAFTING).build());
        try {
            claimAthlete.claimAthlete(request);
            Assert.fail();
        } catch (CannotDoActionNow ignored) { }
    }

    @Test
    public void testClaimNotFreeAthlete() throws ApplicationException {
        doThrow(new AgentNotFree()).when(contractEditor).signContract(any());

        try {
            claimAthlete.claimAthlete(request);
            Assert.fail();
        } catch (AgentNotFree ignored) { }
    }

    @Test
    public void testClaimAthleteSuccess() throws ApplicationException {
        claimAthlete.claimAthlete(request);

        ArgumentCaptor<ContractRequest> captor = ArgumentCaptor.forClass(ContractRequest.class);
        verify(contractEditor).signContract(captor.capture());
        assertEquals(captor.getValue().teamId, "T1");
        assertEquals(captor.getValue().athleteId, "A1");
        assertEquals(captor.getValue().leagueId, "test");
    }

}