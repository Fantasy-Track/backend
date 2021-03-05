package domain.entity;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public class Athlete {

    public final String id;
    public final String name;
    public final String gender;
    public final List<String> primaryEvents;
    public final Map<String, Double> projections;


}
