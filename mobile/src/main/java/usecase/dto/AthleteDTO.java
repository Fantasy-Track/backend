package usecase.dto;

import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@SuperBuilder
public class AthleteDTO {

    public final String id;
    public final String name;
    public final String gender;
    public final List<String> primaryEvents;
    public final Map<String, Double> projections;

}
