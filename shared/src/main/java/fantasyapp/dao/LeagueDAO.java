package fantasyapp.dao;

import domain.entity.LeagueStatus;

import java.util.List;

public class LeagueDAO {

    // FIELDS
    private String id;
    private String owningTeam;
    private String name;
    private String schoolId;

    private int maxTeams;
    private List<String> athletes;

    private DraftSettingsDAO draftSettings;
    private LeagueSettingsDAO leagueSettings;

    private LeagueStatus status;

    public String getOwningTeam() {
        return owningTeam;
    }

    public void setOwningTeam(String owningTeam) {
        this.owningTeam = owningTeam;
    }

    public int getMaxTeams() {
        return maxTeams;
    }

    public void setMaxTeams(int maxTeams) {
        this.maxTeams = maxTeams;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DraftSettingsDAO getDraftSettings() {
        return draftSettings;
    }

    public void setDraftSettings(DraftSettingsDAO draftSettings) {
        this.draftSettings = draftSettings;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getAthletes() {
        return athletes;
    }

    public void setAthletes(List<String> athletes) {
        this.athletes = athletes;
    }

    public LeagueSettingsDAO getLeagueSettings() {
        return leagueSettings;
    }

    public void setLeagueSettings(LeagueSettingsDAO leagueSettings) {
        this.leagueSettings = leagueSettings;
    }

    public LeagueStatus getStatus() {
        return status;
    }

    public void setStatus(LeagueStatus status) {
        this.status = status;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }
}
