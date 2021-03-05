package usecase.trading;

import domain.entity.Contract;
import domain.entity.League;
import domain.entity.LeagueStatus;
import domain.entity.TradeProposal;
import domain.exception.*;
import domain.repository.ContractRepository;
import domain.repository.LeagueRepository;
import domain.repository.TeamRepository;
import mock.TeamBank;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TradeValidatorTest {

    private LeagueRepository leagueRepository;
    private TradeValidator tradeValidator;

    @BeforeMethod
    public void setUp() {
        TeamRepository teamRepository = mock(TeamRepository.class);
        when(teamRepository.getTeamById("T1")).then(invocation -> TeamBank.TEAM1);
        when(teamRepository.getTeamById("T2")).then(invocation -> TeamBank.TEAM2);
        when(teamRepository.getTeamById("T3")).then(invocation -> TeamBank.OTHER_LEAGUE_TEAM);

        ContractRepository contractRepository = mock(ContractRepository.class);
        when(contractRepository.isAthleteOnTeam("A1", "T1")).then(invocation -> true);
        when(contractRepository.isAthleteOnTeam("A4", "T1")).then(invocation -> true);
        when(contractRepository.isAthleteOnTeam("A2", "T2")).then(invocation -> true);
        when(contractRepository.isAthleteOnTeam("A3", "T2")).then(invocation -> true);
        when(contractRepository.getTeamContracts("T1")).then(invocation -> List.of(Contract.builder().build()));

        leagueRepository = mock(LeagueRepository.class);
        when(leagueRepository.getLeagueById("test")).then(invocation -> League.builder()
                .leagueSettings(League.LeagueSettings.builder().teamSize(1).build())
                .status(LeagueStatus.POST_DRAFT)
                .build());

        tradeValidator = new TradeValidator(contractRepository, teamRepository, leagueRepository);
    }

    @Test
    public void testLeagueNotPostDraft() throws ApplicationException {
        when(leagueRepository.getLeagueById("test")).then(invocation -> League.builder()
                .leagueSettings(League.LeagueSettings.builder().teamSize(1).build())
                .status(LeagueStatus.DRAFTING)
                .build());
        TradeProposal request = TradeProposal.builder()
                .leagueId("test")
                .proposingTeamId("T1")
                .offeringAthletes(List.of("A1"))
                .acceptingTeamId("T2")
                .receivingAthletes(List.of("A2"))
                .build();

        try {
            tradeValidator.validate(request);
            Assert.fail();
        } catch (CannotDoActionNow ignored) {}
    }

    @Test
    public void testSuccess() throws ApplicationException {
        TradeProposal request = TradeProposal.builder()
                .leagueId("test")
                .proposingTeamId("T1")
                .offeringAthletes(List.of("A1"))
                .acceptingTeamId("T2")
                .receivingAthletes(List.of("A2"))
                .build();

        tradeValidator.validate(request);
    }

    @Test
    public void testDropAndReceiveEqual() throws ApplicationException {
        TradeProposal request = TradeProposal.builder()
                .leagueId("test")
                .proposingTeamId("T1")
                .offeringAthletes(List.of("A1"))
                .acceptingTeamId("T2")
                .receivingAthletes(List.of("A2", "A3"))
                .build();

        try {
            tradeValidator.validate(request);
            Assert.fail();
        } catch (UnequalTrade ignored) { }
    }

// This cant happen right now because the trades must be equal in numbers
//    @Test
//    public void testTeamFull() throws ApplicationException {
//        TradeProposal request = TradeProposal.builder()
//                .leagueId("test")
//                .initiatingTeamId("T1")
//                .offeringAthletes(List.of("A1"))
//                .proposedTeamId("T2")
//                .receivingAthletes(List.of("A2", "A3"))
//                .build();
//
//        try {
//            tradeValidator.validate(request);
//            Assert.fail();
//        } catch (TeamFull ignored) { }
//    }

    @Test
    public void testAthleteNotOnTeam() throws ApplicationException {
        TradeProposal request = TradeProposal.builder()
                .leagueId("test")
                .proposingTeamId("T1")
                .offeringAthletes(List.of("A1"))
                .acceptingTeamId("T2")
                .receivingAthletes(List.of("imaginaryAthlete"))
                .build();

        try {
            tradeValidator.validate(request);
            Assert.fail();
        } catch (AthleteNotOnTeam ignored) { }
    }

    @Test
    public void testSelfTrade() throws ApplicationException {
        TradeProposal request = TradeProposal.builder()
                .leagueId("test")
                .proposingTeamId("T1")
                .acceptingTeamId("T1")
                .offeringAthletes(List.of("A1"))
                .receivingAthletes(List.of("A2"))
                .build();

        try {
            tradeValidator.validate(request);
            Assert.fail();
        } catch (CannotTradeWithSelf ignored) { }
    }

    @Test
    public void testTeamNotInLeague() throws ApplicationException {
        TradeProposal request = TradeProposal.builder()
                .leagueId("test")
                .acceptingTeamId("T3")
                .proposingTeamId("T1")
                .offeringAthletes(List.of("A1"))
                .receivingAthletes(List.of("A2"))
                .build();

        try {
            tradeValidator.validate(request);
            Assert.fail();
        } catch (TeamNotInLeague ignored) { }
    }

    @Test
    public void testTradeEmpty() throws ApplicationException {
        TradeProposal request = TradeProposal.builder()
                .leagueId("test")
                .acceptingTeamId("T2")
                .proposingTeamId("T1")
                .offeringAthletes(List.of("A1"))
                .receivingAthletes(new ArrayList<>())
                .build();

        try {
            tradeValidator.validate(request);
            Assert.fail();
        } catch (CannotMakeEmptyTrade ignored) { }
    }

    @Test
    public void testTeamNull() throws ApplicationException {
        TradeProposal request = TradeProposal.builder()
                .leagueId("test")
                .acceptingTeamId("imaginaryTeam")
                .proposingTeamId("T1")
                .offeringAthletes(List.of("A1"))
                .receivingAthletes(List.of("A2"))
                .build();

        try {
            tradeValidator.validate(request);
            Assert.fail();
        } catch (TeamNotExists ignored) { }
    }
}