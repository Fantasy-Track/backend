package usecase.league;

import domain.repository.LeagueRepository;

import java.util.Random;

public class Base62IDGen {

    public static String generateUniqueLeagueId(LeagueRepository leagueRepository) {
        String id;
        do {
            id = generateBase62Id();
        } while (leagueRepository.getLeagueById(id) != null);
        return id;
    }

    private static String generateBase62Id() {
        String charArr = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            Random rand = new Random();
            int index = rand.nextInt(62);
            res.append(charArr.charAt(index));
        }
        return res.toString();
    }

}
