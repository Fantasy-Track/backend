package util;

import domain.exception.ApplicationException;

public abstract class DistributedLock {

    public static final String CONTRACT = "contract";
    public static final String ROSTER = "roster"; // not sure if necessary as only one person has access anyway
    public static final String TEAM = "team";
    public static final String SCHED_DRAFTS = "scheduledDrafts";
    public static final String TRADE_PROCESSING = "tradeProcessing";

    public static final String ALL_LEAGUES = "allLeagues";

    public abstract void lockAndRun(String lockName, String leagueId, VoidFunc function) throws ApplicationException;

}
