package fantasyapp.repository.mapper;

import fantasyapp.dao.MeetDAO;
import domain.entity.Meet;

import java.util.stream.Collectors;

public class MeetMapper {

    public static Meet daoToMeet(MeetDAO dao) {
        return Meet.builder()
                .id(dao.getId())
                .athleticId(dao.getAthleticId())
                .leagueId(dao.getLeagueId())
                .name(dao.getName())
                .hasResults(dao.isHasResults())
                .date(dao.getDate())
                .locked(dao.isLocked())
                .enabled(dao.isEnabled())
                .savedContracts(dao.getSavedContracts().stream()
                        .map(ContractMapper::daoToContract).collect(Collectors.toList()))
                .build();
    }

    public static MeetDAO writeDAO(Meet meet) {
        MeetDAO dao = new MeetDAO();
        dao.setId(meet.id);
        dao.setLeagueId(meet.leagueId);
        dao.setSavedContracts(meet.savedContracts.stream()
                .map(ContractMapper::contractToDAO).collect(Collectors.toList()));
        dao.setAthleticId(meet.athleticId);
        dao.setName(meet.name);
        dao.setDate(meet.date);
        dao.setHasResults(meet.hasResults);
        dao.setEnabled(meet.enabled);
        dao.setLocked(meet.locked);
        return dao;
    }
}
