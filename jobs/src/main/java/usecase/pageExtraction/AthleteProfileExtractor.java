package usecase.pageExtraction;

import domain.entity.AthleteProfile;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import usecase.indexing.WebUtil;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class AthleteProfileExtractor {

    public AthleteProfile extractAthleteProfile(String athleteId) throws IOException, ExecutionException, InterruptedException {
        Document page = WebUtil
                .queueWebPage("https://www.athletic.net/TrackAndField/Athlete.aspx?AID=" + athleteId)
                .userAgent("Mozilla/5.0")
                .maxBodySize(0)
                .get();
        return extractProfileFromPage(page);
    }

    private AthleteProfile extractProfileFromPage(Document page) {
        return new AthleteProfile(page.selectFirst("main#anetMain").selectFirst("div[ng-controller=AppCtrl as appC]"));
    }

}
