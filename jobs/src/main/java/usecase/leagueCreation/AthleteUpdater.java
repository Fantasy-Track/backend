package usecase.leagueCreation;

import com.google.inject.Inject;
import domain.entity.Athlete;
import domain.entity.AthleteProfile;
import domain.repository.AthleteUpdateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.pageExtraction.AthleteData;
import usecase.pageExtraction.AthleteProfileExtractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AthleteUpdater {

    private Logger logger = LoggerFactory.getLogger(AthleteUpdater.class);

    private AthleteProfileExtractor extractor;
    private AthleteUpdateRepository athleteUpdateRepository;

    @Inject
    public AthleteUpdater(AthleteProfileExtractor extractor, AthleteUpdateRepository athleteUpdateRepository) {
        this.extractor = extractor;
        this.athleteUpdateRepository = athleteUpdateRepository;
    }

    public void uploadAthlete(AthleteData data) throws IOException {
        logger.info("Indexing athlete: " + data.getName());
        Athlete athlete = Athlete.builder()
                .id(data.getId())
                .name(data.getName())
                .gender(data.getGender())
                .projections(new HashMap<>())
                .primaryEvents(new ArrayList<>())
                .build();
        athleteUpdateRepository.addAthlete(athlete);
    }

    public void updatePrimaryEvents(String athleteId) throws Exception {
        AthleteProfile profile = extractor.extractAthleteProfile(athleteId);
        athleteUpdateRepository.setPrimaryEvents(athleteId, profile.getPrimaryEvents());
    }

}
