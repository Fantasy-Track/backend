package usecase;

import fantasyapp.draftInstance.LeagueId;
import com.google.inject.Inject;
import domain.entity.League;
import domain.entity.LeagueStatus;
import domain.exception.AlreadyDrafted;
import domain.exception.ApplicationException;
import domain.repository.LeagueRepository;

public class DraftSetupTeardown {

    private SetupRandomPickOrder setupPickOrder;
    private LeagueRepository leagueRepository;

    private String leagueId;

    @Inject
    public DraftSetupTeardown(SetupRandomPickOrder setupPickOrder, LeagueRepository leagueRepository, @LeagueId String leagueId) {
        this.setupPickOrder = setupPickOrder;
        this.leagueRepository = leagueRepository;
        this.leagueId = leagueId;
    }

    public void setup() throws ApplicationException {
        League league = leagueRepository.getLeagueById(leagueId);
        if (league.status != LeagueStatus.PRE_DRAFT) throw new AlreadyDrafted();

        leagueRepository.setStatus(leagueId, LeagueStatus.DRAFTING);
        setupPickOrder.setup();
    }

    public void teardown() {
        leagueRepository.setStatus(leagueId, LeagueStatus.POST_DRAFT); // should happen last
    }

}
