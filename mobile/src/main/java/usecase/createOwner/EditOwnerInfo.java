package usecase.createOwner;

import com.google.inject.Inject;
import domain.entity.Owner;
import domain.exception.NameTaken;
import domain.exception.NameTooLong;
import domain.exception.NameTooShort;
import domain.exception.OwnerNotExists;
import domain.repository.NameRepository;
import domain.repository.OwnerRepository;

public class EditOwnerInfo {

    private NameRepository nameRepository;
    private OwnerRepository ownerRepository;
    private OwnerNameValidator nameValidator;

    @Inject
    public EditOwnerInfo(NameRepository nameRepository, OwnerRepository ownerRepository, OwnerNameValidator nameValidator) {
        this.nameRepository = nameRepository;
        this.ownerRepository = ownerRepository;
        this.nameValidator = nameValidator;
    }

    public Owner editOwnerName(String ownerId, String ownerName) throws NameTooShort, NameTaken, NameTooLong, OwnerNotExists {
        nameValidator.validateName(ownerName);
        nameRepository.updateOwnerName(ownerId, ownerName);
        return getOwnerById(ownerId);
    }

    public Owner getOwnerById(String id) throws OwnerNotExists {
        Owner owner = ownerRepository.getOwnerById(id);
        if (owner == null) throw new OwnerNotExists();
        return owner;
    }

}
