package network.giantpay.web;

import network.giantpay.dto.*;
import network.giantpay.error.ApiException;
import network.giantpay.service.MonitoringService;
import network.giantpay.service.PresaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = "application/json; charset=UTF-8")
public class ApiController {

    @Autowired
    private PresaleService presaleService;
    @Autowired
    private MonitoringService monitoringService;

    @PostMapping("/presale")
    public PresaleResponseDto presaleRequest(@RequestBody PresaleRequestDto presaleRequest) throws ApiException {
        return presaleService.createRequest(presaleRequest);
    }

    @GetMapping("/presale")
    public PresaleStatusResponseDto presaleStatus(@RequestParam("id") String guid) throws ApiException {
        return presaleService.getStatus(guid);
    }

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
