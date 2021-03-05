package fantasyapp.repository.mapper;

import fantasyapp.dao.DraftDAO;
import domain.entity.Draft;

public class DraftMapper {

    public static Draft daoToDraft(DraftDAO dao) {
        return Draft.builder()
                .ipAddress(dao.getIpAddress()).id(dao.getId()).build();
    }

    public static DraftDAO draftToDAO(Draft draft) {
        DraftDAO dao = new DraftDAO();
        dao.setId(draft.id);
        dao.setIpAddress(draft.ipAddress);
        return dao;
    }

}
