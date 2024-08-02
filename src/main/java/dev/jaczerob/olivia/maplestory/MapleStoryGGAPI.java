package dev.jaczerob.olivia.maplestory;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "maplestorygg", url = "https://api.maplestory.gg/v2/public")
public interface MapleStoryGGAPI {
    @RequestMapping(method = RequestMethod.GET, value = "/character/gms/{character}", consumes = "application/json", headers = {"Content-Type", "application/json"})
    CharacterDataResponse getCharacterData(@PathVariable("character") String character);
}
