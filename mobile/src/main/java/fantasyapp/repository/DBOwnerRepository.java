package fantasyapp.repository;

import fantasyapp.dao.OwnerDAO;
import fantasyapp.repository.mapper.OwnerMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import domain.entity.Owner;
import domain.repository.OwnerRepository;

import javax.inject.Inject;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.*;

public class DBOwnerRepository implements OwnerRepository {

    private MongoCollection<OwnerDAO> ownerCollection;

    @Inject
    public DBOwnerRepository(MongoDatabase database) {
        ownerCollection = database.getCollection("owners", OwnerDAO.class);
    }

    @Override
    public boolean isNameTaken(String name) {
        return null != ownerCollection.find(and(
                regex("name", "^(?i)" + Pattern.quote(name) + "$"))).first();
    }

    @Override
    public Owner getOwnerById(String id) {
        OwnerDAO dao = ownerCollection.find(eq("_id", id)).first();
        if (dao == null) return null;
        return OwnerMapper.daoToOwner(dao);
    }

    @Override
    public void addOwner(Owner owner) {
        ownerCollection.insertOne(OwnerMapper.ownerToDao(owner));
    }

}
