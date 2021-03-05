package usecase.leagueCreation;

import domain.entity.Meet;
import domain.repository.MeetRepository;
import mock.MeetBank;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import usecase.pageExtraction.MeetData;
import usecase.pageExtraction.SchoolInfoExtractor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

public class IndexMeetsTest {

    private IndexMeets indexMeets;
    @Mock
    private SchoolInfoExtractor extractor;
    @Mock private MeetRepository meetRepository;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        indexMeets = new IndexMeets(meetRepository, extractor);

        when(extractor.extractMeetSchedule("904")).then(invocation -> {
           return List.of(
                   MeetData.builder().name("New Meet 1").athleticId("AID1").date("2020-03-07T00:00:00").type("1").build(),
                   MeetData.builder().name("New Meet 2").athleticId("AID2").date("2020-03-07T00:00:00").type("1").build(),
                   MeetData.builder().name("New Meet 5").athleticId("AID5").date("2020-03-07T00:00:00").type("1").build(),
                   MeetData.builder().name("New Meet 6").athleticId("AID6").date("2020-03-07T00:00:00").type("1").build()
           );
        });

        when(meetRepository.getMeetsInLeague("test")).then(invocation -> {
           return List.of(
                   MeetBank.testMeet1, //locked, disabled
                   MeetBank.testMeet2, //enabled
                   MeetBank.testMeet4, //enabled
                   MeetBank.testMeet6 //disabled
           );
        });
    }

    @Test
    public void testIndexAndUploadMeets() throws Exception {
        indexMeets.indexAndUploadMeets("904", "test", Instant.now().minus(10, ChronoUnit.DAYS));

        verify(meetRepository).addOrReplaceMeet(argThat(argument -> argument.athleticId.equals("AID5") && argument.enabled)); // test add new
        verify(meetRepository).addOrReplaceMeet(argThat(argument -> argument.athleticId.equals("AID6") && !argument.enabled)); // test update and keep disabled
        verify(meetRepository).addOrReplaceMeet(argThat(argument -> argument.athleticId.equals("AID2") && argument.enabled)); // test update and keep enabled
        verify(meetRepository, never()).addOrReplaceMeet(argThat(argument -> argument.athleticId.equals("AID1"))); // test not updated
        verify(meetRepository, never()).addOrReplaceMeet(argThat(argument -> argument.athleticId.equals("AID4"))); // test not touched
    }
}