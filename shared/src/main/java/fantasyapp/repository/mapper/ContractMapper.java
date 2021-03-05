package fantasyapp.repository.mapper;

import fantasyapp.dao.ContractDAO;
import fantasyapp.repository.Events;
import domain.entity.Contract;

public class ContractMapper {

    public static Contract daoToContract(ContractDAO dao) {
        return Contract.builder()
                .athleteId(dao.getAthleteId())
                .teamId(dao.getTeamId())
                .eventId(dao.getEventId())
                .dateSigned(dao.getDateSigned())
                .leagueId(dao.getLeagueId())
                .categoryId(Events.eventToCategory(dao.getEventId()))
                .build();
    }

    public static ContractDAO contractToDAO(Contract contract) {
        ContractDAO dao = new ContractDAO();
        dao.setAthleteId(contract.athleteId);
        dao.setTeamId(contract.teamId);
        dao.setDateSigned(contract.dateSigned);
        dao.setEventId(contract.eventId);
        dao.setLeagueId(contract.leagueId);
        return dao;
    }
}
