package me.ifydev.factionify.spigot.util;

import me.ifydev.factionify.api.FactionifyConstants;
import me.ifydev.factionify.api.database.ConnectionError;
import me.ifydev.factionify.api.util.DisplayUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Innectic
 * @since 07/28/2018
 */
public class SpigotDisplayUtil implements DisplayUtil {

    @Override
    public void displayError(ConnectionError error, Optional<Exception> exception) {
        String reportable = shouldReport(error) ? ChatColor.GREEN + "" + ChatColor.BOLD + "Yes": ChatColor.RED + "" + ChatColor.BOLD + "No";
        List<String> messages = FactionifyConstants.FACTIONIFY_ERROR.stream()
                .map(part -> part.replace("<ERROR_TYPE>", error.getDisplay()))
                .map(part -> part.replace("<SHOULD_REPORT>", reportable))
                .map(ColorUtil::makeReadable).collect(Collectors.toList());
        List<Player> players = Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission(FactionifyConstants.PERMISSION_ADMIN)).collect(Collectors.toList());
        messages.forEach(message -> players.forEach(player -> player.sendMessage(message)));
    }

    private boolean shouldReport(ConnectionError error) {
        return error != ConnectionError.REJECTED && error == ConnectionError.DATABASE_EXCEPTION;
    }
}
