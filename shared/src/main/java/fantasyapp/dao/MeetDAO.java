package fantasyapp.dao;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MeetDAO {

    private String id;
    private String athleticId, name, leagueId;
    private Instant date;
    private boolean hasResults, enabled, locked;
    private List<ContractDAO> savedContracts;

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getAthleticId() {
        return athleticId;
    }

    public void setAthleticId(String athleticId) {
        this.athleticId = athleticId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public boolean isHasResults() {
        return hasResults;
    }

    public void setHasResults(boolean hasResults) {
        this.hasResults = hasResults;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ContractDAO> getSavedContracts() {
        return savedContracts == null ? new ArrayList<>() : savedContracts;
    }

    public void setSavedContracts(List<ContractDAO> savedContracts) {
        this.savedContracts = savedContracts;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
