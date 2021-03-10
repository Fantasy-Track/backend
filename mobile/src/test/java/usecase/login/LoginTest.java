package usecase.login;

import domain.entity.Owner;
import domain.exception.AccountNotFound;
import domain.repository.OwnerRepository;
import domain.repository.TeamRepository;
import mock.MockOwnerRepo;
import mock.TeamBank;
import org.testng.Assert;
import org.testng.annotations.Test;
import usecase.notification.NotificationFactory;
import usecase.notification.NotificationHandler;

import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginTest {

    @Test
    public void testLoginSuccess() {
        OwnerRepository ownerRepository = new MockOwnerRepo();
        TeamRepository teamRepository = mock(TeamRepository.class);
        ownerRepository.addOwner(Owner.builder().id("owner").name("ownerName").build());
        NotificationFactory.handler = mock(NotificationHandler.class);
        Login login = new Login(ownerRepository, teamRepository);

        when(teamRepository.getOwnedTeams("owner")).thenReturn(List.of(TeamBank.TEAM1));

        try {
            login.login("owner", "deviceToken");
            verify(NotificationFactory.handler, only()).registerDeviceForNotifications(
                argThat(arg -> arg.equals("deviceToken")),
                argThat(arg -> arg.containsAll(List.of(TeamBank.TEAM1.id, TeamBank.TEAM1.leagueId))));
        } catch (AccountNotFound e) {
            Assert.fail();
        }
    }

    @Test
    public void testLoginFail() {
        OwnerRepository ownerRepository = new MockOwnerRepo();
        TeamRepository teamRepository = mock(TeamRepository.class);
        Login login = new Login(ownerRepository, teamRepository);

        try {
            login.login("imaginaryOwner", "deviceToken");
            Assert.fail();
        } catch (AccountNotFound e) {
        }
    }
}