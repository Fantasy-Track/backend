package fantasyapp;

import usecase.pageExtraction.MeetData;
import usecase.pageExtraction.SchoolInfoExtractor;

import java.util.List;

public class ScheduleScrapingTest {
    public static void main(String[] args) throws Exception {
        SchoolInfoExtractor extractor = new SchoolInfoExtractor();
        List<MeetData> meets = extractor.extractMeetSchedule("904");
        System.out.println(meets);
    }
}
