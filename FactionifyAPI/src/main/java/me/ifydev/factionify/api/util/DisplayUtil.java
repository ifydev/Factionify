package me.ifydev.factionify.api.util;

import me.ifydev.factionify.api.database.ConnectionError;

import java.util.Optional;

/**
 * @author Innectic
 * @since 07/28/2018
 */
public interface DisplayUtil {
    /**
     * Display an error to online players with the `factionify.admin` permission.
     *
     * @param error     type type of error thrown.
     * @param exception the exception thrown
     */
    void displayError(ConnectionError error, Optional<Exception> exception);
}
