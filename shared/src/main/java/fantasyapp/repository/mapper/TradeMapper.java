package fantasyapp.repository.mapper;

import fantasyapp.dao.TradeDAO;
import domain.entity.TradeProposal;

public class TradeMapper {

    public static TradeDAO writeDAO(TradeProposal proposal) {
        TradeDAO dao = new TradeDAO();
        dao.setId(proposal.id);
        dao.setProposingTeamId(proposal.proposingTeamId);
        dao.setAcceptingTeamId(proposal.acceptingTeamId);
        dao.setLeagueId(proposal.leagueId);
        dao.setReceivingAthletes(proposal.receivingAthletes);
        dao.setOfferingAthletes(proposal.offeringAthletes);
        dao.setDate(proposal.date);
        dao.setStatus(proposal.status);
        return dao;
    }

    public static TradeProposal writeEntity(TradeDAO dao) {
        return TradeProposal.builder()
                .id(dao.getId())
                .status(dao.getStatus())
                .leagueId(dao.getLeagueId())
                .proposingTeamId(dao.getProposingTeamId())
                .acceptingTeamId(dao.getAcceptingTeamId())
                .receivingAthletes(dao.getReceivingAthletes())
                .offeringAthletes(dao.getOfferingAthletes())
                .date(dao.getDate())
                .build();
    }

}
