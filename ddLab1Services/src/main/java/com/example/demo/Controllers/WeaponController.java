package com.example.demo.Controllers;

import com.example.demo.Assemblers.WeaponResourcesAssembler;
import com.example.demo.Assemblers.CreatorResourcesAssembler;
import com.example.demo.Exceptions.CreatorNotFoundException;
import com.example.demo.Exceptions.VechicleNotFoundException;
import com.example.demo.Models.Creator;
import com.example.demo.Models.Weapon;
import com.example.demo.Services.CreatorService;
import com.example.demo.Services.WeaponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/weapons")
public class WeaponController {
    @Autowired
    private WeaponService WeaponService;
    @Autowired
    CreatorService myCreatorService;
    private final WeaponResourcesAssembler assembler;
    private final CreatorResourcesAssembler directorAssembler;

    public WeaponController(WeaponResourcesAssembler assembler, CreatorResourcesAssembler creatorResourcesAssembler){
        this.assembler = assembler;
        this.directorAssembler = creatorResourcesAssembler;
    }
    @GetMapping
    public Resources<Resource<Weapon>> getWeapons(){
        List<Resource<Weapon>> list = WeaponService.getAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(
                list,
                linkTo(methodOn(WeaponController.class).getWeapons()).withSelfRel()
        );
    }
    @GetMapping("/{weaponId}")
    public ResponseEntity<ResourceSupport> getWeapon(@PathVariable Integer weaponId){
        Optional<Weapon> weapon = WeaponService.getObjectById(weaponId);
        return weapon.isPresent() ?
                ok(assembler.toResource(weapon.get())) :
                ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new VndErrors.VndError("Weapon not found", new VechicleNotFoundException(weaponId).getMessage()));
    }
    @PostMapping
    public ResponseEntity<?> createWeapon(@RequestBody Weapon newWeapon) throws URISyntaxException {
        Resource<Weapon> resource = assembler.toResource(WeaponService.saveObject(newWeapon));
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);    }
    @PutMapping("/{weaponId}")
    public ResponseEntity<?> updateWeapon(@RequestBody Weapon updatedMovie, @PathVariable(name = "weaponId") Integer weaponId) throws URISyntaxException {
        Weapon updatedObj = WeaponService.getObjectById(weaponId)
                .map(weapon -> {
                    weapon.setName(updatedMovie.getName());
                    weapon.setCreator(updatedMovie.getCreator());
                    return WeaponService.saveObject(weapon);
                })
                .orElseGet(()->{
                    updatedMovie.setId(weaponId);
                    return WeaponService.saveObject(updatedMovie);
                });

        Resource<Weapon> resource = assembler.toResource(updatedObj);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }
    @DeleteMapping("/{weaponId}")
    public ResponseEntity<?> deleteWeapon(@PathVariable Integer weaponId){
        try{
            changeWeaponCreator(weaponId, -2);
            WeaponService.deleteObject(weaponId);
        }
        catch(VechicleNotFoundException ex){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new VndErrors.VndError("Weapon not found", new VechicleNotFoundException(weaponId).getMessage()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{weaponId}/creator")
    public ResponseEntity<ResourceSupport> getWeaponOfCreator(@PathVariable Integer weaponId){
        Weapon weapon = WeaponService.getObjectById(weaponId)
                .orElseThrow(() -> new VechicleNotFoundException(weaponId));
        if(weapon.getCreator() == null)
            return ResponseEntity.noContent().build();
        Optional<Creator> creator = myCreatorService.getObjectById(weapon.getCreator().getId());
        return creator.isPresent() ?
                ok(directorAssembler.toResource(creator.get())) :
                ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new VndErrors.VndError(
                                "Creator not found. Remove this creator weapon Id!!!",
                                new CreatorNotFoundException(weapon.getCreator().getId()).getMessage())
                        );
    }

    @PostMapping("/{weaponId}/creator/{newCreatorId}")
    public Resources<Resource<Weapon>> changeWeaponCreator(@PathVariable Integer weaponId, @PathVariable Integer newCreatorId){
        Weapon weapon = WeaponService.getObjectById(weaponId)
                .orElseThrow(() -> new VechicleNotFoundException(weaponId));
        removeWeaponFromCreator(weapon);
        return addWeaponToCreator(weapon, newCreatorId);
    }

    private Resources<Resource<Weapon>> getWeaponsOfCreator(Integer weaponId){
        Optional<Creator> creator = myCreatorService.getObjectById(weaponId);
        List<Resource<Weapon>> list = creator.get().getWeapons()
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(
                list,
                linkTo(methodOn(CreatorController.class).getCreator(weaponId)).withSelfRel(),
                linkTo(methodOn(WeaponController.class).getWeapons()).withSelfRel()
        );
    }

    private void removeWeaponFromCreator(Weapon weapon){
        if(weapon.getCreator() != null){
            Creator weaponCreator = myCreatorService.getObjectById(weapon.getCreator().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Delete this weapon ID"));
            weaponCreator.removeWeapons(weapon);
            weaponCreator.setWeapons(weaponCreator.getWeapons());
            myCreatorService.saveObject(weaponCreator);
            WeaponService.saveObject(weapon);
        }
    }

    private Resources<Resource<Weapon>> addWeaponToCreator(Weapon weapon, Integer creatorId){
        Weapon weaponMain = WeaponService.getObjectById(weapon.getId()).
                orElseThrow(() -> new IllegalArgumentException("Something wrong with weapon: " + weapon));
        if(creatorId == -2)
            return null;
        Creator creator = myCreatorService.getObjectById(creatorId)
                .orElseThrow(() -> new CreatorNotFoundException(creatorId));
        weaponMain.setCreator(creator);
        creator.addWeapon(weaponMain);
        creator.setWeapons(creator.getWeapons());
        myCreatorService.saveObject(creator);
        WeaponService.saveObject(weaponMain);
        return getWeaponsOfCreator(creatorId);
    }

}
