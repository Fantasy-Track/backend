package usecase.createOwner;

import domain.exception.ApplicationException;
import domain.exception.NameTaken;
import domain.exception.NameTooLong;
import domain.exception.NameTooShort;
import mock.MockOwnerRepo;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class OwnerNameValidatorTest {

    private OwnerNameValidator validator;

    @BeforeMethod
    public void setUp() {
        validator = new OwnerNameValidator(new MockOwnerRepo());
    }

    @Test
    public void testValidName() {
        try {
            validator.validateName("owner");
        } catch (Throwable e) { Assert.fail(); }
    }

    @Test
    public void testNameTooShort() throws NameTooLong, NameTaken {
        try {
            validator.validateName("ab");
            Assert.fail();
        } catch (NameTooShort nameTooShort) { }
    }

    @Test
    public void testNameTooLong() throws NameTooShort, NameTaken {
        try {
            validator.validateName("abcdefghijklmnol");
            Assert.fail();
        } catch (NameTooLong nameTooShort) { }
    }


    @Test
    public void testNameTaken() throws ApplicationException {
        try {
            validator.validateName("takenName");
            Assert.fail();
        } catch (NameTaken e) { }
    }

}