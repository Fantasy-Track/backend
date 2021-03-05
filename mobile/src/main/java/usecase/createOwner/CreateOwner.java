package usecase.createOwner;

import com.google.inject.Inject;
import domain.entity.Owner;
import domain.exception.ApplicationException;
import domain.exception.OwnerAlreadyExists;
import domain.repository.OwnerRepository;

public class CreateOwner {

    private OwnerNameValidator nameValidator;
    private OwnerRepository ownerRepository;

    @Inject
    public CreateOwner(OwnerNameValidator nameValidator, OwnerRepository ownerRepository) {
        this.nameValidator = nameValidator;
        this.ownerRepository = ownerRepository;
    }

    public void create(CreateOwnerRequest request) throws ApplicationException {
        nameValidator.validateName(request.name);
        validateOwnerNotExists(request.id);

        Owner owner = buildOwner(request);
        ownerRepository.addOwner(owner);
    }

    private void validateOwnerNotExists(String id) throws OwnerAlreadyExists {
        Owner owner = ownerRepository.getOwnerById(id);
        if (owner != null) throw new OwnerAlreadyExists();
    }

    private Owner buildOwner(CreateOwnerRequest request) {
        return Owner.builder()
                .id(request.id)
                .name(request.name.trim())
                .build();
    }

}
