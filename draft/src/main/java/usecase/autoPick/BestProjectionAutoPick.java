package usecase.autoPick;

import com.google.inject.Inject;
import domain.entity.Athlete;
import domain.repository.AgentRepository;
import usecase.ContractEditor;
import usecase.DraftContext;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BestProjectionAutoPick extends AutoPick {

    private AgentRepository agentRepository;

    @Inject
    public BestProjectionAutoPick(ContractEditor contractEditor, DraftContext context, AgentRepository agentRepository) {
        super(contractEditor, context);
        this.agentRepository = agentRepository;
    }

    @Override
    protected Athlete decidePick() {
        List<Athlete> athletes = agentRepository.getAllFreeAgents();
        athletes.sort(Comparator.comparingDouble(this::highestProjection).reversed());
        return athletes.get(0);
    }

    private double highestProjection(Athlete athlete) {
        Collection<Double> values = athlete.projections.values();
        if (values.isEmpty()) return 0;
        return Collections.max(values);
    }

}
