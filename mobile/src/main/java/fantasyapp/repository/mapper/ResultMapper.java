package fantasyapp.repository.mapper;

import fantasyapp.dao.ResultDAO;
import domain.entity.Result;

public class ResultMapper {

    public static Result daoToResult(ResultDAO dao) {
        return Result.builder()
                .meetId(dao.getMeetId())
                .rank(dao.getRank())
                .id(dao.getId())
                .athleteId(dao.getAthleteId())
                .mark(dao.getMark())
                .date(dao.getDate())
                .fantasyPoints(dao.getFantasyPoints())
                .eventId(dao.getEventId())
                .teamId(dao.getTeamId())
                .url(dao.getUrl())
                .build();
    }

}
