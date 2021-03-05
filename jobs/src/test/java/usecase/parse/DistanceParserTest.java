package usecase.parse;

import org.testng.Assert;

import static org.testng.Assert.assertEquals;

public class DistanceParserTest {

    @org.testng.annotations.Test
    public void parseImperialDistanceHyphen() throws Exception {
        int parsedValue = DistanceParser.parseDistance("12-02.20");
        assertEquals(parsedValue, 14620);
    }

    @org.testng.annotations.Test
    public void parseImperialDistance() throws Exception {
        int parsedValue = DistanceParser.parseDistance("12' 02.20\"");
        assertEquals(parsedValue, 14620);
    }


    @org.testng.annotations.Test
    public void parseMetricDistance() throws Exception {
        int parsedValue = DistanceParser.parseDistance("3.71348m");
        assertEquals(parsedValue, 14620);
    }

    @org.testng.annotations.Test
    public void parseWeirdImperial() throws Exception {
        int parsedValue = DistanceParser.parseDistance("5.25");
        assertEquals(parsedValue, 525);
    }

    @org.testng.annotations.Test
    public void parseInvalid() throws Exception {
        try {
            int parsedValue = DistanceParser.parseDistance("great");
            Assert.fail();
        } catch (Exception ignored) {
        }
    }



}