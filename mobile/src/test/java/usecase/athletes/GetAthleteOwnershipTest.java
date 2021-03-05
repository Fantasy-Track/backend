package usecase.athletes;

import domain.entity.Athlete;
import domain.entity.Contract;
import domain.repository.AthleteRepository;
import domain.repository.ContractRepository;
import mock.MockNameRepo;
import org.testng.annotations.Test;
import usecase.dto.AthleteOwnershipDTO;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class GetAthleteOwnershipTest {

    @Test
    public void testGetAthletesOwnership() {
        AthleteRepository athleteRepository = mock(AthleteRepository.class);
        when(athleteRepository.getAthletesByIds(List.of("A1", "A2"))).then(invocation -> List.of(Athlete.builder()
                .id("A1")
                .build(),
                Athlete.builder()
                .id("A2")
                .build()));

        ContractRepository contractRepository = mock(ContractRepository.class);
        when(contractRepository.getLeagueContracts("test")).then(invocation -> List.of(Contract.builder().athleteId("A1").teamId("T1").eventId("1").build()));

        GetAthleteOwnership ownership = new GetAthleteOwnership(athleteRepository, contractRepository, new MockNameRepo());

        List<AthleteOwnershipDTO> dtos = ownership.getAthleteOwnership(List.of("A1", "A2"), "test");

        AthleteOwnershipDTO dto1 = dtos.get(0);
        assertEquals(dto1.id, "A1");
        assertEquals(dto1.teamId, "T1");
        assertTrue(dto1.owned);
        assertEquals(dto1.positionId, "1");

        AthleteOwnershipDTO dto2 = dtos.get(1);
        assertEquals(dto2.id, "A2");
        assertNull(dto2.teamId);
        assertNull(dto2.positionId);
        assertFalse(dto2.owned);
    }

}