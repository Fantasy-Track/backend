package usecase.port;

public interface EventResultHandler {

    void onSuccess();

    void onError(Exception exception);

}
