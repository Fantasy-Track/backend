package usecase.meetLocking;

import domain.entity.League;
import domain.entity.LeagueStatus;
import domain.entity.Meet;
import domain.repository.ContractRepository;
import domain.repository.LeagueRepository;
import domain.repository.MeetRepository;
import mock.ContractBank;
import mock.MeetBank;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.*;

public class LockMeetsTest {

    private Meet meet = MeetBank.testMeet2;
    private LeagueRepository leagueRepository;
    private MeetRepository meetRepository;

    private LockMeets lockMeets;

    @BeforeMethod
    public void setUp() {

        ContractRepository contractRepository = mock(ContractRepository.class);
        meetRepository = mock(MeetRepository.class);
        leagueRepository = mock(LeagueRepository.class);

        when(leagueRepository.getLeagueById(meet.leagueId)).then(invocation -> League.builder().status(LeagueStatus.POST_DRAFT).build());
        when(contractRepository.getLeagueContracts(meet.leagueId)).then(invocation -> ContractBank.leagueContracts);
        when(meetRepository.getEnabledUnlockedMeetsBetween(any(), any())).then(invocation -> List.of(meet));

        lockMeets = new LockMeets(meetRepository, contractRepository, leagueRepository);
    }

    @Test
    public void testLockMeets() {
        lockMeets.lockMeetsToday();
        verify(meetRepository).saveContractsForMeet("MT2", ContractBank.leagueContracts);
        verify(meetRepository).flagMeetAsLocked("MT2");
        verify(meetRepository, never()).setMeetEnabled(meet.id, false);
    }

    @Test
    public void testLeagueNotDrafted() {
        when(leagueRepository.getLeagueById(meet.leagueId)).then(invocation -> League.builder().status(LeagueStatus.DRAFTING).build());

        lockMeets.lockMeetsToday();
        verify(meetRepository, never()).saveContractsForMeet("MT2", ContractBank.leagueContracts);
        verify(meetRepository, never()).flagMeetAsLocked("MT2");
        verify(meetRepository).setMeetEnabled(meet.id, false);
    }


}