package usecase.team;

import domain.entity.Meet;
import domain.repository.ContractRepository;
import domain.repository.MeetRepository;
import mock.ContractBank;
import mock.MeetBank;
import mock.MockAthleteRepository;
import mock.MockNameRepo;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usecase.dto.AthleteOwnershipDTO;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class GetTeamRosterTest {

    Meet meet = MeetBank.testMeet1;
    GetTeamRoster teamRoster;

    @BeforeMethod
    public void setUp() {
        ContractRepository contractRepository = mock(ContractRepository.class);
        when(contractRepository.getTeamContracts("T1")).then(invocation -> ContractBank.team1Contracts);

        MeetRepository meetRepository = mock(MeetRepository.class);
        when(meetRepository.getMeetById(meet.id)).then(invocation -> meet);

        teamRoster = new GetTeamRoster(contractRepository, new MockAthleteRepository(), new MockNameRepo(), meetRepository);
    }

    @Test
    public void testGetRoster() {
        List<AthleteOwnershipDTO> roster = teamRoster.getRoster("T1", null);

        assertEquals(roster.size(), 2);
        assertEquals(roster.get(0).id, "A1");
        assertEquals(roster.get(1).id, "A2");
        assertEquals(roster.get(1).teamId, "T1");
        assertEquals(roster.get(1).teamId, "T1");
    }

    @Test
    public void testNoRoster() {
        List<AthleteOwnershipDTO> roster = teamRoster.getRoster("imaginaryTeam", null);

        assertEquals(roster.size(), 0);
    }

    @Test
    public void testGetMeetRoster() {
        List<AthleteOwnershipDTO> roster = teamRoster.getRoster("T2", meet.id);

        assertEquals(roster.size(), 1);
        assertEquals(roster.get(0).id, "A3");
        assertEquals(roster.get(0).teamId, "T2");
    }

}