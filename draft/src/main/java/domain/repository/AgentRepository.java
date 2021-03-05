package domain.repository;

import domain.entity.Athlete;

import java.util.List;

public interface AgentRepository {

    List<Athlete> getAllFreeAgents();

    Athlete getFreeAgentWithHighestProjection();
}
