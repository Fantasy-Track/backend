package domain.repository;

import domain.entity.Owner;

public interface OwnerRepository {

    boolean isNameTaken(String name);

    Owner getOwnerById(String id);

    void addOwner(Owner owner);

}
