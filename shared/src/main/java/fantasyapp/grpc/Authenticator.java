package fantasyapp.grpc;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.inject.Inject;
import domain.entity.Team;
import domain.repository.TeamRepository;
import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

/**
 * Created by rayt on 10/6/16.
 */
public class Authenticator implements ServerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(Authenticator.class);

    private final TeamRepository teamRepository;

    private static final ServerCall.Listener NOOP_LISTENER = new ServerCall.Listener() {
    };
    public static Context.Key<String> teamKey = Context.key("teamId");
    public static Context.Key<String> ownerKey = Context.key("ownerId");
    public static Context.Key<String> leagueKey = Context.key("leagueId");
    static Metadata.Key<String> teamMeta = Metadata.Key.of("teamId", ASCII_STRING_MARSHALLER);
    static Metadata.Key<String> tokenMeta = Metadata.Key.of("idToken", ASCII_STRING_MARSHALLER);

    @Inject
    public Authenticator(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String idToken = metadata.get(tokenMeta);
        String uid;
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            uid = decodedToken.getUid();
        } catch (Exception e) {
            logger.warn("Token is invalid", e);
            serverCall.close(Status.UNAUTHENTICATED.withDescription("Token invalid"), metadata);
            return NOOP_LISTENER;
        }

        Context ctx = Context.current().withValue(ownerKey, uid);

        String teamId = metadata.get(teamMeta);
        if (teamId != null && !teamId.isEmpty()) {
            Team team = teamRepository.getTeamById(teamId);
            if (team == null || !team.ownerId.equals(uid)) {
                serverCall.close(Status.UNAUTHENTICATED.withDescription("Team/League Permission Error"), metadata);
                return NOOP_LISTENER;
            }
            ctx = ctx.withValue(Authenticator.leagueKey, team.leagueId);
            ctx = ctx.withValue(teamKey, teamId);
        }

        return Contexts.interceptCall(ctx, serverCall, metadata, serverCallHandler);
    }
}
