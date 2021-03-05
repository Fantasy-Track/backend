package fantasyapp.masterDraftServer;

import com.google.inject.Inject;
import com.google.protobuf.Empty;
import com.fantasytrack.protos.DraftGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class DraftStressTest {
    private DraftGrpc.DraftBlockingStub stub;

    @Inject
    public DraftStressTest(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    /** Construct client for accessing RouteGuide server using the existing channel. */
    public DraftStressTest(ManagedChannelBuilder<?> channelBuilder) {
        ManagedChannel channel = channelBuilder.build();
        stub = DraftGrpc.newBlockingStub(channel);
    }

    public void connect() {
        for(int i = 0; i < 300; i++) {
            Thread thread = new Thread(() -> {
                stub.draftingStream(Empty.newBuilder().build()).forEachRemaining(draftState -> System.out.println("got"));
            });
            thread.setDaemon(false);
            thread.start();
        }
    }

    public static void main(String[] args) {
        DraftStressTest test = new DraftStressTest("localhost", 8008);
        test.connect();
    }
}
