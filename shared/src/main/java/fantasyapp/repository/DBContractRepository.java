package fantasyapp.repository;

import fantasyapp.dao.ContractDAO;
import fantasyapp.repository.mapper.ContractMapper;
import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import domain.entity.Contract;
import domain.repository.ContractRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class DBContractRepository implements ContractRepository {

    private Logger logger = LoggerFactory.getLogger(DBContractRepository.class);
    private MongoCollection<ContractDAO> contractCollection;

    @Inject
    public DBContractRepository(MongoDatabase database) {
        contractCollection = database.getCollection("contracts", ContractDAO.class);
    }

    @Override
    public List<Contract> getTeamContracts(String teamId) {
        Iterable<ContractDAO> daos = contractCollection.find(eq("teamId", teamId));
        List<Contract> contracts = new ArrayList<>();
        for (ContractDAO dao : daos) {
            contracts.add(ContractMapper.daoToContract(dao));
        }
        return contracts;
    }

    @Override
    public List<Contract> getLeagueContracts(String leagueId) {
        List<Contract> contractEntities = new ArrayList<>();
        for (ContractDAO dao : contractCollection.find(eq("leagueId", leagueId))) {
            contractEntities.add(ContractMapper.daoToContract(dao));
        }
        return contractEntities;
    }

    @Override
    public void signContract(Contract contract) {
        logger.info("Signing a contract: " + contract);
        contractCollection.insertOne(ContractMapper.contractToDAO(contract));
    }

    @Override
    public boolean isAthleteOnTeam(String athleteId, String teamId) {
        ContractDAO dao = contractCollection.find(and(eq("athleteId", athleteId), eq("teamId", teamId))).first();
        return dao != null;
    }

    @Override
    public boolean isAthleteFreeAgent(String athleteId, String leagueId) {
        ContractDAO dao = contractCollection.find(and(eq("athleteId", athleteId), eq("leagueId", leagueId))).first();
        return dao == null;
    }

    @Override
    public void deleteContract(String athleteId, String teamId) {
        contractCollection.deleteOne(and(eq("athleteId", athleteId), eq("teamId", teamId)));
    }

}
