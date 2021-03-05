package fantasyapp;

public class RedisTest {

    public static void main(String[] args) {
        StaticDB.redis.getSet("ff").add("key");
    }

}
