package fantasyapp.grpc;

import com.fantasytrack.protos.TransactionGrpc;
import com.fantasytrack.protos.TransactionService;
import fantasyapp.grpc.serializer.TransactionSerializer;
import com.google.inject.Inject;
import com.google.protobuf.Empty;
import domain.exception.ApplicationException;
import io.grpc.stub.StreamObserver;
import usecase.dto.TradeProposalDTO;
import usecase.trading.*;

import java.util.List;

public class GRPCTransactionService extends TransactionGrpc.TransactionImplBase {

    private ClaimAthlete claimAthlete;
    private DropAthlete dropAthlete;
    private TradeProposer tradeProposer;
    private TradeProposalFetcher proposalFetcher;
    private TradeApprover tradeApprover;

    @Inject
    public GRPCTransactionService(ClaimAthlete claimAthlete, DropAthlete dropAthlete, TradeProposer tradeProposer, TradeProposalFetcher proposalFetcher, TradeApprover tradeApprover) {
        this.claimAthlete = claimAthlete;
        this.dropAthlete = dropAthlete;
        this.tradeProposer = tradeProposer;
        this.proposalFetcher = proposalFetcher;
        this.tradeApprover = tradeApprover;
    }

    private void callAndHandleError(ExceptionLambda func, StreamObserver<Empty> responseObserver) {
        try {
            func.call();
            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(GrpcError.makeError(e));
        }
    }

    @Override
    public void drop(TransactionService.DropRequest request, StreamObserver<Empty> responseObserver) {
        DropAthletesRequest dropRequest = DropAthletesRequest.builder()
                .leagueId(Authenticator.leagueKey.get())
                .teamId(Authenticator.teamKey.get())
                .athleteIds(request.getAthleteIdsList())
                .build();

        callAndHandleError(() -> dropAthlete.dropAthletes(dropRequest), responseObserver);
    }

    @Override
    public void claim(TransactionService.ClaimRequest request, StreamObserver<Empty> responseObserver) {
        ClaimAthleteRequest claimRequest = ClaimAthleteRequest.builder()
                .leagueId(Authenticator.leagueKey.get())
                .teamId(Authenticator.teamKey.get())
                .athleteId(request.getAthleteId())
                .build();

        callAndHandleError(() -> claimAthlete.claimAthlete(claimRequest), responseObserver);
    }

    @Override
    public void trade(TransactionService.TradeRequest request, StreamObserver<Empty> responseObserver) {
        TradeProposalRequest proposal = TradeProposalRequest.builder()
                .proposingTeamId(Authenticator.teamKey.get())
                .leagueId(Authenticator.leagueKey.get())
                .offeringAthletes(request.getOfferingAthletesList())
                .acceptingTeamId(request.getAcceptingTeamId())
                .receivingAthletes(request.getReceivingAthletesList())
                .build();
//        TradeProposalRequest proposal = TradeProposalRequest.builder()
//                .acceptingTeamId(Authenticator.teamKey.get())
//                .leagueId(Authenticator.leagueKey.get())
//                .receivingAthletes(request.getOfferingAthletesList())
//                .proposingTeamId(request.getAcceptingTeamId())
//                .offeringAthletes(request.getReceivingAthletesList())
//                .build();

        callAndHandleError(() -> tradeProposer.proposeTrade(proposal), responseObserver);
    }

    @Override
    public void getTrade(TransactionService.TradeQuery request, StreamObserver<TransactionService.TradeProposal> responseObserver) {
        try {
            TradeProposalDTO proposalDTO = proposalFetcher.getTradeById(request.getId());
            responseObserver.onNext(TransactionSerializer.serializeTradeProposal(proposalDTO));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(GrpcError.makeError(e));
        }
    }

    @Override
    public void getTradesWithTeam(TransactionService.TradeTeamQuery request, StreamObserver<TransactionService.TradesResponse> responseObserver) {
        List<TradeProposalDTO> proposals = proposalFetcher.getTradesWithTeam(request.getTeamId());
        responseObserver.onNext(TransactionSerializer.serializeTradeResponse(proposals));
        responseObserver.onCompleted();
    }

    @Override
    public void getAcceptedTradesInLeague(TransactionService.TradeLeagueQuery request, StreamObserver<TransactionService.TradesResponse> responseObserver) {
        List<TradeProposalDTO> proposals = proposalFetcher.getAcceptedTradesInLeague(request.getLeagueId());
        responseObserver.onNext(TransactionSerializer.serializeTradeResponse(proposals));
        responseObserver.onCompleted();
    }

    @Override
    public void acceptTrade(TransactionService.TradeProposalId request, StreamObserver<Empty> responseObserver) {
        callAndHandleError(() -> tradeApprover.acceptTrade(request.getTradeId(), Authenticator.teamKey.get()), responseObserver);
    }

    @Override
    public void cancelTrade(TransactionService.TradeProposalId request, StreamObserver<Empty> responseObserver) {
        callAndHandleError(() -> tradeApprover.cancelTrade(request.getTradeId(), Authenticator.teamKey.get()), responseObserver);
    }

    @Override
    public void rejectTrade(TransactionService.TradeProposalId request, StreamObserver<Empty> responseObserver) {
        callAndHandleError(() -> tradeApprover.rejectTrade(request.getTradeId(), Authenticator.teamKey.get()), responseObserver);
    }

    public interface ExceptionLambda {
        void call() throws ApplicationException;
    }


}
