package usecase.indexing;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WebUtil {

    private static Logger logger = LoggerFactory.getLogger(WebUtil.class);
    private static final ExecutorService webPageExecutor = Executors.newFixedThreadPool(1);
    private static long lastTime;

    public static Connection queueWebPage(String url) throws ExecutionException, InterruptedException {
        Future<Connection> future = webPageExecutor.submit(() -> getJSoup(url));
        return future.get();
    }

    private static Connection getJSoup(String url) {
        long currTime = System.currentTimeMillis();
        long delta = currTime - lastTime;
        if (delta < 300) {
            try {
                Thread.sleep(300 - delta);
            } catch (InterruptedException e) {
                logger.error("Error sleeping to get web page", e);
            }
        }
        lastTime = System.currentTimeMillis();
        return Jsoup.connect(url).maxBodySize(0); //unbounded page size
    }

}
