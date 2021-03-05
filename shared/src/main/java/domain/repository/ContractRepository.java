package domain.repository;

import domain.entity.Contract;

import java.util.List;

public interface ContractRepository {

    List <Contract> getLeagueContracts(String leagueId);

    void signContract(Contract contract);

    List<Contract> getTeamContracts(String teamId);

    boolean isAthleteOnTeam(String athleteId, String teamId);

    boolean isAthleteFreeAgent(String athleteId, String leagueId);

    void deleteContract(String athleteId, String teamId);

}
