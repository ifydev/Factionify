package me.ifydev.factionify.spigot.commands.commands;

import me.ifydev.factionify.api.FactionifyConstants;
import me.ifydev.factionify.api.faction.Faction;
import me.ifydev.factionify.api.faction.Role;
import me.ifydev.factionify.spigot.FactionifyMain;
import me.ifydev.factionify.spigot.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * @author Innectic
 * @since 07/29/2018
 */
public class BasicCommands {

    public static String createFaction(CommandSender sender, String[] args) {
        if (!sender.hasPermission(FactionifyConstants.PERMISSION_FACTION_CREATE)) return FactionifyConstants.YOU_DONT_HAVE_PERMISSION;
        if (!(sender instanceof Player)) return FactionifyConstants.YOU_ARENT_A_PLAYER;

        if (args.length < 1) {
            // We don't have a name for the faction
            return FactionifyConstants.NOT_ENOUGH_ARGUMENTS_FACTION_CREATE;
        }
        // TODO: Should faction names be allowed to have spaces in them?
        String factionName = args[0];
        FactionifyMain plugin = FactionifyMain.getInstance();
        if (!plugin.getApi().getDatabaseHandler().isPresent()) return FactionifyConstants.DATABASE_HANDLER_NOT_PRESENT;

        Player player = (Player) sender;

        // :NameExistsCheck
        Optional<Faction> faction = plugin.getApi().getDatabaseHandler().get().createGroup(factionName, player.getUniqueId());
        if (!faction.isPresent()) return FactionifyConstants.DATABASE_ERROR;

        // @Speed
        String broadcast = ColorUtil.makeReadable(FactionifyConstants.FACTION_CREATED_BROADCAST.replace("<NAME>", factionName));
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> !p.getUniqueId().equals(player.getUniqueId()))
                .forEach(p -> p.sendMessage(broadcast));
        return FactionifyConstants.FACTION_CREATED.replace("<NAME>", factionName);
    }

    public static String disbandFaction(CommandSender sender, String[] args) {
        if (!sender.hasPermission(FactionifyConstants.PERMISSION_FACTION_DISBAND)) return FactionifyConstants.YOU_DONT_HAVE_PERMISSION;
        if (!(sender instanceof Player)) return FactionifyConstants.YOU_ARENT_A_PLAYER;

        Player player = (Player) sender;
        FactionifyMain plugin = FactionifyMain.getInstance();
        if (plugin.getApi().getDatabaseHandler().isPresent()) return FactionifyConstants.DATABASE_HANDLER_NOT_PRESENT;

        Optional<Faction> faction = plugin.getApi().getDatabaseHandler().get().getFactionForPlayer(player.getUniqueId());
        // Make sure the player is actually in a faction
        if (!faction.isPresent()) return FactionifyConstants.YOU_ARENT_IN_A_FACTION;
        // Then, we can get the role of the player
        Optional<Role> role = faction.get().getRoleForPlayer(player.getUniqueId());
        if (!role.isPresent()) return FactionifyConstants.INTERNAL_ERROR.replace("<ERROR>", "Role is missing");
        // Then make sure the player has the proper power to do this
        // :ReplacePowerWithSomethingElse
        // :ConfigurableDisbandPower
        if (role.get().getPower() < plugin.getMinimumDisbandPower()) return FactionifyConstants.YOU_DONT_HAVE_POWER;
        // Since the user has the right power, we can *finally* disband.
        // :BetterResultReturn
        Optional<Faction> result = plugin.getApi().getDatabaseHandler().get().removeFaction(faction.get().getUuid());
        if (!result.isPresent()) return FactionifyConstants.DATABASE_ERROR;
        // Announce the disband.
        // @Speed
        String broadcast = ColorUtil.makeReadable(FactionifyConstants.FACTION_DISBANDED_BROADCAST.replace("<NAME>", result.get().getName()));
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> !p.getUniqueId().equals(player.getUniqueId()))
                .forEach(p -> p.sendMessage(broadcast));
        return FactionifyConstants.FACTION_DISBANDED.replace("<NAME>", result.get().getName());
    }
}
