package com.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data

@NoArgsConstructor
@AllArgsConstructor
public class CreatorDTO {
    private int id;

    private String name;

    private List<WeaponDTO> weapons = new ArrayList<>();
}
