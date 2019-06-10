package com.example.demo.Assemblers;

import com.example.demo.Controllers.CreatorController;
import com.example.demo.Models.Creator;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class CreatorResourcesAssembler implements ResourceAssembler<Creator, Resource<Creator>> {
    @Override
    public Resource<Creator> toResource(Creator creator) {
        return new Resource<>(
                creator,
                linkTo(methodOn(CreatorController.class).getCreator(creator.getId())).withSelfRel(),
                linkTo(methodOn(CreatorController.class).getCreators()).withRel("creators")
        );
    }
}
