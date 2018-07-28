package me.ifydev.factionify.api.manager.structures;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Innectic
 * @since 07/27/2018
 */
@AllArgsConstructor
@Getter
public class Chunk {
    private int x;
    private int z;
    private String world;
}
