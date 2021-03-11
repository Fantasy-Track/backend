package fantasyapp;

public class SharedEnvVars {
    public static final String REDIS_URI = System.getenv("REDIS_URI");
    public static final String REDIS_PASSWORD = System.getenv("REDIS_PASSWORD");
    public static final String MONGO_URI = System.getenv("MONGO_URI");
    public static final String MONGO_DB = System.getenv("MONGO_DB");
    public static final String GOOGLE_PROJECT_ID = System.getenv("GOOGLE_PROJECT_ID");
}
