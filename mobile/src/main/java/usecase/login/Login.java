package usecase.login;

import com.google.inject.Inject;
import domain.entity.Owner;
import domain.exception.AccountNotFound;
import domain.repository.OwnerRepository;
import domain.repository.TeamRepository;
import usecase.notification.NotificationFactory;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Login {

    private OwnerRepository ownerRepository;
    private TeamRepository teamRepository;

    @Inject
    public Login(OwnerRepository ownerRepository, TeamRepository teamRepository) {
        this.ownerRepository = ownerRepository;
        this.teamRepository = teamRepository;
    }

    public void login(String ownerId, String messagingToken) throws AccountNotFound {
        Owner owner = ownerRepository.getOwnerById(ownerId);
        if (owner == null) throw new AccountNotFound();
        var teams = teamRepository.getOwnedTeams(ownerId);
        var topics = teams.stream()
            .flatMap(team -> Stream.of(team.id, team.leagueId))
            .collect(Collectors.toList());
        NotificationFactory.handler.registerDeviceForNotifications(messagingToken, topics);
    }

}
