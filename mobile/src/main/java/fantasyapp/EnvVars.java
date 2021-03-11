package fantasyapp;

public class EnvVars {

    public static final int PORT = Integer.parseInt(System.getenv("PORT"));
    public static final String JOBS_IP = System.getenv("JOBS_IP");
    public static final int JOBS_PORT = Integer.parseInt(System.getenv("JOBS_PORT"));

}
