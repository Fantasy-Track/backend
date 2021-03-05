package fantasyapp.batchAssignments;

import fantasyapp.ExecuteJobs;
import fantasyapp.job.TradeProcessJob;
import com.google.inject.Inject;
import domain.entity.TradeProposal;
import domain.entity.TradeStatus;
import domain.repository.TradeRepository;
import usecase.tradeProcessor.TradeProcessor;

import java.util.List;

// process all trades that have been approved
public class ProcessTradesBatch {

    private TradeRepository tradeRepository;
    private TradeProcessor tradeProcessor;

    @Inject
    public ProcessTradesBatch(TradeRepository tradeRepository, TradeProcessor tradeProcessor) {
        this.tradeRepository = tradeRepository;
        this.tradeProcessor = tradeProcessor;
    }

    public void processTrades() {
        List<TradeProposal> trades = tradeRepository.getTradesWithStatus(TradeStatus.ACCEPTED_WAITING);
        for (TradeProposal trade : trades) {
            TradeProcessJob job = new TradeProcessJob(trade, tradeProcessor);
            ExecuteJobs.scheduler.submit(job);
        }
    }

}
