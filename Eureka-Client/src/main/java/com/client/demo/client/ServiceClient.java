package com.client.demo.client;

import com.common.CreatorDTO;
import  com.common.WeaponDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient (serviceId = "greetings-service")
public interface ServiceClient {
    @RequestMapping(method = RequestMethod.GET, value = "/creators")
    List<CreatorDTO> GetCreators();

    @RequestMapping(method = RequestMethod.GET, value = "/creators/{creatorId}")
    CreatorDTO getCreator(@PathVariable Integer creatorId);

    @RequestMapping(method = RequestMethod.POST, value = "/creators")
    CreatorDTO createCreator(@RequestBody CreatorDTO creatorDTO);

    @RequestMapping(method = RequestMethod.GET, value = "/weapons")
    List<WeaponDTO> getWeapons();

    @RequestMapping(method = RequestMethod.DELETE, value = "/weapons/{weaponId}")
    ResponseEntity<?> deleteWeapon(@PathVariable Integer weaponId);

    @RequestMapping(method = RequestMethod.POST, value = "/weapons")
    WeaponDTO createWeapon(@RequestBody WeaponDTO weaponDTO);

    @RequestMapping(method = RequestMethod.PUT, value = "/weapons/{weaponId}")
    WeaponDTO updateWeapon(@RequestBody WeaponDTO weaponDTO, @PathVariable Integer weaponId);

}
