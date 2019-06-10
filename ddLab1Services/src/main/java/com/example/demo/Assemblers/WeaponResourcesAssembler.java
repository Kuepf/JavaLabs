package com.example.demo.Assemblers;

import com.example.demo.Controllers.WeaponController;
import com.example.demo.Models.Weapon;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class WeaponResourcesAssembler implements ResourceAssembler<Weapon, Resource<Weapon>> {
    @Override
    public Resource<Weapon> toResource(Weapon weapon) {
        return new Resource<>(
                weapon,
                linkTo(methodOn(WeaponController.class).getWeapon(weapon.getId())).withSelfRel(),
                linkTo(methodOn(WeaponController.class).getWeapons()).withRel("weapons")
        );
    }
}
