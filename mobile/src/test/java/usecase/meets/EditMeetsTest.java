package usecase.meets;

import domain.entity.League;
import domain.exception.MeetBeforeDraft;
import domain.exception.MeetNotExists;
import domain.exception.UnauthorizedException;
import domain.repository.LeagueRepository;
import domain.repository.MeetRepository;
import mock.LeagueBank;
import mock.MeetBank;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usecase.league.LeaguePointsUpdater;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class EditMeetsTest {

    private League league = LeagueBank.predraftLeague;

    private LeaguePointsUpdater pointsUpdater;
    private MeetRepository meetRepository;

    private EditMeets editMeets;

    @BeforeMethod
    public void setUp() {
        LeagueRepository leagueRepository = mock(LeagueRepository.class);
        meetRepository = mock(MeetRepository.class);
        pointsUpdater = mock(LeaguePointsUpdater.class);

        when(leagueRepository.getLeagueById(league.id)).then(invocation -> league);
        when(meetRepository.getMeetsInLeague(league.id)).then(invocation -> List.of(MeetBank.testMeet1, MeetBank.testMeet2, MeetBank.testMeet5));

        editMeets = new EditMeets(meetRepository, leagueRepository, pointsUpdater);
    }


    @Test
    public void testEnableAndDisableNotExistMeets() throws Exception {
        List<String> enable = List.of(MeetBank.testMeet4.id);
        List<String> disable = List.of(MeetBank.testMeet3.id);

        try {
            editMeets.editEnabledMeets(EditMeetsRequest.builder()
                    .enableMeetIds(enable)
                    .disableMeetIds(disable)
                    .teamId(league.owningTeam)
                    .leagueId(league.id).build());
            Assert.fail();
        } catch (MeetNotExists ignored) {}
    }

    @Test
    public void testEnableAndDisableNotDrafted() throws Exception {
        List<String> enable = List.of(MeetBank.testMeet5.id);

        try {
            editMeets.editEnabledMeets(EditMeetsRequest.builder()
                    .enableMeetIds(enable)
                    .disableMeetIds(new ArrayList<>())
                    .teamId(league.owningTeam)
                    .leagueId(league.id).build());
            Assert.fail();
        } catch (MeetBeforeDraft ignored) {}
    }

    @Test
    public void testEnableAndDisableMeets() throws Exception {
        List<String> enable = List.of(MeetBank.testMeet1.id);
        List<String> disable = List.of(MeetBank.testMeet2.id);

        editMeets.editEnabledMeets(EditMeetsRequest.builder()
                .enableMeetIds(enable)
                .disableMeetIds(disable)
                .teamId(league.owningTeam)
                .leagueId(league.id).build());

        verify(meetRepository).setMeetEnabled(MeetBank.testMeet1.id, true);
        verify(meetRepository).setMeetEnabled(MeetBank.testMeet2.id, false);
        verify(pointsUpdater).updatePoints(league.id);
    }

    @Test
    public void testNotOwner() throws Exception {
        try {
            editMeets.editEnabledMeets(EditMeetsRequest.builder()
                    .enableMeetIds(new ArrayList<>())
                    .disableMeetIds(new ArrayList<>())
                    .teamId("notTheOwner")
                    .leagueId(league.id).build());
            Assert.fail();
        } catch (UnauthorizedException ignored) {}
    }

}