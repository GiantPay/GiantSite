package network.giantpay.service;

import network.giantpay.repository.UtmSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service("marketingService")
public class MarketingService {

    @Autowired
    private UtmSourceRepository utmSourceRepository;

    public String getDiscordUrl() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String utmSource = ServletRequestUtils.getStringParameter(request, "utm_source", "");
        return utmSourceRepository.findBySource(utmSource);
    }
}
