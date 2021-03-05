package usecase.meets;

import domain.entity.League;
import domain.repository.MeetRepository;
import mock.LeagueBank;
import mock.MeetBank;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usecase.dto.MeetDTO;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;

public class GetMeetsTest {


    private League league = LeagueBank.postDraftLeague;
    private MeetRepository meetRepository;
    private GetMeets meetFetcher;

    @BeforeMethod
    public void setUp() {
        meetRepository = mock(MeetRepository.class);
        when(meetRepository.getMeetsInLeague(league.id)).then(invocation -> List.of(MeetBank.testMeet1, MeetBank.testMeet2));
        meetFetcher = new GetMeets(meetRepository);
    }

    @Test
    public void testGetMeetsInLeague() {
        List<MeetDTO> meets = meetFetcher.getMeetsInLeague(league.id);

        assertEquals(meets.size(), 2);
        assertEquals(meets.get(0).id, MeetBank.testMeet1.id);
        assertEquals(meets.get(1).id, MeetBank.testMeet2.id);
    }

    @Test
    public void testNoMeetsInLeague() {
        when(meetRepository.getMeetsInLeague(league.id)).then(invocation -> List.of(MeetBank.testMeet1, MeetBank.testMeet2, MeetBank.testMeet4));

        List<MeetDTO> meets = meetFetcher.getMeetsInLeague("imaginaryLeague");

        assertEquals(meets.size(), 0);
    }

}