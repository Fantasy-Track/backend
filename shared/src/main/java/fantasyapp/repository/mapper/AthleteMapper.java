package fantasyapp.repository.mapper;

import fantasyapp.dao.AthleteDAO;
import domain.entity.Athlete;
import lombok.NonNull;

public class AthleteMapper {

    public static Athlete daoToAthlete(@NonNull AthleteDAO dao) {
        return Athlete.builder()
                .id(dao.getId())
                .name(dao.getName())
                .primaryEvents(dao.getPrimaryEvents())
                .gender(dao.getGender())
                .projections(dao.getProjections())
                .build();
    }

    public static AthleteDAO entityToDAO(Athlete athlete) {
        AthleteDAO dao = new AthleteDAO();
        dao.setId(athlete.id);
        dao.setName(athlete.name);
        dao.setProjections(athlete.projections);
        dao.setGender(athlete.gender);
        dao.setPrimaryEvents(athlete.primaryEvents);
        return dao;
    }

}
