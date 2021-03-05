package usecase.tradeProcessor;

import domain.entity.League;
import domain.entity.TradeProposal;
import domain.entity.TradeStatus;
import domain.exception.ApplicationException;
import domain.exception.TradeNotAccepted;
import domain.repository.LeagueRepository;
import domain.repository.TradeRepository;
import mock.MockDistributedLock;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usecase.ContractEditor;
import usecase.ContractRequest;
import usecase.notification.NotificationFactory;
import usecase.notification.NotificationHandler;
import usecase.trading.TradeInvalidator;
import usecase.trading.TradeValidator;
import util.DistributedLock;

import java.util.List;

import static org.mockito.Mockito.*;

public class TradeProcessorTest {

    private TradeProcessor processor;
    private TradeValidator validator;
    private ContractEditor editor;
    private TradeRepository tradeRepository;
    private LeagueRepository leagueRepository;
    private TradeInvalidator tradeInvalidator;

    private TradeProposal successTrade = TradeProposal.builder()
            .id("TR1")
            .proposingTeamId("T1")
            .acceptingTeamId("T2")
            .leagueId("test")
            .offeringAthletes(List.of("A1", "A2"))
            .receivingAthletes(List.of("A3", "A4"))
            .status(TradeStatus.ACCEPTED_WAITING)
            .build();


    @BeforeMethod
    public void setUp() {
        DistributedLock lock = new MockDistributedLock();
        validator = mock(TradeValidator.class);
        editor = mock(ContractEditor.class);
        tradeRepository = mock(TradeRepository.class);
        leagueRepository = mock(LeagueRepository.class);
        tradeInvalidator = mock(TradeInvalidator.class);

        when(leagueRepository.getLeagueById("test")).then(invocation -> League.builder().build());
        processor = new TradeProcessor(validator, editor, tradeRepository, tradeInvalidator, lock);
        NotificationFactory.handler = mock(NotificationHandler.class);
    }


    @Test
    public void testTradeInWrongState() throws ApplicationException {
        TradeProposal trade = TradeProposal.builder()
                .leagueId("test")
                .status(TradeStatus.ACCEPTED_ENACTED) // can be anything but ACCEPTED_WAITING
                .build();
        try {
            processor.processTrade(trade);
            Assert.fail();
        } catch (TradeNotAccepted ignored) {}
    }

    @Test
    public void testProcessTrade() throws ApplicationException {
        processor.processTrade(successTrade);

        verify(validator).validate(successTrade);

        verify(editor).deleteContract("A1", "T1");
        verify(editor).deleteContract("A2", "T1");
        verify(editor).signContract(ContractRequest.builder().athleteId("A1").teamId("T2").leagueId("test").build());
        verify(editor).signContract(ContractRequest.builder().athleteId("A2").teamId("T2").leagueId("test").build());

        verify(editor).deleteContract("A3", "T2");
        verify(editor).deleteContract("A4", "T2");
        verify(editor).signContract(ContractRequest.builder().athleteId("A3").teamId("T1").leagueId("test").build());
        verify(editor).signContract(ContractRequest.builder().athleteId("A4").teamId("T1").leagueId("test").build());

        verify(tradeRepository).setTradeStatus("TR1", TradeStatus.ACCEPTED_ENACTED);
        verify(tradeInvalidator).cancelInvalidTrades("test");
    }
}