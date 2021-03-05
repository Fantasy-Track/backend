package fantasyapp.repository;

import com.google.inject.Inject;
import com.mongodb.client.MongoDatabase;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class CachedNameRepository extends DBNameRepository {

    private RLocalCachedMap<String, String> athleteCache;
    private RLocalCachedMap<String, String> teamCache;
    private RLocalCachedMap<String, String> ownerCache;
    private RLocalCachedMap<String, String> meetCache;

    @Inject
    public CachedNameRepository(MongoDatabase database, RedissonClient redis) {
        super(database);

        LocalCachedMapOptions<String, String> options = LocalCachedMapOptions.defaults();
        options.timeToLive(1, TimeUnit.HOURS);

        athleteCache = redis.getLocalCachedMap("athletes_cache", options);
        teamCache = redis.getLocalCachedMap("teams_cache", options);
        ownerCache = redis.getLocalCachedMap("owner_cache", options);
        meetCache = redis.getLocalCachedMap("meet_cache", options);
    }

    @Override
    public String getAthleteName(String athleteId) {
        return getAndCache(athleteId, athleteCache, super::getAthleteName);
    }

    @Override
    public String getMeetName(String meetId) {
        return getAndCache(meetId, meetCache, super::getMeetName);
    }

    @Override
    public String getOwnerName(String ownerId) {
        return getAndCache(ownerId, ownerCache, super::getOwnerName);
    }

    @Override
    public String getLeagueName(String leagueId) {
        return super.getLeagueName(leagueId);
    }

    @Override
    public String getTeamName(String teamId) {
        return getAndCache(teamId, teamCache, super::getTeamName);
    }

    @Override
    public void updateTeamName(String teamId, String teamName) {
        super.updateTeamName(teamId, teamName);
        teamCache.fastPut(teamId, teamName);
    }

    @Override
    public void updateOwnerName(String ownerId, String ownerName) {
        super.updateOwnerName(ownerId, ownerName);
        ownerCache.fastPut(ownerId, ownerName);
    }

    private String getAndCache(String id, RLocalCachedMap<String,String> cache, Function<String, String> getter) {
        String name = cache.get(id);
        if (name == null) {
            name = getter.apply(id);
            cache.fastPut(id, name);
        }
        return name;
    }

}
