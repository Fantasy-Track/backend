package domain.repository;

import domain.entity.Draft;

public interface DraftRepository {

    Draft getDraftById(String id);

    void addDraft(Draft draft);

    void removeDraft(String id);

}
