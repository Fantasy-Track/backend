package fantasyapp.grpc;

import com.fantasytrack.protos.JobsGrpc;
import com.fantasytrack.protos.JobsService;
import com.google.inject.Inject;
import domain.exception.ServerFetchIssue;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import usecase.league.LeaguePointsUpdater;
import usecase.league.RemoteIndexer;

import java.time.Instant;
import java.util.List;

@SuppressWarnings("ALL")
public class GrpcJobsClient implements RemoteIndexer, LeaguePointsUpdater {

    private JobsGrpc.JobsBlockingStub jobsClient;

    @Inject
    public GrpcJobsClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext().disableRetry());
    }

    public GrpcJobsClient(ManagedChannelBuilder<?> channelBuilder) {
        ManagedChannel channel = channelBuilder.build();
        jobsClient = JobsGrpc.newBlockingStub(channel);
    }

    @Override
    public List<String> indexAthletesInSchool(String schoolId) throws Exception {
        try {
            JobsService.IndexAthletesResponse res = jobsClient.indexAthletesInSchool(JobsService.IndexAthletesRequest.newBuilder()
                    .setSchoolId(schoolId)
                    .build());
            return res.getAthleteIdsList();
        } catch (Exception e) {
            throw new ServerFetchIssue();
        }
    }

    @Override
    public void indexMeets(String schoolId, String leagueId, Instant afterDate) throws Exception {
        try {
            jobsClient.indexMeets(JobsService.IndexMeetsRequest.newBuilder()
                    .setLeagueId(leagueId)
                    .setSchoolId(schoolId)
                    .setDraftTime(TimeSerializer.serializeTime(afterDate))
                    .build());
        } catch (Exception e) {
            throw new ServerFetchIssue();
        }
    }

    @Override
    public void rescoreMeet(String meetId) throws Exception {
        try {
            jobsClient.rescoreMeet(JobsService.RescoreMeetRequest.newBuilder()
                .setMeetId(meetId)
                .build());
        } catch (Exception e) {
            throw new ServerFetchIssue();
        }
    }

    @Override
    public void updatePoints(String leagueId) throws Exception {
        try {
            jobsClient.updatePoints(JobsService.UpdatePointsRequest.newBuilder()
                    .setLeagueId(leagueId)
                    .build());
        } catch (Exception e){
            throw new ServerFetchIssue();
        }
    }
}
