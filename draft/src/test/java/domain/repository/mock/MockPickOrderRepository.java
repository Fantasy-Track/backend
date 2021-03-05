package domain.repository.mock;

import domain.repository.PickOrderRepository;

import java.util.List;

public class MockPickOrderRepository implements PickOrderRepository {

    private List<String> pickOrder = null;

    @Override
    public List<String> getPickOrder() {
        return pickOrder;
    }

    @Override
    public void savePickOrder(List<String> order) {
        pickOrder = order;
    }

}
