package usecase.trading;

import com.google.inject.Inject;
import domain.entity.LeagueStatus;
import domain.entity.Team;
import domain.entity.TradeProposal;
import domain.exception.*;
import domain.repository.ContractRepository;
import domain.repository.LeagueRepository;
import domain.repository.TeamRepository;

import java.util.List;

public class TradeValidator {

    private ContractRepository contractRepository;
    private TeamRepository teamRepository;
    private LeagueRepository leagueRepository;

    @Inject
    public TradeValidator(ContractRepository contractRepository, TeamRepository teamRepository, LeagueRepository leagueRepository) {
        this.contractRepository = contractRepository;
        this.teamRepository = teamRepository;
        this.leagueRepository = leagueRepository;
    }

    public void validate(TradeProposal proposal) throws ApplicationException {
        validateTradeSize(proposal);
        validateLeagueStatus(proposal);
        validateTeams(proposal);
        validateAthletes(proposal);
//        validateTeamSize(proposal);
    }

    private void validateTradeSize(TradeProposal proposal) throws UnequalTrade, CannotMakeEmptyTrade {
        if (proposal.offeringAthletes.isEmpty() || proposal.receivingAthletes.isEmpty()) throw new CannotMakeEmptyTrade();
        if (proposal.offeringAthletes.size() != proposal.receivingAthletes.size()) {
            throw new UnequalTrade();
        }
    }

    private void validateLeagueStatus(TradeProposal proposal) throws CannotDoActionNow {
        if (leagueRepository.getLeagueById(proposal.leagueId).status != LeagueStatus.POST_DRAFT) {
            throw new CannotDoActionNow();
        }
    }

    private void validateAthletes(TradeProposal proposal) throws AthleteNotOnTeam {
        validateAthletesOwned(proposal.offeringAthletes, proposal.proposingTeamId);
        validateAthletesOwned(proposal.receivingAthletes, proposal.acceptingTeamId);
    }

//    private void validateTeamSize(TradeProposal proposalRequest) throws TeamFull {
//        int athletes = contractRepository.getTeamContracts(proposalRequest.initiatingTeamId).size();
//        athletes += proposalRequest.receivingAthletes.size() - proposalRequest.offeringAthletes.size();
//        if (athletes > leagueRepository.getLeagueById(proposalRequest.leagueId).leagueSettings.teamSize) {
//            throw new TeamFull();
//        }
//    }

    private void validateAthletesOwned(List<String> athleteIds, String teamId) throws AthleteNotOnTeam {
        for (String athleteId : athleteIds) {
            if (contractRepository.isAthleteOnTeam(athleteId, teamId)) continue; //maybe do it all in one db call?
            throw new AthleteNotOnTeam();
        }
    }

    private void validateTeams(TradeProposal proposal) throws TeamNotExists, TeamNotInLeague, CannotTradeWithSelf {
        if (proposal.acceptingTeamId.equals(proposal.proposingTeamId)) {
            throw new CannotTradeWithSelf();
        }
        validateTeam(proposal.acceptingTeamId, proposal.leagueId);
        validateTeam(proposal.proposingTeamId, proposal.leagueId);
    }

    private void validateTeam(String teamId, String leagueId) throws TeamNotExists, TeamNotInLeague {
        Team team = teamRepository.getTeamById(teamId);
        if (team == null) {
            throw new TeamNotExists();
        } else if (!team.leagueId.equals(leagueId)) {
            throw new TeamNotInLeague();
        }
    }


}
