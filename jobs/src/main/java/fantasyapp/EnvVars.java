package fantasyapp;

public class EnvVars {
    public static final int PORT = Integer.parseInt(System.getenv("PORT"));
    public static final int SEASON_YEAR = Integer.parseInt(System.getenv("SEASON_YEAR"));
}
