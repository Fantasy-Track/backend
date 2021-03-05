package mock;

import domain.entity.Athlete;

import java.util.Arrays;
import java.util.HashMap;

public class AthleteBank {

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

    public static final Athlete athlete4 = Athlete.builder()
            .id("A4")
            .name("athleteName4")
            .primaryEvents(Arrays.asList("6", "8"))
            .gender("Male")
            .projections(new HashMap<>())
            .build();

}
