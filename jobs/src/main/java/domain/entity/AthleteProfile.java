package domain.entity;

import fantasyapp.repository.Events;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AthleteProfile {

    private Element element;

    public AthleteProfile(Element element) {
        this.element = element;
    }

    public String getName() {
        Element e = element.selectFirst("span.mr-2");
        return e.text();
    }

    public String getGender() {
        Element e = element.selectFirst("img.mr-1");
        String src = e.attr("src");
        return src.charAt(src.lastIndexOf("/") + 1) == 'm' ? "male" : "female";
    }

    public List<String> getPrimaryEvents() {
        HashMap<String, Integer> participationMap = new HashMap<>();
        Elements elements = element.select("div.athleteResults").select("h5");
        for (Element e : elements) {
            if (Events.isValidEventByName(e.text())) {
                participationMap.computeIfAbsent(e.text(), s -> 0);
                participationMap.put(e.text(), participationMap.get(e.text()) + e.nextElementSibling().select("tr").size());
            }
        }
        List<String> sortedEvents = participationMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 2 && Events.isValidEventByName(entry.getKey()))
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .map(entry -> Events.eventNameToId(entry.getKey(), getGender()))
                .collect(Collectors.toList());
        return sortedEvents.subList(0, Math.min(sortedEvents.size(), 3));
    }

}
