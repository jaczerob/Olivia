package dev.jaczerob.olivia.ttr;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "toontown", url = "https://www.toontownrewritten.com/api")
public interface TTRAPIClient {
    @RequestMapping("/population")
    PopulationResponse getPopulation();
}
