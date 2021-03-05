package usecase.login;

import domain.entity.Owner;
import domain.exception.AccountNotFound;
import domain.repository.OwnerRepository;
import mock.MockOwnerRepo;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest {

    @Test
    public void testLoginSuccess() {
        OwnerRepository ownerRepository = new MockOwnerRepo();
        ownerRepository.addOwner(Owner.builder().id("owner").name("ownerName").build());
        Login login = new Login(ownerRepository);

        try {
            login.login("owner");
        } catch (AccountNotFound e) {
            Assert.fail();
        }
    }

    @Test
    public void testLoginFail() {
        OwnerRepository ownerRepository = new MockOwnerRepo();
        Login login = new Login(ownerRepository);

        try {
            login.login("imaginaryOwner");
            Assert.fail();
        } catch (AccountNotFound e) { }
    }
}