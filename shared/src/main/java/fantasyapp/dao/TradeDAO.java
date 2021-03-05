package fantasyapp.dao;

import domain.entity.TradeStatus;

import java.time.Instant;
import java.util.List;

public class TradeDAO {

    private String id;
    private String proposingTeamId;
    private String acceptingTeamId;
    private String leagueId;

    private Instant date;

    private List<String> receivingAthletes;
    private List<String> offeringAthletes;

    private TradeStatus status;

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getProposingTeamId() {
        return proposingTeamId;
    }

    public void setProposingTeamId(String proposingTeamId) {
        this.proposingTeamId = proposingTeamId;
    }

    public String getAcceptingTeamId() {
        return acceptingTeamId;
    }

    public void setAcceptingTeamId(String acceptingTeamId) {
        this.acceptingTeamId = acceptingTeamId;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public List<String> getReceivingAthletes() {
        return receivingAthletes;
    }

    public void setReceivingAthletes(List<String> receivingAthletes) {
        this.receivingAthletes = receivingAthletes;
    }

    public List<String> getOfferingAthletes() {
        return offeringAthletes;
    }

    public void setOfferingAthletes(List<String> offeringAthletes) {
        this.offeringAthletes = offeringAthletes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TradeStatus getStatus() {
        return status;
    }

    public void setStatus(TradeStatus status) {
        this.status = status;
    }
}
