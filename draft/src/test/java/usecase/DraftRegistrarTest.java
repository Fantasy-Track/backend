package usecase;

import fantasyapp.masterDraftServer.DraftRegistrar;
import domain.entity.Draft;
import domain.repository.DraftRepository;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class DraftRegistrarTest {

    @Test
    public void testRegisterDraft() {
        DraftRepository draftRepo = Mockito.mock(DraftRepository.class);
        DraftRegistrar registrar = new DraftRegistrar(draftRepo);
        registrar.registerDraft("test", "192.168.0.255");

        ArgumentCaptor<Draft> argument = ArgumentCaptor.forClass(Draft.class);
        Mockito.verify(draftRepo, Mockito.times(1)).addDraft(argument.capture());

        assertEquals(argument.getValue().ipAddress, "192.168.0.255");
        assertEquals(argument.getValue().id, "test");
    }

    @Test
    public void testUnregisterDraft() {
        DraftRepository draftRepo = Mockito.mock(DraftRepository.class);
        DraftRegistrar registrar = new DraftRegistrar(draftRepo);
        registrar.unregisterDraft("test");

        Mockito.verify(draftRepo, Mockito.times(1)).removeDraft("test");
    }

}