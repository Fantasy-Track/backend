package usecase.createOwner;

import com.google.inject.Inject;
import domain.exception.NameTaken;
import domain.exception.NameTooLong;
import domain.exception.NameTooShort;
import domain.repository.OwnerRepository;

public class OwnerNameValidator {

    private OwnerRepository ownerRepository;

    @Inject
    public OwnerNameValidator(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public void validateName(String ownerName) throws NameTooShort, NameTooLong, NameTaken {
        if (ownerName.length() < 3) throw new NameTooShort();
        else if (ownerName.length() > 15) throw new NameTooLong();
        if(ownerRepository.isNameTaken(ownerName)) throw new NameTaken();
    }

}
