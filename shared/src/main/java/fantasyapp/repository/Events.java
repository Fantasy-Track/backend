package fantasyapp.repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Events {
    
    public static final HashSet<String> FIELD_EVENTS;
    public static final HashSet<String>  DISTANCE_EVENTS;
    public static final HashSet<String> SPRINT_EVENTS;
    public static final HashSet<String> BENCH_EVENTS = new HashSet<>(Collections.singletonList("-5"));

    public static final String FIELD_ID = "f";
    public static final String DISTANCE_ID = "d";
    public static final String SPRINTS_ID = "s";
    public static final String BENCH_ID = "b";

    public static final HashSet<Event> EVENTS = new HashSet<>(Arrays.asList(
            new Event(SPRINTS_ID, "41", "46", "50 Meter Dash"),
            new Event(SPRINTS_ID, "42", "47", "60 Meter Dash"),
            new Event(SPRINTS_ID, "1", "19", "100 Meters"),
            new Event(SPRINTS_ID, null, "28", "100m Hurdles - 33\""),
            new Event(SPRINTS_ID, "10", null, "110m Hurdles - 39\""),
            new Event(SPRINTS_ID, null, "29", "300m Hurdles - 30\""),
            new Event(SPRINTS_ID, "11", null, "300m Hurdles - 36\""),
            new Event(SPRINTS_ID, "45", null, "400m Hurdles - 36\""),
            new Event(SPRINTS_ID, null, "94", "400m Hurdles - 30\""),
            new Event(SPRINTS_ID, "2", "20", "200 Meters"),
            new Event(SPRINTS_ID, "3", "21", "400 Meters"),
            new Event(DISTANCE_ID, "4", "22", "800 Meters"),
            new Event(DISTANCE_ID, "56", "57", "1000 Meters"),
            new Event(DISTANCE_ID, "52", "53", "1600 Meters"),
            new Event(DISTANCE_ID, "6", "24", "3000 Meters"),
            new Event(DISTANCE_ID, "60", "61", "3200 Meters"),
            new Event(DISTANCE_ID, "79", "80", "5000 Meters"),
            new Event(DISTANCE_ID, "118", "119", "10,000 Meters"),
            new Event(FIELD_ID, "9", "27", "High Jump"),
            new Event(FIELD_ID, "17", "35", "Long Jump"),
            new Event(FIELD_ID, "18", "36", "Triple Jump"),
            new Event(FIELD_ID, "16", "34", "Pole Vault"),
            new Event(FIELD_ID, "13", "31", "Discus - 1.6kg"),
            new Event(FIELD_ID, "12", "30", "Shot Put - 12lb")
            ));

    // must be after declaring EVENTS
    static {
        System.out.println("Number of Events: " + EVENTS.size());

        FIELD_EVENTS = new HashSet<>();
        DISTANCE_EVENTS = new HashSet<>();
        SPRINT_EVENTS = new HashSet<>();

        for (Event event : EVENTS) {
            switch (event.category) {
                case FIELD_ID:
                    if (event.maleId != null) FIELD_EVENTS.add(event.maleId);
                    if (event.femaleId != null) FIELD_EVENTS.add(event.femaleId);
                    break;
                case DISTANCE_ID:
                    if (event.maleId != null) DISTANCE_EVENTS.add(event.maleId);
                    if (event.femaleId != null) DISTANCE_EVENTS.add(event.femaleId);
                    break;
                case SPRINTS_ID:
                    if (event.maleId != null) SPRINT_EVENTS.add(event.maleId);
                    if (event.femaleId != null) SPRINT_EVENTS.add(event.femaleId);
                    break;
            }
        }
    }

    public static boolean isValidEventByName(String eventName) {
        return EVENTS.stream().anyMatch(event -> event.name.equals(eventName));
    }

    public static String eventNameToId(String eventName, String gender) {
        Event e = EVENTS.stream().filter(event -> event.name.equals(eventName)).findFirst().get();
        return gender.equalsIgnoreCase("male") ? e.maleId : e.femaleId;
    }

    public static boolean isValidEvent(String eventId) {
        if (FIELD_EVENTS.contains(eventId)) return true;
        if (DISTANCE_EVENTS.contains(eventId)) return true;
        if (SPRINT_EVENTS.contains(eventId)) return true;
        if (BENCH_EVENTS.contains(eventId)) return true;
        return false;
    }

    public static String eventToCategory(String eventId) {
        if (FIELD_EVENTS.contains(eventId)) return FIELD_ID;
        if (DISTANCE_EVENTS.contains(eventId)) return DISTANCE_ID;
        if (SPRINT_EVENTS.contains(eventId)) return SPRINTS_ID;
        if (BENCH_EVENTS.contains(eventId)) return BENCH_ID;
        return null;
    }

    public static Set<String> getEventsInCategory(String categoryId) {
        if (categoryId.equals(FIELD_ID)) return FIELD_EVENTS;
        if (categoryId.equals(DISTANCE_ID)) return DISTANCE_EVENTS;
        if (categoryId.equals(SPRINTS_ID)) return SPRINT_EVENTS;
        return BENCH_EVENTS;
    }

    public static class Event {
        public final String category;
        public final String maleId;
        public final String femaleId;
        public final String name;

        Event(String category, String maleId, String femaleId, String name) {
            this.category = category;
            this.maleId = maleId;
            this.femaleId = femaleId;
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof String) {
                if (obj.equals(maleId) || obj.equals(femaleId)) return true;
            }
            return false;
        }

    }

}
