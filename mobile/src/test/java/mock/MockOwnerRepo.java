package mock;

import domain.entity.Owner;
import domain.repository.OwnerRepository;

import java.util.HashMap;

public class MockOwnerRepo implements OwnerRepository {

    private HashMap<String, Owner> ownerHashMap = new HashMap<>();

    @Override
    public boolean isNameTaken(String name) {
        if (name.equals("takenName")) return true;
        return false;
    }

    @Override
    public Owner getOwnerById(String id) {
        return ownerHashMap.get(id);
    }

    @Override
    public void addOwner(Owner owner) {
        ownerHashMap.put(owner.id, owner);
    }

}
