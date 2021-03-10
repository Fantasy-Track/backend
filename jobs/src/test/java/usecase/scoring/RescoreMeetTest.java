package usecase.scoring;

import domain.entity.Meet;
import domain.repository.MeetRepository;
import domain.repository.ResultRepository;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usecase.fantasyPoints.PointsUpdater;
import usecase.meetProcessing.RescoreMeet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RescoreMeetTest {

    private RescoreMeet rescoreMeet;

    private MeetRepository meetRepository;
    private ResultRepository resultRepository;
    private PointsUpdater pointsUpdater;

    @BeforeMethod
    public void setUp() {
        meetRepository = mock(MeetRepository.class);
        resultRepository = mock(ResultRepository.class);
        pointsUpdater = mock(PointsUpdater.class);

        rescoreMeet = new RescoreMeet(meetRepository, resultRepository, pointsUpdater);

        when(meetRepository.getMeetById("meetId")).thenReturn(Meet.builder()
            .id("meetId")
            .leagueId("leagueId")
            .hasResults(true)
            .locked(true)
            .build());
    }

    @Test
    public void testRemoveResults() {
        rescoreMeet.rescoreMeet("meetId");
        verify(meetRepository).setMeetHasResults("meetId", false);
        verify(pointsUpdater).recalculatePointsForLeague("leagueId");
        verify(resultRepository).removeMeetResults("meetId");
    }

}
