package usecase.trading;

import domain.entity.TradeProposal;
import domain.entity.TradeStatus;
import domain.exception.ApplicationException;
import domain.repository.TradeRepository;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usecase.notification.NotificationFactory;
import usecase.notification.NotificationHandler;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class TradeProposerTest {

    private TradeValidator validator;
    private TradeRepository tradeRepository;
    private TradeProposer proposer;

    @BeforeMethod
    public void setUp() {
        validator = mock(TradeValidator.class);
        tradeRepository = mock(TradeRepository.class);
        proposer = new TradeProposer(validator, tradeRepository);
        NotificationFactory.handler = mock(NotificationHandler.class);
    }

    @Test
    public void testProposeTrade() throws ApplicationException {
        TradeProposalRequest request = TradeProposalRequest.builder()
                .leagueId("test")
                .proposingTeamId("T1")
                .acceptingTeamId("T2")
                .offeringAthletes(List.of("A1"))
                .receivingAthletes(List.of("A2"))
                .build();

        proposer.proposeTrade(request);
        verify(validator).validate(any());

        ArgumentCaptor<TradeProposal> captor = ArgumentCaptor.forClass(TradeProposal.class);
        verify(tradeRepository).addTradeProposal(captor.capture());

        assertNotNull(captor.getValue().id);
        assertEquals(captor.getValue().status, TradeStatus.PENDING);
        assertEquals(captor.getValue().proposingTeamId, "T1");
        assertEquals(captor.getValue().acceptingTeamId, "T2");
        assertEquals(captor.getValue().leagueId, "test");
        assertEquals(captor.getValue().offeringAthletes, List.of("A1"));
        assertEquals(captor.getValue().receivingAthletes, List.of("A2"));
    }
}