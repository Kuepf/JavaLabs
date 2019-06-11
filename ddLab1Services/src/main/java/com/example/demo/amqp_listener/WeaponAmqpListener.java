package com.example.demo.amqp_listener;

import com.common.WeaponDTO;
import com.common.keys.RabbitKeys;
import com.example.demo.Models.Weapon;
import com.example.demo.Services.MyWeaponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor

@Component
public class WeaponAmqpListener {
    @Autowired
    MyWeaponService myWeaponService;

    @RabbitListener(queues = {RabbitKeys.QUEUE_WEAPONS_CHANGE_NAME})
    public void receiveMessage(WeaponDTO weaponDTO) {
        weaponDTO.setName(weaponDTO.getName() + "RabbitMQ");
        myWeaponService.updateObject(new ModelMapper().map(weaponDTO, Weapon.class), weaponDTO.getId());
    }
}