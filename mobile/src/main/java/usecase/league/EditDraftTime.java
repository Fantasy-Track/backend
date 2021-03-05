package usecase.league;

import com.google.inject.Inject;
import domain.entity.Meet;
import domain.exception.ApplicationException;
import domain.repository.EditLeagueRepository;
import domain.repository.MeetRepository;

import java.time.Instant;
import java.util.List;

public class EditDraftTime {

    private EditLeagueRepository leagueRepository;
    private DraftTimeValidator timeValidator;
    private MeetRepository meetRepository;
    private EditDraftTimeValidator editDraftValidator;

    @Inject
    public EditDraftTime(EditLeagueRepository leagueRepository, DraftTimeValidator timeValidator, MeetRepository meetRepository, EditDraftTimeValidator editDraftValidator) {
        this.leagueRepository = leagueRepository;
        this.timeValidator = timeValidator;
        this.meetRepository = meetRepository;
        this.editDraftValidator = editDraftValidator;
    }

    public void editDraftTime(String leagueId, Instant draftTime) throws ApplicationException {
        editDraftValidator.validateCanEditDraftTime(leagueId);
        timeValidator.validateDraftTime(draftTime);
        leagueRepository.setDraftTime(leagueId, draftTime);
        disableMeetsBeforeDraft(leagueId, draftTime);
    }

    private void disableMeetsBeforeDraft(String leagueId, Instant draftTime) {
        List<Meet> meets = meetRepository.getEnabledMeetsInLeague(leagueId);
        for (Meet meet : meets) {
            if (!meet.date.isAfter(draftTime)) meetRepository.setMeetEnabled(meet.id, false);
        }
    }

}
