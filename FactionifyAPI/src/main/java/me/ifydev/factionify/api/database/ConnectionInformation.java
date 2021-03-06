package me.ifydev.factionify.api.database;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * @author Innectic
 * @since 07/28/2018
 */
@AllArgsConstructor
public class ConnectionInformation {
    @Getter private String url;
    @Getter private String database;
    @Getter private int port;
    @Getter private String username;
    @Getter private String password;
    @Getter private Map<String, Object> meta;
}
