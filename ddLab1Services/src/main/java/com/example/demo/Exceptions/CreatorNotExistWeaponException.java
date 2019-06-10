package com.example.demo.Exceptions;

import com.example.demo.Models.Creator;
import com.example.demo.Models.Weapon;

public class CreatorNotExistWeaponException extends RuntimeException {
    public CreatorNotExistWeaponException(Creator creator, Weapon weapon){
        super("Creator: " + creator.toString() + " does not shot weapon: " + weapon.toString());
    }
    public CreatorNotExistWeaponException(Integer CreatorId, Integer weaponId){
        super("Creator with id: " + CreatorId + " does not shot weapon with id: " + weaponId);
    }
}
