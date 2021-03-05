package usecase.parse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class DistanceParser {

    // distance is in format of 17-3.25
    public static int parseDistance(String distance) throws Exception {
        if (distance.contains("m")) return parseMetricDistance(distance);
        try {
            return parseImperialDistance(distance);
        } catch (Exception e) {
            System.out.println("Error parsing distance: " + distance + ", Error: " + Arrays.toString(e.getStackTrace()));
            throw e;
        }
    }

    private static int parseImperialDistance(String distance) {
        String[] splitStr = new String[2];
        if (!distance.contains("'") && !distance.contains("-")) {
            splitStr[0] = "0";
            splitStr[1] = distance;
        } else {
            splitStr = distance.contains("'") ? distance.split("'") : distance.split("-");
        }

        String feetStr = splitStr[0].replaceAll("[^0-9.]", "");
        String inchStr = splitStr[1].replaceAll("[^0-9.]", "");

        int feet = Integer.parseInt(feetStr);

        int hundredthsOfInches = (int) Math.round(100 * Double.parseDouble(inchStr));

        return feet * 12 * 100 + hundredthsOfInches;
    }

    private static int parseMetricDistance(String distance) {
        double meters = Double.parseDouble(distance.substring(0, distance.length() -1));
        return (int) Math.round(meters * 39.37007874d * 100);
    }

}
