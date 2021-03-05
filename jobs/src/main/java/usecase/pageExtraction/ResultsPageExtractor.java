package usecase.pageExtraction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import domain.entity.EventResultsTable;
import domain.entity.Meet;
import domain.exception.CannotParseAthletes;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.indexing.WebUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ResultsPageExtractor {

    private Logger logger = LoggerFactory.getLogger(ResultsPageExtractor.class);
    static final String PROJECTIONS_YEAR = System.getenv("PROJECTIONS_YEAR");

    public List<EventResultsTable> extractEventSections(Meet meet) throws Exception {
        Document resultsPage = WebUtil
                .queueWebPage("https://www.athletic.net/TrackAndField/MeetResults.aspx?Meet=" + meet.athleticId + "&show=all")
                .userAgent("Mozilla/5.0")
                .maxBodySize(0)
                .get();
        HashMap<String, Element> eventBuckets = new HashMap<>();
        Elements tables = resultsPage.body()
                .select("div#M-Results, div#F-Results")
                .select("tbody[id^=E]");
        for (Element table : tables) {
            String eventId = EventResultsTable.getEventIdFromTable(table);
            if (eventBuckets.containsKey(eventId)) {
                eventBuckets.get(eventId).insertChildren(-1, table.children());
            } else {
                eventBuckets.put(eventId, table);
            }
        }
        return eventBuckets.values().stream().map(EventResultsTable::new).collect(Collectors.toList());
    }

    public Elements extractEventRankingsInDivision(String eventId, String divisionId) throws Exception {
        int page = 0;
        Document document;
        Elements rankings = new Elements();
        String divisionSeasonId = getSeasonDivision(divisionId);
        logger.info(String.format("Getting division rankings. Original Division ID: %s, Using ID: %s", divisionId, divisionSeasonId));
        do {
            URI url = new URIBuilder("https://www.athletic.net/TrackAndField/Division/Event.aspx")
                    .setParameter("DivID", divisionSeasonId)
                    .setParameter("Event", eventId)
                    .setParameter("Page", String.valueOf(page)).build();
            logger.info(String.format("Extracting division rankings (DivID: %s, EventID: %s, Page %d)", divisionId, eventId, page));
            logger.info("Loading results from url: " + url);
            document = WebUtil
                    .queueWebPage(url.toString())
                    .userAgent("Mozilla/5.0")
                    .maxBodySize(0)
                    .followRedirects(true)
                    .get();
            //add logging here
            Elements results = extractResultsFromEventPage(document);
            rankings.addAll(results);
            page++;
        } while (document.select("a[ng-click*=appC.setPage]").first() != null);
        return rankings;
    }

    public String getSeasonDivision(String divisionId) throws Exception {
        Document document = WebUtil
                .queueWebPage("https://www.athletic.net/TrackAndField/Division/Event.aspx?DivID=" + divisionId)
                .userAgent("Mozilla/5.0")
                .maxBodySize(0)
                .followRedirects(true)
                .get();
        Element script = document.selectFirst("script:containsData(\"seasons\":)");
        Matcher matcher = Pattern.compile("\"seasons\":(.*),\"tree\"").matcher(script.data());
        if (!matcher.find()) {
            throw new CannotParseAthletes();
        }
        Type mapType = new TypeToken<HashMap<String, String>>(){}.getType();
        final HashMap<String, String> seasons = new Gson().fromJson(matcher.group(1), mapType);
        logger.info("List of division seasons: " + seasons.toString());
        return Optional.ofNullable(seasons.get(PROJECTIONS_YEAR)).orElse(divisionId);
    }

    private Elements extractResultsFromEventPage(Document document) {
        try {
            Elements results = document.selectFirst("table.HLData").select("tr:has(a[href*=Athlete.aspx])");
            return results;
        } catch (Exception e) {
            logger.info("Could not extract rankings from page: " + document.location());
            return new Elements();
        }
    }

    public static String getFinalURL(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        httpClient.execute(httpGet, context);
        List<URI> redirectURIs = context.getRedirectLocations();
        if (redirectURIs != null && !redirectURIs.isEmpty()) {
            for (URI redirectURI : redirectURIs) {
                System.out.println("Redirect URI: " + redirectURI);
            }
            return redirectURIs.get(redirectURIs.size() - 1).toString();
        }
        return url;
    }

}
