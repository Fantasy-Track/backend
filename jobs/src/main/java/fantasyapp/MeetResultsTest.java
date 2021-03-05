package fantasyapp;

import com.google.inject.Guice;
import com.google.inject.Injector;
import domain.entity.Meet;
import domain.repository.MeetRepository;
import usecase.meetProcessing.GenerateMeetReport;

import java.time.Instant;
import java.util.ArrayList;

public class MeetResultsTest {
    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(new JobsModule());
        GenerateMeetReport report = injector.getInstance(GenerateMeetReport.class);
        MeetRepository meetRepository = injector.getInstance(MeetRepository.class);
        Meet meet = Meet.builder().athleticId("394414").id("hahaha").leagueId("NGLaRa").date(Instant.now()).name("bigLick").hasResults(false).savedContracts(new ArrayList<>()).enabled(true).build();
        System.out.println(report.generate(meet));
    }
}
