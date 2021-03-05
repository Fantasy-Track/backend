package domain.entity;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MarkTest {

    @Test
    public void testDidStart() {
        Mark mark = new Mark("12-02.20", "9");
        assertTrue(mark.isMarkValid());
    }

    @Test
    public void testDidNotStart() {
        Mark mark = new Mark("DNS", "9");
        assertFalse(mark.isMarkValid());
    }

    @Test
    public void testGetIntValue() {
        Mark mark = new Mark("9.34", "52");
        assertEquals(mark.getIntValue(), -9340);
    }
}