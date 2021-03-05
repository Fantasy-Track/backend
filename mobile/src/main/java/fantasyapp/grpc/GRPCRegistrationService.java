package fantasyapp.grpc;

import com.fantasytrack.protos.RegistrationGrpc;
import com.fantasytrack.protos.RegistrationService;
import com.fantasytrack.protos.RegistrationService.CreateAccountRequest;
import com.fantasytrack.protos.RegistrationService.CreateTeamResponse;
import com.fantasytrack.protos.RegistrationService.LoginRequest;
import com.google.inject.Inject;
import com.google.protobuf.Empty;
import domain.exception.AccountNotFound;
import domain.exception.ApplicationException;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.createOwner.CreateOwner;
import usecase.createOwner.CreateOwnerRequest;
import usecase.createTeam.CreateTeam;
import usecase.createTeam.CreateTeamRequest;
import usecase.createTeam.JoinLeagueValidator;
import usecase.login.Login;

public class GRPCRegistrationService extends RegistrationGrpc.RegistrationImplBase {

    private Logger logger = LoggerFactory.getLogger(GRPCRegistrationService.class);

    private final CreateTeam teamCreator;
    private final CreateOwner createOwner;
    private final Login login;
    private JoinLeagueValidator joinLeagueValidator;

    @Inject
    public GRPCRegistrationService(CreateTeam teamCreator, CreateOwner createOwner, Login login, JoinLeagueValidator joinLeagueValidator) {
        this.teamCreator = teamCreator;
        this.createOwner = createOwner;
        this.login = login;
        this.joinLeagueValidator = joinLeagueValidator;
    }

    @Override
    public void createTeam(RegistrationService.CreateTeamRequest request, StreamObserver<CreateTeamResponse> responseObserver) {
        logger.info("Create team request received");
        try {
            CreateTeamRequest createRequest = CreateTeamRequest.builder()
                    .teamName(request.getTeamName())
                    .leagueId(request.getLeagueId())
                    .ownerId(Authenticator.ownerKey.get())
                    .build();
            String teamId = teamCreator.createTeam(createRequest);
            responseObserver.onNext(CreateTeamResponse.newBuilder().setTeamId(teamId).setLeagueId(request.getLeagueId()).build());
            responseObserver.onCompleted();
            logger.info("Successfully created team and notified client");
        } catch (Throwable throwable) {
            responseObserver.onError(GrpcError.makeError(throwable));
        }
    }

    @Override
    public void createAccount(CreateAccountRequest request, StreamObserver<Empty> responseObserver) {
        logger.info("User creating account with name: " + request.getOwnerName());
        try {
            CreateOwnerRequest createRequest = CreateOwnerRequest.builder()
                    .id(Authenticator.ownerKey.get())
                    .name(request.getOwnerName()).build();
            createOwner.create(createRequest);
            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
            logger.info("User account created successfully");
        } catch (Throwable throwable) {
            responseObserver.onError(GrpcError.makeError(throwable));
        }
    }

    @Override
    public void loginOwner(LoginRequest request, StreamObserver<Empty> responseObserver) {
        logger.info("User logging in");
        try {
            login.login(Authenticator.ownerKey.get());
            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
            logger.info("User successfully logged in");
        } catch (AccountNotFound throwable) {
            logger.warn("Could not login user");
            responseObserver.onError(GrpcError.makeError(throwable));
        }
    }

    @Override
    public void canJoinLeague(RegistrationService.CanJoinRequest request, StreamObserver<RegistrationService.CanJoinResponse> responseObserver) {
        try {
            logger.info("Checking can join league");
            joinLeagueValidator.validateOwnerJoiningLeague(Authenticator.ownerKey.get(), request.getLeagueId());
            responseObserver.onNext(RegistrationService.CanJoinResponse.newBuilder().setCanJoin(true).build());
            logger.info("Yes, can join league");
        } catch (ApplicationException e) {
            logger.info("No, cannot join league", e);
            responseObserver.onNext(RegistrationService.CanJoinResponse.newBuilder().setCanJoin(false).setMessage(e.getMessage()).build());
        }
        responseObserver.onCompleted();
    }

}
