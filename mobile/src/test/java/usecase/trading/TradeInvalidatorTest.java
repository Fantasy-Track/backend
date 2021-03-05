package usecase.trading;

import domain.entity.TradeStatus;
import domain.exception.ApplicationException;
import domain.exception.AthleteNotOnTeam;
import domain.repository.TradeRepository;
import mock.MockDistributedLock;
import mock.TradeBank;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class TradeInvalidatorTest {

    private TradeRepository tradeRepository;
    private TradeValidator validator;
    private TradeInvalidator invalidator;

    @BeforeMethod
    public void setUp() {
        tradeRepository = mock(TradeRepository.class);
        validator = mock(TradeValidator.class);
        invalidator = new TradeInvalidator(tradeRepository, validator, new MockDistributedLock());

        when(tradeRepository.getIncompleteTradesInLeague("test")).then(invocation -> List.of(TradeBank.proposal));
    }

    @Test
    public void testKeepValidTradesInLeague() {
        invalidator.cancelInvalidTrades("test");

        verify(tradeRepository, never()).setTradeStatus(eq(TradeBank.proposal.id), any());
    }

    @Test
    public void testCancelInvalidTradesInLeague() throws ApplicationException {
        doThrow(new AthleteNotOnTeam()).when(validator).validate(TradeBank.proposal);
        invalidator.cancelInvalidTrades("test");

        verify(tradeRepository).setTradeStatus(TradeBank.proposal.id, TradeStatus.CANCELLED);
    }
}