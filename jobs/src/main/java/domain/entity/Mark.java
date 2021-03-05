package domain.entity;

import fantasyapp.repository.Events;
import lombok.SneakyThrows;
import usecase.parse.DistanceParser;
import usecase.parse.TimeParser;

public class Mark {

    private final String actualValue;
    private final int intValue;

    @SneakyThrows
    public Mark(String mark, String eventId) {
        actualValue = mark;

        if (!isMarkValid())
            intValue = 0;
        else if (Events.FIELD_ID.equals(Events.eventToCategory(eventId)))
            intValue = DistanceParser.parseDistance(mark);
        else
            intValue = -TimeParser.parseTime(mark);
    }

    public int getIntValue() {
        return intValue;
    }

    // did not start if string contains no digits
    public boolean isMarkValid() {
        return actualValue.matches(".*\\d+.*");
    }

    @Override
    public String toString() {
        return actualValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Mark) {
            return ((Mark) obj).intValue == this.intValue;
        }
        return false;
    }
}
