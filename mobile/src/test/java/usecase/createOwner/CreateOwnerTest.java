package usecase.createOwner;

import domain.entity.Owner;
import domain.exception.ApplicationException;
import domain.exception.OwnerAlreadyExists;
import domain.repository.OwnerRepository;
import mock.MockOwnerRepo;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class CreateOwnerTest {

    private OwnerNameValidator nameValidator;
    private OwnerRepository ownerRepo;

    private CreateOwner createOwner;

    @BeforeMethod
    public void setUp() {
        nameValidator = Mockito.mock(OwnerNameValidator.class);
        ownerRepo = new MockOwnerRepo();
        createOwner = new CreateOwner(nameValidator, ownerRepo);
    }

    @Test
    public void testCreate() throws ApplicationException {
        CreateOwnerRequest request = CreateOwnerRequest.builder()
                .id("owner")
                .name("ownerName     ") //keep spaces
                .build();

        createOwner.create(request);

        Mockito.verify(nameValidator, Mockito.times(1)).validateName(request.name);

        Owner owner = ownerRepo.getOwnerById(request.id);
        assertNotNull(owner);
        assertEquals(owner.id, "owner");
        assertEquals(owner.name, "ownerName");
    }

    @Test
    public void testAlreadyExists() throws ApplicationException {
        ownerRepo = Mockito.mock(MockOwnerRepo.class);
        Mockito.when(ownerRepo.getOwnerById("owner")).then(_i -> Owner.builder().id("owner").build());

        createOwner = new CreateOwner(nameValidator, ownerRepo);

        CreateOwnerRequest request = CreateOwnerRequest.builder()
                .id("owner")
                .name("ownerName")
                .build();

        try {
            createOwner.create(request);
            Assert.fail();
        } catch (OwnerAlreadyExists ownerAlreadyExists) { }

        Mockito.verify(ownerRepo, Mockito.times(0)).addOwner(Mockito.any());
        Mockito.verify(nameValidator, Mockito.times(1)).validateName(request.name);

    }
}