package usecase.login;

import com.google.inject.Inject;
import domain.entity.Owner;
import domain.exception.AccountNotFound;
import domain.repository.OwnerRepository;

public class Login {

    private OwnerRepository ownerRepository;

    @Inject
    public Login(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public void login(String ownerId) throws AccountNotFound {
        Owner owner = ownerRepository.getOwnerById(ownerId);
        if (owner == null) throw new AccountNotFound();
    }

}
