package usecase.trading;

import domain.entity.TradeProposal;
import domain.entity.TradeStatus;
import domain.exception.ApplicationException;
import domain.exception.TradeNotExists;
import domain.exception.TradeNotPending;
import domain.exception.UnauthorizedException;
import domain.repository.TradeRepository;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usecase.notification.NotificationFactory;
import usecase.notification.NotificationHandler;

import static org.mockito.Mockito.*;

public class TradeApproverTest {

    TradeProposal pendingTrade = TradeProposal.builder()
            .id("TR1")
            .proposingTeamId("T1")
            .acceptingTeamId("T2")
            .status(TradeStatus.PENDING)
            .build();

    private TradeRepository tradeRepository;
    private TradeValidator validator;
    private TradeApprover tradeApprover;

    @BeforeMethod
    public void setUp() {
        validator = mock(TradeValidator.class);
        tradeRepository = mock(TradeRepository.class);
        tradeApprover = new TradeApprover(validator, tradeRepository);

        when(tradeRepository.getTradeById("TR1")).then(invocation -> pendingTrade);
        NotificationFactory.handler = mock(NotificationHandler.class);
    }

    @Test
    public void testCancelSuccess() throws ApplicationException {
        tradeApprover.cancelTrade("TR1", "T1");
        verify(tradeRepository).setTradeStatus("TR1", TradeStatus.CANCELLED);
    }

    @Test
    public void testAcceptSuccess() throws ApplicationException {
        tradeApprover.acceptTrade("TR1", "T2");
        verify(validator).validate(pendingTrade);
        verify(tradeRepository).setTradeStatus("TR1", TradeStatus.ACCEPTED_WAITING);
    }

    @Test
    public void testRejectSuccess() throws ApplicationException {
        tradeApprover.rejectTrade("TR1", "T2");
        verify(tradeRepository).setTradeStatus("TR1", TradeStatus.REJECTED);
    }

    @Test
    public void testTradeNotAuthorized() throws ApplicationException {
        try {
            tradeApprover.rejectTrade("TR1", "T1"); // does not matter which one is it
            Assert.fail();
        } catch (UnauthorizedException ignored) {}
        verify(tradeRepository, times(0)).setTradeStatus(any(), any());
    }

    @Test
    public void testTradeNotPending() throws ApplicationException {
        when(tradeRepository.getTradeById("TR1")).then(invocation -> TradeProposal.builder()
                .id("TR1")
                .proposingTeamId("T1")
                .acceptingTeamId("T2")
                .status(TradeStatus.ACCEPTED_WAITING)
                .build());

        try {
            tradeApprover.rejectTrade("TR1", "T1");
            Assert.fail();
        } catch (TradeNotPending ignored) {}
    }

    @Test
    public void testTradeNotExists() throws ApplicationException {
        try {
            tradeApprover.rejectTrade("TR2", "T1"); // does not matter which one is it
            Assert.fail();
        } catch (TradeNotExists ignored) {}
        verify(tradeRepository, times(0)).setTradeStatus(any(), any());
    }

}