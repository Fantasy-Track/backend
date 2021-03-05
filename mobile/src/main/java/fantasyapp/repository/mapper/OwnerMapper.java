package fantasyapp.repository.mapper;

import fantasyapp.dao.OwnerDAO;
import domain.entity.Owner;

public class OwnerMapper {

    public static Owner daoToOwner(OwnerDAO dao) {
        return Owner.builder()
                .id(dao.getId())
                .name(dao.getName())
                .build();
    }

    public static OwnerDAO ownerToDao(Owner owner) {
        OwnerDAO dao = new OwnerDAO();
        dao.setId(owner.id);
        dao.setName(owner.name);
        return dao;
    }

}
