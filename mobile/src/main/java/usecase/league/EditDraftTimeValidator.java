package usecase.league;

import com.google.inject.Inject;
import domain.entity.League;
import domain.entity.LeagueStatus;
import domain.entity.ScheduledDraft;
import domain.exception.AlreadyDrafted;
import domain.exception.ApplicationException;
import domain.exception.CannotDoActionNow;
import domain.repository.LeagueRepository;
import domain.repository.ScheduledDraftRepository;
import util.DistributedLock;

public class EditDraftTimeValidator {

    private LeagueRepository leagueRepository;
    private ScheduledDraftRepository draftRepository;
    private DistributedLock lock;

    @Inject
    public EditDraftTimeValidator(LeagueRepository leagueRepository, ScheduledDraftRepository draftRepository, DistributedLock lock) {
        this.leagueRepository = leagueRepository;
        this.draftRepository = draftRepository;
        this.lock = lock;
    }

    public void validateCanEditDraftTime(String leagueId) throws ApplicationException {
        League league = leagueRepository.getLeagueById(leagueId);
        if (league.status != LeagueStatus.PRE_DRAFT) {
            throw new CannotDoActionNow();
        }
        lock.lockAndRun(DistributedLock.SCHED_DRAFTS, DistributedLock.ALL_LEAGUES, () -> {
            ScheduledDraft draft = draftRepository.getDraftForLeague(leagueId);
            if (draft == null) throw new AlreadyDrafted();
        });
    }

}
