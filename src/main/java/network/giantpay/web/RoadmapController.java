package network.giantpay.web;

import lombok.AllArgsConstructor;
import network.giantpay.model.RoadMap;
import network.giantpay.service.RoadMapService;
import network.giantpay.service.RoadMapsService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Controller
@AllArgsConstructor
public class RoadmapController {

    private final RoadMapsService mapsService;

    private final RoadMapService mapService;

    @GetMapping("/roadmap")
    public String roadmap(final Map<String, Object> model) {
        model.put("roadMaps", this.mapsService.roadMapsByYear());
        return "roadmap";
    }

    @GetMapping("/roadmap/admin")
    public String adminPage(final Map<String, Object> model) {
        model.put("roadMaps", this.mapsService.roadMaps());
        return "roadmapadmin";
    }

    @PostMapping("/roadmap/save")
    public String save(@Valid final RoadMap roadMap,
                       final BindingResult result,
                       final Map<String, Object> model) {
        if (result.hasErrors()) {
            addErrors(result.getAllErrors(), model);
            model.put("roadMap", this.mapService.getOne(roadMap.getId()));
            return "roadmapEdit";
        } else {
            this.mapService.save(roadMap);
            return "redirect:/roadmap/admin";
        }
    }

    @GetMapping("/roadmap/save")
    public String toSave(final Map<String, Object> model) {
        model.put("roadMap", RoadMap.withCurrentDate());
        return "roadmapEdit";
    }

    @GetMapping("/roadmap/update/{id}")
    public String edit(@PathVariable(name = "id") final long id,
                       final Map<String, Object> model) {
        model.put("roadMap", this.mapService.getOne(id));
        return "roadmapEdit";
    }

    @GetMapping("/roadmap/delete/{id}")
    public String delete(@PathVariable(name = "id") final long delete) {
        this.mapService.delete(delete);
        return "redirect:/roadmap/admin";
    }

    private static void addErrors(final List<ObjectError> allErrors, final Map<String, Object> model) {
        model.put("errors", allErrors.stream().map(ObjectError::getDefaultMessage).collect(toList()));
    }
}
