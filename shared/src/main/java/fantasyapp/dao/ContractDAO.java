package fantasyapp.dao;

import java.time.Instant;

public class ContractDAO {

    private String athleteId, teamId, leagueId, eventId;
    private Instant dateSigned;

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(String athleteId) {
        this.athleteId = athleteId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Instant getDateSigned() {
        return dateSigned;
    }

    public void setDateSigned(Instant dateSigned) {
        this.dateSigned = dateSigned;
    }

}
