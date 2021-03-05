package domain.repository;

import domain.repository.mock.MockPickOrderRepository;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class MockPickOrderRepositoryTest {

    @Test
    public void testGetPickOrder() {
        PickOrderRepository pickOrderRepository = new MockPickOrderRepository();
        assertNull(pickOrderRepository.getPickOrder());
    }

    @Test
    public void testSavePickOrder() {
        List<String> list = List.of("1", "2");
        PickOrderRepository pickOrderRepository = new MockPickOrderRepository();
        pickOrderRepository.savePickOrder(list);

        assertEquals(pickOrderRepository.getPickOrder(), list);
    }
}