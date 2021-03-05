package usecase.league;

import domain.exception.ApplicationException;
import domain.repository.EditLeagueRepository;
import domain.repository.MeetRepository;
import mock.MeetBank;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.Mockito.*;

public class EditDraftTimeTest {

    @Test
    public void testEditDraftTime() throws ApplicationException {
        EditLeagueRepository leagueRepository = mock(EditLeagueRepository.class);
        DraftTimeValidator timeValidator = mock(DraftTimeValidator.class);
        MeetRepository meetRepository = mock(MeetRepository.class);
        EditDraftTimeValidator editValidator = mock(EditDraftTimeValidator.class);
        EditDraftTime editDraftTime = new EditDraftTime(leagueRepository, timeValidator, meetRepository, editValidator);

        when(meetRepository.getEnabledMeetsInLeague("test")).then(invocation -> List.of(MeetBank.testMeet2, MeetBank.testMeet4));

        Instant time = Instant.now().plus(10, ChronoUnit.MINUTES);

        editDraftTime.editDraftTime("test", time);

        verify(editValidator).validateCanEditDraftTime("test");
        verify(timeValidator).validateDraftTime(time);
        verify(leagueRepository).setDraftTime("test", time);

        verify(meetRepository, never()).setMeetEnabled(MeetBank.testMeet2.id, false);
        verify(meetRepository).setMeetEnabled(MeetBank.testMeet4.id, false);
    }

}