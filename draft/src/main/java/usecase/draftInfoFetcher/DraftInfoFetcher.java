package usecase.draftInfoFetcher;

import com.google.inject.Inject;
import domain.repository.PickOrderRepository;
import domain.repository.SettingsRepository;
import usecase.dto.DraftInfoDTO;
import usecase.port.DraftInfoPort;

import java.util.List;

public class DraftInfoFetcher implements DraftInfoPort {

    private PickOrderRepository pickOrderRepository;
    private SettingsRepository settingsRepository;

    @Inject
    public DraftInfoFetcher(PickOrderRepository pickOrderRepository, SettingsRepository settingsRepository) {
        this.pickOrderRepository = pickOrderRepository;
        this.settingsRepository = settingsRepository;
    }

    @Override
    public DraftInfoDTO getInfo() {
        List<String> pickOrder = pickOrderRepository.getPickOrder();

        DraftInfoDTO dto = new DraftInfoDTO();
        dto.setPickingOrder(pickOrder);
        dto.setRounds(settingsRepository.getRounds());
        dto.setStartTime(settingsRepository.getStartTime());

        return dto;
    }

}
