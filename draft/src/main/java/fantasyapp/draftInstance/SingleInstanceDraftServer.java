package fantasyapp.draftInstance;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class SingleInstanceDraftServer {

    private Injector injector;

    public SingleInstanceDraftServer(String leagueId) {
        this.injector = Guice.createInjector(new DraftInstanceModule(leagueId));
    }

    public GrpcDraftService createGrpcService() {
        return injector.getInstance(GrpcDraftService.class);
    }

    public void startAndBlock() throws InterruptedException {
        DraftThread draftThread = injector.getInstance(DraftThread.class);
        draftThread.start();
        draftThread.join();
    }

}
