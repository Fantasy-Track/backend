package domain.entity;

import org.jsoup.nodes.Element;

public class ResultRow {

    private final Element row;

    public  ResultRow(Element row) {
        this.row = row;
    }

    public String getAthleteId() {
        return row.id().substring(1); //TODO remove substring so that ids are prepended with A
    }

    public String getResultUrl() {
        Element aTag = row.selectFirst("a[href^=/result/]");
        if (aTag == null) return null;
        String url = aTag.attr("href");
        return url.split("/")[2];
    }

    public String getMark() {
        Element mark = row.selectFirst("a[href^=/result/]");
        if (mark == null) return "DNS";
        return mark.ownText();
    }

}
