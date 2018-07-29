package me.ifydev.factionify.api.database;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Innectic
 * @since 07/28/2018
 */
@AllArgsConstructor
public enum ConnectionError {
    REJECTED("Connection to database rejected"), DATABASE_EXCEPTION("Exception in a database handler");

    @Getter private String display;
}

