package usecase.trading;

import domain.entity.TradeProposal;
import domain.entity.TradeStatus;
import domain.repository.NameRepository;
import domain.repository.TradeRepository;
import org.testng.annotations.Test;
import usecase.dto.TradeProposalDTO;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class TradeProposalFetcherTest {

    @Test
    public void testGetTradesWithTeam() {
        Instant now = Instant.now();
        NameRepository nameRepository = mock(NameRepository.class);
        when(nameRepository.getAthleteName(anyString())).then(invocation -> invocation.getArgument(0) + "N");
        when(nameRepository.getTeamName(anyString())).then(invocation -> invocation.getArgument(0) + "N");

        TradeRepository tradeRepository = mock(TradeRepository.class);
        when(tradeRepository.getTradesWithTeam("T1")).then(invocation -> List.of(TradeProposal.builder()
                .status(TradeStatus.ACCEPTED_WAITING)
                .id("TR1")
                .proposingTeamId("T1")
                .acceptingTeamId("T2")
                .offeringAthletes(List.of("A1"))
                .receivingAthletes(List.of("A2"))
                .leagueId("test")
                .date(now)
                .build()));

        TradeProposalFetcher fetcher = new TradeProposalFetcher(tradeRepository, nameRepository);
        List<TradeProposalDTO> dtos = fetcher.getTradesWithTeam("T1");

        assertEquals(dtos.size(), 1);

        TradeProposalDTO dto = dtos.get(0);
        assertEquals(dto.id, "TR1");
        assertEquals(dto.status, TradeStatus.ACCEPTED_WAITING);
        assertEquals(dto.proposingTeamId, "T1");
        assertEquals(dto.acceptingTeamId, "T2");
        assertEquals(dto.proposingTeamName, "T1N");
        assertEquals(dto.acceptingTeamName, "T2N");
        assertEquals(dto.offeringAthletes.get("A1"), "A1N");
        assertEquals(dto.receivingAthletes.get("A2"), "A2N");
        assertEquals(dto.offeringAthletes.size(), 1);
        assertEquals(dto.receivingAthletes.size(), 1);
        assertEquals(dto.date, now);
    }
}