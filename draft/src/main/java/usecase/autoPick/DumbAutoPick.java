package usecase.autoPick;

import com.google.inject.Inject;
import domain.entity.Athlete;
import domain.repository.AgentRepository;
import usecase.ContractEditor;
import usecase.DraftContext;

import java.util.List;

public class DumbAutoPick extends AutoPick {

    private AgentRepository agentRepository;

    @Inject
    public DumbAutoPick(ContractEditor contractEditor, AgentRepository agentRepository, DraftContext context) {
        super(contractEditor, context);
        this.agentRepository = agentRepository;
    }

    @Override
    protected Athlete decidePick() {
        List<Athlete> agents = agentRepository.getAllFreeAgents();
        return agents.get(0);
    }

}
