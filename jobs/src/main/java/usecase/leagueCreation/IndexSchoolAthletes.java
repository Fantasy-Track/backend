package usecase.leagueCreation;

import com.google.inject.Inject;
import domain.repository.AthleteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.pageExtraction.AthleteData;
import usecase.pageExtraction.AthleteListExtractor;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class IndexSchoolAthletes {

    private Logger logger = LoggerFactory.getLogger(IndexSchoolAthletes.class);

    private AthleteListExtractor extractor;
    private AthleteUpdater indexer;
    private AthleteRepository athleteRepository;

    @Inject
    public IndexSchoolAthletes(AthleteListExtractor extractor, AthleteUpdater indexer, AthleteRepository athleteRepository) {
        this.extractor = extractor;
        this.indexer = indexer;
        this.athleteRepository = athleteRepository;
    }

    public List<String> indexAndGetAthletesInSchool(String schoolId) throws Exception {
        List<AthleteData> athletes = extractor.extractAthletes(schoolId);
        List<String> athleteIds = athletes.stream().map(AthleteData::getId).collect(Collectors.toList());
        HashSet<String> existingAthleteIds = athleteRepository
                .getAthletesByIds(athleteIds)
                .stream().map(athlete -> athlete.id).collect(Collectors.toCollection(HashSet::new));

        athletes.stream().filter(s -> !existingAthleteIds.contains(s.getId())).forEach((athlete) -> {
            try {
                logger.info("Indexing athlete: " + athlete.getName());
                indexer.uploadAthlete(athlete);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return athleteIds;
    }

    public void completeAthleteProfiles(List<String> athleteIds) {
        long lastTime = System.currentTimeMillis();
        for (String athleteId : athleteIds) {
            logger.info("Completing athlete profile: " + athleteId);
            try {
                indexer.updatePrimaryEvents(athleteId);
            } catch (Exception e) {
                logger.error("Error updating athlete primary events", e);
            }
        }
        long currTime = System.currentTimeMillis();
        System.out.println(String.format("Time to complete %s profiles: %d", athleteIds.size(), currTime - lastTime));
    }

}
