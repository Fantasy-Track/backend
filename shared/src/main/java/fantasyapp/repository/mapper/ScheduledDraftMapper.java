package fantasyapp.repository.mapper;

import fantasyapp.dao.ScheduledDraftDAO;
import domain.entity.ScheduledDraft;

public class ScheduledDraftMapper {

    public static ScheduledDraft writeEntity(ScheduledDraftDAO dao) {
        return ScheduledDraft.builder()
                .id(dao.getId())
                .time(dao.getTime())
                .build();
    }

}
