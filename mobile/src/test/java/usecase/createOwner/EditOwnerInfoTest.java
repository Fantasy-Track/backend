package usecase.createOwner;

import domain.entity.Owner;
import domain.exception.NameTaken;
import domain.exception.NameTooLong;
import domain.exception.NameTooShort;
import domain.exception.OwnerNotExists;
import domain.repository.NameRepository;
import domain.repository.OwnerRepository;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class EditOwnerInfoTest {

    private EditOwnerInfo editOwnerInfo;

    @Mock
    private NameRepository nameRepository;
    @Mock private OwnerRepository ownerRepository;
    @Mock private OwnerNameValidator nameValidator;

    private Owner owner = Owner.builder().id("O1").name("Owner Name").build();

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        editOwnerInfo = new EditOwnerInfo(nameRepository, ownerRepository, nameValidator);
    }

    @Test
    public void testEditOwnerName() throws NameTooShort, NameTooLong, NameTaken, OwnerNotExists {
        when(ownerRepository.getOwnerById(owner.id)).then(invocation -> owner);

        Owner returnedOwner = editOwnerInfo.editOwnerName(owner.id, "New Name");
        verify(nameValidator).validateName("New Name");
        verify(nameRepository).updateOwnerName("O1", "New Name");
        assertEquals(returnedOwner, owner);
    }

    @Test
    public void testGetOwnerByIdNull() throws OwnerNotExists {
        try {
            Owner returnedOwner = editOwnerInfo.getOwnerById(owner.id);
            Assert.fail();
        } catch (OwnerNotExists ignored) {
        }
    }

    @Test
    public void testGetOwnerById() throws OwnerNotExists {
        when(ownerRepository.getOwnerById(owner.id)).then(invocation -> owner);
        Owner returnedOwner = editOwnerInfo.getOwnerById(owner.id);
        assertEquals(returnedOwner, owner);
    }
}