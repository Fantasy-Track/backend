package domain.entity;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import usecase.pageExtraction.AthleteProfileExtractor;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.testng.Assert.assertEquals;

public class AthleteProfileTest {

    AthleteProfile profile;

    @BeforeClass
    public void beforeClass() throws IOException, ExecutionException, InterruptedException {
        profile = new AthleteProfileExtractor().extractAthleteProfile("12292606");
    }

    @Test
    public void testGetName() {
        assertEquals(profile.getName(), "Gregory Saldanha");
    }

    @Test
    public void testGetGender() {
        assertEquals(profile.getGender(), "male");
    }

    @Test
    public void testPrimaryEvents() {
        List<String> events = profile.getPrimaryEvents();
        assertEquals(events.size(), 3);
        assertEquals(events, List.of("9", "18", "17"));
    }

}