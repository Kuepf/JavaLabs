package com.example.demo.Services;

import com.example.demo.Models.Weapon;
import com.example.demo.Repositories.WeaponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyWeaponService implements WeaponService
{
    @Autowired
    private WeaponRepository weaponRepository;

    @Override
    public List<Weapon> getAll()
    {
        return weaponRepository.findAll();
    }

    @Override
    public Optional<Weapon> getObjectById(Integer integer)
    {
        return weaponRepository.findById(integer);
    }

    @Override
    public Weapon saveObject(Weapon newObject)
    {
        return weaponRepository.save(newObject);
    }

    @Override
    public void deleteObject(Integer integer)
    {
        weaponRepository.deleteById(integer);
    }

    @Override
    public Weapon updateObject(Weapon newObject, Integer integer)
    {
        return weaponRepository.findById(integer)
                .map(creator -> {
                    creator.setCreator(newObject.getCreator());
                    creator.setName(newObject.getName());
                    creator.setId(newObject.getId());
                    creator.setIsDelete(newObject.isDelete());
                    return weaponRepository.save(creator);
                })
                .orElseGet(()->{
                    newObject.setId(integer);
                    return weaponRepository.save(newObject);
                });
    }

}
