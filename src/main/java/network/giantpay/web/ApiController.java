package network.giantpay.web;

import network.giantpay.dto.InfoDto;
import network.giantpay.dto.MasternodeDto;
import network.giantpay.dto.MasternodeInfoDto;
import network.giantpay.dto.RateDto;
import network.giantpay.error.ApiException;
import network.giantpay.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = "application/json; charset=UTF-8")
public class ApiController {

    @Autowired
    private MonitoringService monitoringService;

    @GetMapping("/info")
    public InfoDto getInfo() throws ApiException {
        return monitoringService.getInfo();
    }

    @GetMapping("/masternodes")
    public List<MasternodeDto> getMasternodes() throws ApiException {
        return monitoringService.getMasternodes();
    }

    @GetMapping("/masternodes/info")
    public MasternodeInfoDto getMasternodeInfo() throws ApiException {
        return monitoringService.getMasternodeInfo();
    }

    @GetMapping("/rates")
    public RateDto getRates() throws ApiException {
        return monitoringService.getRates();
    }
}
