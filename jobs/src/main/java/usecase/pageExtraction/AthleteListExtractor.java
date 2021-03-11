package usecase.pageExtraction;

import com.google.gson.Gson;
import domain.exception.CannotParseAthletes;
import fantasyapp.EnvVars;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import usecase.indexing.WebUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AthleteListExtractor {

    public List<AthleteData> extractAthletes(String schoolId) throws IOException, CannotParseAthletes, ExecutionException, InterruptedException {
        Document teamPage = WebUtil
                .queueWebPage("https://www.athletic.net/TrackAndField/School.aspx?SchoolID=" + schoolId + "&S=" + EnvVars.SEASON_YEAR)
                .userAgent("Mozilla/5.0")
                .maxBodySize(0)
                .get();
        return extractAthleteData(teamPage);
    }

    private List<AthleteData> extractAthleteData(Document page) throws CannotParseAthletes {
        Element script = page.selectFirst("script:containsData(\"athletes\":)");
        Matcher matcher = Pattern.compile("\"athletes\":(.*),\"coaches\"").matcher(script.data());
        if (!matcher.find()) {
            throw new CannotParseAthletes();
        }
        final AthleteData[] docs = new Gson().fromJson(matcher.group(1), AthleteData[].class);
        return Arrays.asList(docs);
    }

    private List<String> extractIdsFromPage(Document page) throws CannotParseAthletes {
        Element script = page.selectFirst("script:containsData(\"athletes\":)");
        Matcher matcher = Pattern.compile("\"athletes\":(.*),\"coaches\"").matcher(script.data());
        if (!matcher.find()) {
            throw new CannotParseAthletes();
        }
        final AthleteData[] docs = new Gson().fromJson(matcher.group(1), AthleteData[].class);
        return Arrays.stream(docs).map(AthleteData::getId).collect(Collectors.toList());
    }

}
