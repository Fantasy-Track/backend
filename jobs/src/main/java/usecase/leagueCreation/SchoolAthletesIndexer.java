package usecase.leagueCreation;

import fantasyapp.ExecuteJobs;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.pageExtraction.SchoolInfoExtractor;

import java.util.List;

public class SchoolAthletesIndexer {

    private Logger logger = LoggerFactory.getLogger(SchoolAthletesIndexer.class);

    private SchoolInfoExtractor extractor;
    private ProjectionsUpdater projectionsUpdater;
    private IndexSchoolAthletes indexSchoolAthletes;

    @Inject
    public SchoolAthletesIndexer(SchoolInfoExtractor extractor, ProjectionsUpdater projectionsUpdater, IndexSchoolAthletes indexSchoolAthletes) {
        this.extractor = extractor;
        this.projectionsUpdater = projectionsUpdater;
        this.indexSchoolAthletes = indexSchoolAthletes;
    }

    public IndexedAthletesDTO indexAthletesInSchool(String schoolId) throws Exception {
        List<String> athletes = indexSchoolAthletes.indexAndGetAthletesInSchool(schoolId);

        ExecuteJobs.scheduler.submit(() -> generateProjectionsInBackground(schoolId));
        ExecuteJobs.scheduler.submit(() -> indexSchoolAthletes.completeAthleteProfiles(athletes));

        return IndexedAthletesDTO.builder()
                .athletes(athletes)
                .build();
    }

    private void generateProjectionsInBackground(String schoolId) {
        try {
            generateProjections(schoolId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateProjections(String schoolId) throws Exception {
        String divisionId = extractor.extractSchoolDivision(schoolId);
        if (divisionId != null) {
            projectionsUpdater.updateProjections(divisionId);
        } else {
            logger.info("Cannot get division info and generate projections for school: " + schoolId);
        }
    }

}
