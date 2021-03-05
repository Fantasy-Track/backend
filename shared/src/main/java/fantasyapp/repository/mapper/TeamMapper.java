package fantasyapp.repository.mapper;

import fantasyapp.dao.TeamDAO;
import domain.entity.Team;

public class TeamMapper {

    public static Team daoToTeam(TeamDAO dao) {
        return Team.builder()
                .id(dao.getId())
                .leagueId(dao.getLeagueId())
                .ownerId(dao.getOwnerId())
                .name(dao.getName())
                .fantasyPoints(dao.getFantasyPoints())
                .build();
    }

    public static TeamDAO teamToDAO(Team team) {
        TeamDAO dao = new TeamDAO();
        dao.setId(team.id);
        dao.setLeagueId(team.leagueId);
        dao.setName(team.name);
        dao.setOwnerId(team.ownerId);
        dao.setFantasyPoints(team.fantasyPoints);
        return dao;
    }
}
