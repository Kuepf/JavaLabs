package com.example.demo.Controllers;

import com.example.demo.Assemblers.WeaponResourcesAssembler;
import com.example.demo.Assemblers.CreatorResourcesAssembler;
import com.example.demo.Exceptions.VechicleNotFoundException;
import com.example.demo.Exceptions.CreatorNotExistWeaponException;
import com.example.demo.Exceptions.CreatorNotFoundException;
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

@RestController
@RequestMapping("/creators")
public class CreatorController {
    @Autowired
    private CreatorService myCreatorService;
    @Autowired
    private WeaponService WeaponService;

    private final CreatorResourcesAssembler assembler;
    private final WeaponResourcesAssembler movieAssembler;

    public CreatorController(CreatorResourcesAssembler assembler, WeaponResourcesAssembler weaponResourcesAssembler){
        this.assembler = assembler;
        this.movieAssembler = weaponResourcesAssembler;
    }

    @GetMapping
    public List<Creator> getCreators(){
        return myCreatorService.getAll();
//        return new Resources<>(
//                list,
//                linkTo(methodOn(CreatorController.class).getCreators()).withSelfRel()
//        );
    }
    @GetMapping("/{creatorId}")
    public ResponseEntity<ResourceSupport> getCreator(@PathVariable Integer creatorId){
        Optional<Creator> creator = myCreatorService.getObjectById(creatorId);
        return creator.isPresent() ?
                ResponseEntity
                        .ok(assembler.toResource(creator.get())) :
                ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new VndErrors.VndError("Creator not found", new CreatorNotFoundException(creatorId).getMessage()));
    }
    @PostMapping
    public ResponseEntity<?> createCreator(@RequestBody Creator newCreator) throws URISyntaxException {
        Resource<Creator> resource = assembler.toResource(myCreatorService.saveObject(newCreator));
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }
    @PutMapping("/{creatorId}")
    public ResponseEntity<?> updateCreator(@RequestBody Creator updatedCreator, @PathVariable Integer creatorId) throws URISyntaxException {
        Creator updatedObj = myCreatorService.getObjectById(creatorId)
                .map(director -> {
                    director.setName(updatedCreator.getName());
                    director.setWeapons(updatedCreator.getWeapons());
                    return myCreatorService.saveObject(director);
                })
                .orElseGet(()->{
                    updatedCreator.setId(creatorId);
                    return myCreatorService.saveObject(updatedCreator);
                });

        Resource<Creator> resource = assembler.toResource(updatedObj);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/{creatorId}/notsave")
    public ResponseEntity<?> deleteCreatorWithWeapon(@PathVariable Integer creatorId){
        myCreatorService.deleteObject(creatorId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{creatorId}")
    public ResponseEntity<?> deleteCreatorAndSaveWeapon(@PathVariable Integer creatorId){
        Creator creator = myCreatorService.getObjectById(creatorId)
                .orElseThrow(() -> new CreatorNotFoundException(creatorId));
        Weapon[] weapons = new Weapon[creator.getWeapons().size()];
        weapons = creator.getWeapons().toArray(weapons);
        for (Weapon stud:
                weapons) {
            Weapon weapon = WeaponService.getObjectById(stud.getId()).get();
            weapon.setCreator(null);
            creator.removeWeapons(weapon);
            creator.setWeapons(creator.getWeapons());
            WeaponService.saveObject(weapon);
            myCreatorService.saveObject(creator);
        }
        return deleteCreatorWithWeapon(creatorId);
    }

    @GetMapping("/{creatorId}/weapons")
    public Resources<Resource<Weapon>> getWeaponOfCreator(@PathVariable Integer creatorId){
        Optional<Creator> creator = myCreatorService.getObjectById(creatorId);
        List<Resource<Weapon>> list = creator.get().getWeapons()
                .stream()
                .map(movieAssembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(
                list,
                linkTo(methodOn(CreatorController.class).getCreator(creatorId)).withSelfRel(),
                linkTo(methodOn(WeaponController.class).getWeapons()).withSelfRel()
        );
    }
    @DeleteMapping("/{creatorId}/weapons/{weaponId}")
    public Resources<Resource<Weapon>> removeWeaponFromCreator(@PathVariable Integer creatorId, @PathVariable Integer bossId){
        Weapon weapon = WeaponService.getObjectById(bossId)
                .orElseThrow(() -> new VechicleNotFoundException(bossId));
        Creator creator = myCreatorService.getObjectById(creatorId)
                .orElseThrow(() -> new CreatorNotFoundException(creatorId));
        if(!creator.getWeapons().contains(weapon))
            throw new CreatorNotExistWeaponException(creator, weapon);
        else {
            weapon.setCreator(null);
            creator.removeWeapons(weapon);
            creator.setWeapons(creator.getWeapons());
            myCreatorService.saveObject(creator);
            WeaponService.saveObject(weapon);
        }
        return getWeaponOfCreator(creatorId);
    }

}
