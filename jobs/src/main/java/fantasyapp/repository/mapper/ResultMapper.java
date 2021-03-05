package fantasyapp.repository.mapper;

import fantasyapp.dao.ResultDAO;
import domain.entity.IndexedResult;
import domain.entity.ScoredResult;

public class ResultMapper {

    public static ResultDAO resultToDAO(ScoredResult scoredResult, String teamId) {
        IndexedResult indexedResult = scoredResult.result;

        ResultDAO dao = new ResultDAO();
        dao.setId(indexedResult.athleteId + "_" + indexedResult.meetId + "_" + indexedResult.eventId);
        dao.setLeagueId(indexedResult.leagueId);
        dao.setAthleteId(indexedResult.athleteId);
        dao.setDate(indexedResult.date);
        dao.setEventId(indexedResult.eventId);
        dao.setMark(indexedResult.mark.toString());
        dao.setMeetId(indexedResult.meetId);
        dao.setUrl(indexedResult.url);
        dao.setTeamId(teamId);
        dao.setRank(scoredResult.rank);
        dao.setFantasyPoints(scoredResult.fantasyPoints);

        return dao;
    }

}
