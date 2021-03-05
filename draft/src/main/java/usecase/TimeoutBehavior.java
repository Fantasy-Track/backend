package usecase;

import com.mongodb.lang.NonNull;
import domain.entity.Turn;

public interface TimeoutBehavior {

    void handleTimeout(@NonNull Turn turn);

}
