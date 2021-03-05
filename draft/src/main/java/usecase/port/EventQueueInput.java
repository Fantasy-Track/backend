package usecase.port;

import usecase.ContractRequest;

public interface EventQueueInput {

    void queueDraftEvent(ContractRequest contractRequest, EventResultHandler resultHandler);

}
