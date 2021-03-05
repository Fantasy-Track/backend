package usecase.team;

import fantasyapp.repository.Events;
import domain.exception.ApplicationException;
import domain.exception.AthleteNotOnTeam;
import domain.exception.EventCategoryFull;
import domain.exception.InvalidEvent;
import domain.repository.RosterRepository;
import mock.MockDistributedLock;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

public class EditRosterTest {

    private RosterRepository rosterRepository;
    private EditRoster rosterEditor;

    @BeforeMethod
    public void setUp() {
        rosterRepository = mock(RosterRepository.class);
        rosterEditor = new EditRoster(rosterRepository, new MockDistributedLock());

        Mockito.when(rosterRepository.isAthleteOnRoster("A0", "T1")).then(invocation -> false);
        Mockito.when(rosterRepository.isAthleteOnRoster("A1", "T1")).then(invocation -> true);
        Mockito.when(rosterRepository.countAthletesInEventCategory(Events.BENCH_ID, "T1")).then(invocation -> 2);
        Mockito.when(rosterRepository.countAthletesInEventCategory(Events.FIELD_ID, "T1")).then(invocation -> 2);
    }

    @Test
    public void testEditSuccess() throws Exception {
        RosterEditRequest request = RosterEditRequest.builder()
                .leagueId("test")
                .athleteId("A1")
                .teamId("T1")
                .eventId("52")
                .build();
        rosterEditor.edit(request);
        Mockito.verify(rosterRepository, Mockito.times(1)).setAthleteEvent("A1", "52", "T1");
    }

    @Test
    public void testAthleteNotOnTeam() throws ApplicationException {
        RosterEditRequest request = RosterEditRequest.builder()
                .athleteId("A0")
                .teamId("T1")
                .leagueId("test")
                .eventId("9")
                .build();
        try {
            rosterEditor.edit(request);
            Assert.fail();
        } catch (AthleteNotOnTeam ignored) { }
    }

    @Test
    public void testBenchNotFull() throws ApplicationException {
        RosterEditRequest request = RosterEditRequest.builder()
                .athleteId("A1")
                .teamId("T1")
                .leagueId("test")
                .eventId("-5")
                .build();
        rosterEditor.edit(request);
    }

    @Test
    public void testInvalidEvent() throws ApplicationException {
        RosterEditRequest request = RosterEditRequest.builder()
                .athleteId("A1")
                .teamId("T1")
                .leagueId("test")
                .eventId("1000")
                .build();
        try {
            rosterEditor.edit(request);
            Assert.fail();
        } catch (InvalidEvent ignored) { }
    }


    @Test
    public void testEventCategoryFull() throws ApplicationException {
        RosterEditRequest request = RosterEditRequest.builder()
                .athleteId("A1")
                .teamId("T1")
                .leagueId("test")
                .eventId("9")
                .build();

        try {
            rosterEditor.edit(request);
            Assert.fail();
        } catch (EventCategoryFull ignored) { }
    }



}