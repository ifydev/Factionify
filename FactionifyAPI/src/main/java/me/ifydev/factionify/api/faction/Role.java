package me.ifydev.factionify.api.faction;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * @author Innectic
 * @since 07/28/2018
 */
@AllArgsConstructor
@Getter
public class Role {

    private UUID uuid;
    private UUID faction;
    private String name;
    private int power;
}
