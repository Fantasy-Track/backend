package domain.repository.mock;

import domain.repository.PickOrderRepository;

import java.util.ArrayList;
import java.util.List;

public class MockStaticPickOrderRepository implements PickOrderRepository {

    @Override
    public List<String> getPickOrder() {
        return new ArrayList<>(List.of("1", "2"));
    }

    @Override
    public void savePickOrder(List<String> order) {

    }
}
