package usecase.team;

import fantasyapp.repository.Events;
import com.google.inject.Inject;
import domain.exception.ApplicationException;
import domain.exception.AthleteNotOnTeam;
import domain.exception.EventCategoryFull;
import domain.exception.InvalidEvent;
import domain.repository.RosterRepository;
import util.DistributedLock;

public class EditRoster {

    private RosterRepository rosterRepository;
    private DistributedLock lock;

    @Inject
    public EditRoster(RosterRepository rosterRepository,DistributedLock lock) {
        this.rosterRepository = rosterRepository;
        this.lock = lock;
    }

    public void edit(RosterEditRequest request) throws ApplicationException {
        lock.lockAndRun(DistributedLock.ROSTER, request.leagueId, () -> {
            validateCanEdit(request);
            validateEvent(request.eventId, request.teamId);
            rosterRepository.setAthleteEvent(request.athleteId, request.eventId, request.teamId);
        });
    }

    private void validateCanEdit(RosterEditRequest request) throws AthleteNotOnTeam {
        if (!rosterRepository.isAthleteOnRoster(request.athleteId, request.teamId)) {
            throw new AthleteNotOnTeam();
        }
    }

    private void validateEvent(String eventId, String teamId) throws EventCategoryFull, InvalidEvent {
        String eventCategory = Events.eventToCategory(eventId);
        if (eventCategory == null) throw new InvalidEvent();
        if (eventCategory.equals(Events.BENCH_ID)) return;

        int athletesInCategory = rosterRepository.countAthletesInEventCategory(eventCategory, teamId);
        if (athletesInCategory >= 2) {
            throw new EventCategoryFull();
        }
    }

}
