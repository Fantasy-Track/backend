package usecase.pageExtraction;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import domain.exception.CannotParse;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import usecase.indexing.WebUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SchoolInfoExtractor {

    static final String SEASON = System.getenv("SEASON_YEAR");

    private Document getSchoolPage(String schoolId) throws Exception {
        return WebUtil
                .queueWebPage("https://www.athletic.net/TrackAndField/School.aspx?SchoolID=" + schoolId + "&S=" + SEASON)
                .userAgent("Mozilla/5.0")
                .maxBodySize(0)
                .get();
    }

    public String extractSchoolDivision(String schoolId) throws Exception { // TODO implement proper work system.
        Document teamPage = getSchoolPage(schoolId);
        Element script = teamPage.selectFirst("script:containsData(anetSiteAppParams)");
        Matcher matcher = Pattern.compile("\"tree\":(.*)};").matcher(script.data());
        if (!matcher.find()) {
            throw new CannotParse("school division");
        }
        DivisionData[] data =  new Gson().fromJson(matcher.group(1), DivisionData[].class);
        if (data.length < 2) return null;
        return data[data.length - 2].id;
    }

    public List<MeetData> extractMeetSchedule(String schoolId) throws Exception {
        Document schoolPage = getSchoolPage(schoolId);
        Element script = schoolPage.selectFirst("script:containsData(\"currentCal\":)");
        Matcher matcher = Pattern.compile("\"currentCal\":(.*),\"inviteRequests\"").matcher(script.data());
        if (!matcher.find()) {
            throw new CannotParse("calendar");
        }

        MeetData[] meetData = new Gson().fromJson(matcher.group(1), MeetData[].class);
        ArrayList<MeetData> meets = new ArrayList<>();
        for (MeetData data : meetData) {
            if (data.type.equals("1")) {
                meets.add(data);
            }
        }
        return meets;
    }


    static class DivisionData {
        @SerializedName("id") public String id;
    }

}
