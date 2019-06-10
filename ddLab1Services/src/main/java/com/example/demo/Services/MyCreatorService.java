package com.example.demo.Services;

import com.example.demo.Models.Creator;
import com.example.demo.Repositories.CreatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyCreatorService implements CreatorService
{
    @Autowired
    private CreatorRepository creatorRepository;

    @Override
    public List<Creator> getAll()
    {
        return creatorRepository.findAll();
    }

    @Override
    public Optional<Creator> getObjectById(Integer integer)
    {
        return creatorRepository.findById(integer);
    }

    @Override
    public Creator saveObject(Creator newObject)
    {
        return creatorRepository.save(newObject);
    }

    @Override
    public void deleteObject(Integer integer)
    {
        creatorRepository.deleteById(integer);
    }

    @Override
    public Creator updateObject(Creator newObject, Integer integer)
    {
        return creatorRepository.findById(integer)
                .map(car -> {
                    car.setName(newObject.getName());
                    car.setWeapons(newObject.getWeapons());
                    car.setId(newObject.getId());
                    return creatorRepository.save(car);
                })
                .orElseGet(()->{
                    newObject.setId(integer);
                    return creatorRepository.save(newObject);
                });
    }
}
