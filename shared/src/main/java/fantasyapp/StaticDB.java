package fantasyapp;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class StaticDB {

    public static MongoDatabase database = initMongoDatabase();
    public static RedissonClient redis = initRedisDatabase();

    static {
        setupFirebase();
    }

    public static RedissonClient initRedisDatabase() {
        String redisUri = System.getenv("REDIS_URI");
        String redisPassword = System.getenv("REDIS_PASSWORD");

        Config config = new Config();
        config.useSingleServer().setConnectionMinimumIdleSize(3).setConnectionPoolSize(4).setAddress(redisUri).setPassword(redisPassword.isEmpty() ? null : redisPassword);
        return Redisson.create(config);
    }

    public static MongoDatabase initMongoDatabase() {
        String mongoUri = System.getenv("MONGO_URI");
        String mongoDb = System.getenv("MONGO_DB");

        Logger mongoLogger = Logger.getLogger("org.mongodb");
        mongoLogger.setLevel(Level.WARNING);

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(mongoUri))
                .codecRegistry(pojoCodecRegistry)
                .build();

        return MongoClients.create(settings).getDatabase(mongoDb);
    }

    public static void setupFirebase() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setProjectId("fantasytrackhs")
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .setDatabaseUrl("https://fantasytrackhs.firebaseio.com")
                .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
