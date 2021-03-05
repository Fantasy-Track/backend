package fantasyapp.dao;

import java.util.List;
import java.util.Map;

public class AthleteDAO {

    private String id;
    private String name;
    private String gender;

    private List<String> primaryEvents;
    private Map<String, Double> projections;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getPrimaryEvents() {
        return primaryEvents;
    }

    public void setPrimaryEvents(List<String> primaryEvents) {
        this.primaryEvents = primaryEvents;
    }

    public Map<String, Double> getProjections() {
        return projections;
    }

    public void setProjections(Map<String, Double> projections) {
        this.projections = projections;
    }
}
