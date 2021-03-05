package usecase;

import domain.entity.Team;
import domain.repository.MeetRepository;
import domain.repository.ResultRepository;
import domain.repository.TeamRepository;
import mock.MeetBank;
import org.mockito.Mockito;
import org.testng.annotations.Test;
import usecase.fantasyPoints.PointsUpdater;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PointsUpdaterTest {

    @Test
    public void testUpdatePointsForLeague() {
        ResultRepository resultRepository = Mockito.mock(ResultRepository.class);
        TeamRepository teamRepository = Mockito.mock(TeamRepository.class);
        MeetRepository meetRepository = Mockito.mock(MeetRepository.class);

        when(teamRepository.getTeamsInLeague("test")).then(invocation -> Collections.singletonList(Team.builder().id("T1").fantasyPoints(10).build()));
        when(resultRepository.aggregatePointsAtMeets("T1", List.of(MeetBank.testMeet1.id))).then(invocation -> 50.0);
        when(meetRepository.getEnabledMeetsInLeague("test")).then(invocation -> List.of(MeetBank.testMeet1));

        PointsUpdater updater = new PointsUpdater(resultRepository, teamRepository, meetRepository);
        updater.recalculatePointsForLeague("test");

        verify(teamRepository).updateFantasyPoints("T1", 50.0);
    }
}