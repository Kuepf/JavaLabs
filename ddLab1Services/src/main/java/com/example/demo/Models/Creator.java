package com.example.demo.Models;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Creator")
@EntityListeners(AuditingEntityListener.class)
public class Creator
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    private String name;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Weapon> weapons = new ArrayList<>();

    public int getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Weapon> getWeapons()
    {
        return weapons;
    }

    public void setWeapons(List<Weapon> weapons)
    {
        this.weapons = weapons;
    }

    public boolean removeWeapons(Weapon weapon)
    {
        return weapons.contains(weapon) && weapons.remove(weapon);

    }

    public boolean removeWeaponById(Integer id)
    {
        for(int i = 0; i < weapons.size(); ++i)
        {
            if(weapons.get(i).getId() == id)
                return weapons.remove(weapons.get(i));
        }
        return false;
    }

    public List<Weapon> addWeapon(Weapon weapon)
    {
        if(weapons.contains(weapon))
            throw new RuntimeException("Weapon already added to this group!");
        weapons.add(weapon);
        return weapons;
    }
}
