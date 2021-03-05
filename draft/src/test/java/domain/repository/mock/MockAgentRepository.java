package domain.repository.mock;

import domain.entity.Athlete;
import domain.repository.AgentRepository;

import java.util.ArrayList;
import java.util.List;

public class MockAgentRepository implements AgentRepository {

    @Override
    public List<Athlete> getAllFreeAgents() {
        return new ArrayList<>(List.of(Athlete.builder().id("A").build(), Athlete.builder().id("B").build()));
    }

    @Override
    public Athlete getFreeAgentWithHighestProjection() {
        return null;
    }

}
