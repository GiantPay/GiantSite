package network.giantpay.service;

import lombok.AllArgsConstructor;
import network.giantpay.model.RoadMap;
import network.giantpay.repository.RoadMapRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoadMapService {

    private final RoadMapRepository repository;

    public void delete(final long id) {
        this.repository.deleteById(id);
    }

    public RoadMap getOne(final long id) {
        return this.repository.findById(id).orElseThrow(NullPointerException::new);
    }

    public RoadMap save(final RoadMap roadMap) {
        return this.repository.save(roadMap);
    }
}
