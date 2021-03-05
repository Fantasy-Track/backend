package fantasyapp.job;

import domain.entity.TradeProposal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import usecase.tradeProcessor.TradeProcessor;

import java.util.Arrays;

public class TradeProcessJob extends Thread {

    private Logger logger = LoggerFactory.getLogger(TradeProcessJob.class);

    private TradeProposal trade;
    private TradeProcessor tradeProcessor;

    public TradeProcessJob(TradeProposal trade, TradeProcessor tradeProcessor) {
        this.trade = trade;
        this.tradeProcessor = tradeProcessor;
    }

    @Override
    public void run() {
        try {
            tradeProcessor.processTrade(trade);
        } catch (Exception e) {
            logger.error("Error when processing trade: " + Arrays.toString(e.getStackTrace()));
        }
    }
}
