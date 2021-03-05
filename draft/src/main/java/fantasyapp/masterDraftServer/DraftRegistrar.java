package fantasyapp.masterDraftServer;

import com.google.inject.Inject;
import domain.entity.Draft;
import domain.repository.DraftRepository;

import java.util.concurrent.ConcurrentHashMap;

public class DraftRegistrar {

    private DraftRepository draftRepository;
    public static ConcurrentHashMap<String, Object> draftLocks = new ConcurrentHashMap<>();

    @Inject
    public DraftRegistrar(DraftRepository draftRepository) {
        this.draftRepository = draftRepository;
    }

    public void registerDraft(String leagueId, String ipAddress) {
        Draft draft = Draft.builder()
                .id(leagueId)
                .ipAddress(ipAddress)
                .build();
        draftRepository.addDraft(draft);
        draftLocks.put(leagueId, new Object());
    }

    public void unregisterDraft(String leagueId) {
        draftRepository.removeDraft(leagueId);
        draftLocks.remove(leagueId);
    }

}
