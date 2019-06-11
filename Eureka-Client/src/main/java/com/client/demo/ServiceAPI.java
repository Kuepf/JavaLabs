package com.client.demo;

import com.common.CreatorDTO;
import com.common.WeaponDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ServiceAPI {
    @Autowired
    ServiceClient serviceClient;
    @GetMapping("/creators")
    public List<CreatorDTO> creators() {
        return serviceClient.GetCreators();
    }

    @GetMapping("/creators/{creatorId}")
    public  CreatorDTO creator(@PathVariable Integer creatorId){return  serviceClient.getCreator(creatorId);}

    @PostMapping("/creators")
    public  CreatorDTO newCreator(@RequestBody CreatorDTO creatorDTO){return  serviceClient.createCreator(creatorDTO);}

    @GetMapping("/weapons")
    public  List<WeaponDTO> weapons(){return  serviceClient.getWeapons();}

    @DeleteMapping("/weapons/{weaponId}")
    public ResponseEntity<?> deleteWeapon(@PathVariable Integer weaponId){ return serviceClient.deleteWeapon(weaponId);}

    @PostMapping("/weapons")
    public  WeaponDTO newWeapon(@RequestBody WeaponDTO weaponDTO){
        return  serviceClient.createWeapon(weaponDTO);
    }

    @PutMapping("/weapons/{weaponId}")
    public  WeaponDTO updateWeapon(@RequestBody WeaponDTO weaponDTO, @PathVariable Integer weaponId){
        return  serviceClient.updateWeapon(weaponDTO, weaponId);
    }

}
