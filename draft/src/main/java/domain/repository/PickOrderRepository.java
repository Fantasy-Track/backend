package domain.repository;

import java.util.List;

public interface PickOrderRepository {

    List<String> getPickOrder();

    void savePickOrder(List<String> order);

}
