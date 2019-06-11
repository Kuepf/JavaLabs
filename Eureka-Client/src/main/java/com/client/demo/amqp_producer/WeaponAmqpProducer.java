package com.client.demo.amqp_producer;

import com.common.WeaponDTO;
import com.common.keys.RabbitKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WeaponAmqpProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendMessage(WeaponDTO weaponDTO, int id) {
        weaponDTO.setId(id);
        rabbitTemplate.convertAndSend(RabbitKeys.QUEUE_WEAPONS_CHANGE_NAME, weaponDTO);
    }

}