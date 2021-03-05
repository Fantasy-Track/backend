package usecase.postProcess;

import fantasyapp.repository.Events;
import com.google.inject.Inject;
import domain.entity.Contract;
import domain.entity.Meet;
import domain.entity.ScoredResult;
import domain.repository.ResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class ResultsUploader {

    private Logger logger = LoggerFactory.getLogger(ResultsUploader.class);

    private ResultRepository resultRepository;

    @Inject
    public ResultsUploader(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public void uploadAllResults(List<ScoredResult> results, Meet meet) {
        logger.info("Uploading results");
        Map<String, Contract> contracts = meet.savedContracts.stream()
                .collect(Collectors.toMap(c -> getContractLookupString(c.athleteId, c.eventId), c -> c));
        uploadAllResults(results, contracts);
        logger.info("Finished uploading results");
    }

    private void uploadAllResults(List<ScoredResult> results, Map<String, Contract> contractLookup) {
        Map<Contract, List<ScoredResult>> contractResultCandidates = new HashMap<>();
        uploadResultsOrPopulateCandidates(results, contractLookup, contractResultCandidates);
        uploadContractResultCandidates(contractResultCandidates);
    }

    private void uploadResultsOrPopulateCandidates(List<ScoredResult> results, Map<String, Contract> contractLookup, Map<Contract, List<ScoredResult>> contractResultCandidates) {
        for (ScoredResult result : results) {
            String lookupString = getContractLookupString(result.result.athleteId, result.result.eventId);
            Contract contract = contractLookup.get(lookupString);
            if (contract == null) {
                resultRepository.addResult(result);
                continue;
            }
            contractResultCandidates.putIfAbsent(contract, new ArrayList<>());
            contractResultCandidates.get(contract).add(result);
        }
    }

    private void uploadContractResultCandidates(Map<Contract, List<ScoredResult>> contractResultCandidates) {
        contractResultCandidates.forEach((contract, candidates) -> {
            ScoredResult bestResult = candidates.stream().max(Comparator.comparingDouble(o -> o.fantasyPoints)).get();
            for (ScoredResult candidate : candidates) {
                if (candidate.equals(bestResult)) {
                    resultRepository.addResult(candidate, contract.teamId);
                } else {
                    resultRepository.addResult(candidate);
                }
            }
        });
    }

    private String getContractLookupString(String athleteId, String eventId) {
        return athleteId + "_" + Events.eventToCategory(eventId);
    }

}
