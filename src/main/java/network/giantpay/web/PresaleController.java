package network.giantpay.web;

import network.giantpay.dto.InfoDto;
import network.giantpay.model.PresaleRequest;
import network.giantpay.repository.PresaleRequestRepository;
import network.giantpay.service.MonitoringService;
import network.giantpay.service.PresaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class PresaleController {

    @Autowired
    private MonitoringService monitoringService;
    @Autowired
    private PresaleService presaleService;
    @Autowired
    private PresaleRequestRepository presaleRequestRepository;

    @GetMapping("/presale")
    public String presale(Map<String, Object> model) {
        InfoDto info = monitoringService.getInfo();
        model.put("masternodes", info.getMasternodes());
        model.put("masternodeRoi", info.getMasternodeRoi());
        model.put("masternodeRoiDays", info.getMasternodeRoiDays());
        model.put("dailyIncome", monitoringService.getDailyIncome());
        model.put("enabledMasternodes", monitoringService.getEnabledMasternodes());
        return "presale";
    }

    @GetMapping("/presale/status")
    public String status(@RequestParam("id") String id, Map<String, Object> model) {
        PresaleRequest presaleRequest = presaleRequestRepository.findByGuid(id);
        model.put("presale", presaleRequest);

        return "status";
    }
}
