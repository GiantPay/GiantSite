package network.giantpay.web;

import lombok.AllArgsConstructor;
import network.giantpay.dto.InfoDto;
import network.giantpay.dto.MasternodeDto;
import network.giantpay.dto.MasternodeInfoDto;
import network.giantpay.dto.RateDto;
import network.giantpay.dto.trello.Board;
import network.giantpay.error.ApiException;
import network.giantpay.service.MonitoringService;
import network.giantpay.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api", produces = "application/json; charset=UTF-8")
@AllArgsConstructor
public class ApiController {

    private final StorageService storageService;

    private final MonitoringService monitoringService;

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

    @PostMapping("/upload")
    public Map<String, Object> uploadImage(@RequestParam("image") MultipartFile file,
                                           @RequestParam("username") String username,
                                           @RequestParam("password") String password) {
        return storageService.uploadImage(username, password, file);
    }

    @GetMapping("/workflow")
    public Board getTrello() {
        return monitoringService.getTrelloBoard();
    }
}
