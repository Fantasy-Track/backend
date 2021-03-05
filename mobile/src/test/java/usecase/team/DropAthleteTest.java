package usecase.team;

import domain.entity.League;
import domain.entity.LeagueStatus;
import domain.exception.ApplicationException;
import domain.exception.CannotDoActionNow;
import domain.repository.LeagueRepository;
import mock.MockDistributedLock;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usecase.ContractEditor;
import usecase.trading.DropAthlete;
import usecase.trading.DropAthletesRequest;
import usecase.trading.TradeInvalidator;

import java.util.List;

import static org.mockito.Mockito.*;

public class DropAthleteTest {

    private ContractEditor contractEditor;
    private DropAthlete dropAthlete;

    private LeagueRepository leagueRepository;
    private TradeInvalidator tradeInvalidator;

    private DropAthletesRequest request = DropAthletesRequest.builder()
            .athleteIds(List.of("A1"))
            .teamId("T1")
            .leagueId("test")
            .build();

    @BeforeMethod
    public void setUp() {
        contractEditor = Mockito.mock(ContractEditor.class);
        leagueRepository = mock(LeagueRepository.class);
        tradeInvalidator = mock(TradeInvalidator.class);
        dropAthlete = new DropAthlete(contractEditor, leagueRepository, tradeInvalidator, new MockDistributedLock());
    }

    @Test
    public void testDrop() throws ApplicationException {
        when(leagueRepository.getLeagueById("test")).then(invocation -> League.builder().status(LeagueStatus.POST_DRAFT).build());
        dropAthlete.dropAthletes(request);
        verify(contractEditor).deleteContract("A1", "T1");
        verify(tradeInvalidator).cancelInvalidTrades("test");
    }

    @Test
    public void testNotPostDraft() throws ApplicationException {
        when(leagueRepository.getLeagueById("test")).then(invocation -> League.builder().status(LeagueStatus.DRAFTING).build());
        try {
            dropAthlete.dropAthletes(request);
            Assert.fail();
        } catch (CannotDoActionNow ignored) {}
    }

}