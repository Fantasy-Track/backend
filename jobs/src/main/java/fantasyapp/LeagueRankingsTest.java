package fantasyapp;

import com.google.inject.Guice;
import com.google.inject.Injector;
import usecase.leagueCreation.ProjectionsUpdater;
import usecase.pageExtraction.SchoolInfoExtractor;

public class LeagueRankingsTest {

    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(new JobsModule());
        SchoolInfoExtractor extractor = new SchoolInfoExtractor();
//        System.out.println(extractor.extractSchoolDivision("904"));
        ProjectionsUpdater projectionsUpdater = injector.getInstance(ProjectionsUpdater.class);
        projectionsUpdater.updateProjections("107485");
        StaticDB.redis.shutdown();
    }

}
