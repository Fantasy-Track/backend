package usecase.meetLocking;

import com.google.inject.Inject;
import domain.entity.Contract;
import domain.entity.League;
import domain.entity.LeagueStatus;
import domain.entity.Meet;
import domain.repository.ContractRepository;
import domain.repository.LeagueRepository;
import domain.repository.MeetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class LockMeets {

    private Logger logger = LoggerFactory.getLogger(LockMeets.class);

    private MeetRepository meetRepository;
    private ContractRepository contractRepository;
    private LeagueRepository leagueRepository;

    @Inject
    public LockMeets(MeetRepository meetRepository, ContractRepository contractRepository, LeagueRepository leagueRepository) {
        this.meetRepository = meetRepository;
        this.contractRepository = contractRepository;
        this.leagueRepository = leagueRepository;
    }

    public void lockMeetsToday() {
        logger.info("Locking leagues with meets today");

        ZonedDateTime pstTime = Instant.now().atZone(ZoneId.of("America/Los_Angeles"));
        Instant today = pstTime.toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant tomorrow = today.plus(1, ChronoUnit.DAYS);
        List<Meet> meets = meetRepository.getEnabledUnlockedMeetsBetween(today, tomorrow);

        for (Meet meet : meets) {
            logger.info("Locking meet: " + meet);
            try { // just in case there is an error, the other meets will be locked
                lockMeet(meet);
            } catch (Exception e) {
                logger.error("Error when locking meet: " + meet);
            }
        }
    }

    private void lockMeet(Meet meet) {
        if (!hasLeagueDrafted(meet.leagueId)) {
            meetRepository.setMeetEnabled(meet.id, false);
            return;
        }
        saveContractsForMeet(meet);
        meetRepository.flagMeetAsLocked(meet.id); // must be last
    }

    public boolean hasLeagueDrafted(String leagueId) {
        League league = leagueRepository.getLeagueById(leagueId);
        if (league == null) return false;
        return league.status == LeagueStatus.POST_DRAFT;
    }

    private void saveContractsForMeet(Meet meet) {
        List<Contract> contracts = contractRepository.getLeagueContracts(meet.leagueId);
        meetRepository.saveContractsForMeet(meet.id, contracts);
        logger.info("Saved contracts for meet. Count: " + contracts.size());
    }

}
