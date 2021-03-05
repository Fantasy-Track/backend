package usecase.leagueCreation;

import domain.entity.Athlete;
import domain.repository.AthleteUpdateRepository;
import mock.AthleteBank;
import org.testng.annotations.Test;
import usecase.pageExtraction.AthleteData;
import usecase.pageExtraction.AthleteListExtractor;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class IndexSchoolAthletesTest {

    private AthleteData athleteDataFromEntity(Athlete athlete) {
        AthleteData data = new AthleteData();
        data.setId(athlete.id);
        data.setName(athlete.name);
        data.setGender(athlete.gender);
        return data;
    }

    @Test
    public void testIndexAndGetAthletesInSchool() throws Exception {
        AthleteListExtractor extractor = mock(AthleteListExtractor.class);
        AthleteUpdater athleteUpdater = mock(AthleteUpdater.class);
        AthleteUpdateRepository athleteRepository = mock(AthleteUpdateRepository.class);

        when(athleteRepository.getAthletesByIds(any())).then(invocation -> List.of(Athlete.builder().id("A2").build(), Athlete.builder().id("A3").build()));
        when(extractor.extractAthletes("904")).then(invocation -> List.of(
                athleteDataFromEntity(AthleteBank.athlete1),
                athleteDataFromEntity(AthleteBank.athlete2),
                athleteDataFromEntity(AthleteBank.athlete3),
                athleteDataFromEntity(AthleteBank.athlete4)));

        IndexSchoolAthletes athletes = new IndexSchoolAthletes(extractor, athleteUpdater, athleteRepository);

        List<String> ids = athletes.indexAndGetAthletesInSchool("904");

        assertEquals(ids, List.of("A1", "A2", "A3", "A4"));
        verify(athleteUpdater, times(2)).uploadAthlete(any(AthleteData.class));
    }
}