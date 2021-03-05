package domain.repository;

import domain.entity.Contract;
import domain.entity.Meet;

import java.time.Instant;
import java.util.List;

public interface MeetRepository {

    List<Meet> getMeetsInLeague(String leagueId);

    List<Meet> getAllLockedMeetsBefore(Instant time);

    void flagMeetAsHasResults(String meetId);

    void addOrReplaceMeet(Meet meet);

    List<Meet> getEnabledUnlockedMeetsBetween(Instant start, Instant end);

    void saveContractsForMeet(String meetId, List<Contract> contracts);

    void setMeetEnabled(String meetId, boolean enabled);

    List<Meet> getEnabledMeetsInLeague(String leagueId);

    Meet getMeetById(String meetId);

    void flagMeetAsLocked(String meetId);
}
