package mock;

import domain.entity.Athlete;
import domain.repository.AthleteRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MockAthleteRepository implements AthleteRepository {

    public static final Athlete athlete1 = Athlete.builder()
            .id("A1")
            .name("athleteName1")
            .primaryEvents(Arrays.asList("6", "9"))
            .gender("Male")
            .projections(new HashMap<>())
            .build();

    public static final Athlete athlete2 = Athlete.builder()
            .id("A2")
            .name("athleteName2")
            .primaryEvents(Arrays.asList("3", "9"))
            .gender("Female")
            .projections(new HashMap<>())
            .build();

    public static final Athlete athlete3 = Athlete.builder()
            .id("A3")
            .name("athleteName3")
            .primaryEvents(Arrays.asList("6", "8"))
            .gender("Male")
            .projections(new HashMap<>())
            .build();

    private HashMap<String, Athlete> athleteHashMap = new HashMap<>();

    public MockAthleteRepository() {
        athleteHashMap.put(athlete1.id, athlete1);
        athleteHashMap.put(athlete2.id, athlete2);
        athleteHashMap.put(athlete3.id, athlete3);
    }

    @Override
    public Athlete getAthleteById(String id) {
        return athleteHashMap.get(id);
    }

    @Override
    public List<Athlete> getAthletesByIds(List<String> ids) {
        return athleteHashMap.values().stream()
                .filter(athlete -> ids.contains(athlete.id))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAthleteInLeague(String athleteId, String leagueId) {
        return true;
    }
}
