package com.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class CreatorDTO {
    private int id;

    private String name;

    private List<WeaponDTO> weapons = new ArrayList<>();
}
