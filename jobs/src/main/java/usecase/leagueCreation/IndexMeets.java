package usecase.leagueCreation;

import com.google.inject.Inject;
import domain.entity.Meet;
import domain.repository.MeetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.pageExtraction.MeetData;
import usecase.pageExtraction.SchoolInfoExtractor;
import util.TimeUtil;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class IndexMeets {

    private Logger logger = LoggerFactory.getLogger(IndexMeets.class);

    private MeetRepository meetRepository;
    private SchoolInfoExtractor extractor;

    @Inject
    public IndexMeets(MeetRepository meetRepository, SchoolInfoExtractor extractor) {
        this.meetRepository = meetRepository;
        this.extractor = extractor;
    }

    // This method will add any new meets and replace old meets
    public void indexAndUploadMeets(String schoolId, String leagueId, Instant draftTime) throws Exception {
        List<MeetData> newMeets = extractor.extractMeetSchedule(schoolId);
        Map<String, Meet> prevMeets = meetRepository.getMeetsInLeague(leagueId).stream().collect(Collectors.toMap(meet -> meet.athleticId, meet -> meet));

        for (MeetData meetData : newMeets) {
            logger.info("Indexing meet: " + meetData);
            Instant meetDate = Instant.parse(meetData.date + "Z").truncatedTo(ChronoUnit.DAYS);
            boolean enabled = !TimeUtil.isAfterPST(draftTime, meetDate);

            Meet prevMeet = prevMeets.get(meetData.athleticId);
            if (prevMeet != null) {
                if (prevMeet.locked || prevMeet.hasResults) continue;
                if (enabled) enabled = prevMeet.enabled; // only is before draft time
                addOrReplaceMeet(prevMeet.id, leagueId, meetData, meetDate, enabled);
            } else {
                addOrReplaceMeet(null, leagueId, meetData, meetDate, enabled);
            }
        }
    }

    private void addOrReplaceMeet(String id, String leagueId, MeetData meetData, Instant meetDate, boolean enabled) {
        logger.info("Adding/replacing meet: " + meetData);
        Meet meet = Meet.builder()
                .id(id == null ? UUID.randomUUID().toString() : id)
                .athleticId(meetData.athleticId)
                .name(meetData.name)
                .enabled(enabled)
                .hasResults(false)
                .locked(false)
                .leagueId(leagueId)
                .date(meetDate)
                .savedContracts(new ArrayList<>())
                .build();
        meetRepository.addOrReplaceMeet(meet);
    }

}
