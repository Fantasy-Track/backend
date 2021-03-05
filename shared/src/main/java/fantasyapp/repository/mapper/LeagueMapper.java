package fantasyapp.repository.mapper;

import fantasyapp.dao.DraftSettingsDAO;
import fantasyapp.dao.LeagueDAO;
import fantasyapp.dao.LeagueSettingsDAO;
import domain.entity.League;

public class LeagueMapper {

    public static League daoToLeague(LeagueDAO dao) {
        return League.builder()
                .name(dao.getName())
                .id(dao.getId())
                .schoolId(dao.getSchoolId())
                .owningTeam(dao.getOwningTeam())
                .athletes(dao.getAthletes())
                .maxTeams(dao.getMaxTeams())
                .draftSettings(League.DraftSettings.builder()
                        .startTime(dao.getDraftSettings().getStartTime())
                        .rounds(dao.getDraftSettings().getRounds())
                        .turnDurationMillis(dao.getDraftSettings().getTurnDurationMillis())
                        .build())
                .leagueSettings(League.LeagueSettings.builder()
                        .teamSize(dao.getLeagueSettings().getTeamSize())
                        .build())
                .status(dao.getStatus())
                .build();
    }

    public static LeagueDAO writeDAO(League league) {
        DraftSettingsDAO draftSettingsDAO = new DraftSettingsDAO();
        draftSettingsDAO.setRounds(league.draftSettings.rounds);
        draftSettingsDAO.setTurnDurationMillis(league.draftSettings.turnDurationMillis);
        draftSettingsDAO.setStartTime(league.draftSettings.startTime);

        LeagueSettingsDAO leagueSettingsDAO = new LeagueSettingsDAO();
        leagueSettingsDAO.setTeamSize(league.leagueSettings.teamSize);

        LeagueDAO dao = new LeagueDAO();
        dao.setId(league.id);
        dao.setSchoolId(league.schoolId);
        dao.setOwningTeam(league.owningTeam);
        dao.setName(league.name);
        dao.setMaxTeams(league.maxTeams);
        dao.setAthletes(league.athletes);
        dao.setDraftSettings(draftSettingsDAO);
        dao.setLeagueSettings(leagueSettingsDAO);
        dao.setStatus(league.status);
        return dao;
    }
}
