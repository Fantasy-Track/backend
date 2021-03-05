package usecase.parse;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TimeParserTest {

    @Test
    public void testParseLongTime() {
        int parsedValue = TimeParser.parseTime("2:05:45.34");
        assertEquals(parsedValue, 7545340);
    }

    @Test
    public void testParseShortTime() {
        int parsedValue = TimeParser.parseTime("9.34");
        assertEquals(parsedValue, 9340);
    }
}