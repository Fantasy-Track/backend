package usecase.parse;

public class TimeParser {

    public static int parseTime(String time) {
        time = time.replaceAll("[^0-9.:]", "");
        int secondsIndex = time.lastIndexOf(":");

        if (secondsIndex == -1) {
            return (int) Math.round(Double.parseDouble(time) * 1000);
        }
        double seconds = Double.parseDouble(time.substring(secondsIndex + 1));
        return (int) Math.round(1000 * (parseTime(time.substring(0, secondsIndex), 60) + seconds));
    }

    private static int parseTime(String time, int multiplier) {
        int index = time.lastIndexOf(":");
        if (index == -1) return Integer.parseInt(time) * multiplier;
        return parseTime(time.substring(0, index), multiplier * 60) + multiplier * Integer.parseInt(time.substring(index + 1));
    }

}
