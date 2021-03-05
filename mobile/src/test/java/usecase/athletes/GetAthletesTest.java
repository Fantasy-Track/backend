package usecase.athletes;

import domain.entity.Athlete;
import domain.entity.League;
import domain.exception.AthleteNotExists;
import domain.exception.LeagueNotExists;
import domain.repository.LeagueRepository;
import mock.MockAthleteRepository;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usecase.dto.AthleteDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class GetAthletesTest {

    private GetAthletes athleteFetcher;

    @BeforeMethod
    public void setUp() {
        LeagueRepository leagueRepository = mock(LeagueRepository.class);
        when(leagueRepository.getLeagueById("test")).then(invocation -> League.builder()
                .id("test")
                .athletes(List.of("A1", "A2"))
                .build());
        athleteFetcher = new GetAthletes(leagueRepository, new MockAthleteRepository());
    }

    @Test
    public void testGetAthletesInNonexistentLeague() {
        try {
            athleteFetcher.getAthletesInLeague("imaginaryLeague");
            Assert.fail();
        } catch (LeagueNotExists leagueNotExists) { }
    }


    @Test
    public void testGetAthletesInLeague() throws LeagueNotExists {
        List<AthleteDTO> athletes = athleteFetcher.getAthletesInLeague("test");

        assertEquals(athletes.size(), 2);
        checkDTOAgainstAthlete(athletes.get(0), MockAthleteRepository.athlete1);
        checkDTOAgainstAthlete(athletes.get(1), MockAthleteRepository.athlete2);
    }

    @Test
    public void testGetOneNonexistentAthlete() {
        try {
            athleteFetcher.getOneAthlete("imaginaryAthlete");
            Assert.fail();
        } catch (AthleteNotExists athleteNotExists) { }
    }


    @Test
    public void testGetOneAthlete() throws AthleteNotExists {
        AthleteDTO dto = athleteFetcher.getOneAthlete("A1");
        checkDTOAgainstAthlete(dto, MockAthleteRepository.athlete1);
    }

    private void checkDTOAgainstAthlete(AthleteDTO dto, Athlete athlete) {
        assertEquals(dto.id, athlete.id);
        assertEquals(dto.name, athlete.name);
        assertEquals(dto.gender, athlete.gender);
        assertEquals(dto.primaryEvents, athlete.primaryEvents);
    }

    @Test
    public void testAthleteIdsInLeague() throws LeagueNotExists {
        List<String> athletes = athleteFetcher.getAthleteIdsInLeague("test");
        assertEquals(athletes, List.of("A1", "A2"));
    }
}