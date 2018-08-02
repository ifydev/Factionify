package me.ifydev.factionify.spigot.commands;

import me.ifydev.factionify.api.FactionifyConstants;
import me.ifydev.factionify.api.util.ArgumentUtil;
import me.ifydev.factionify.spigot.FactionifyMain;
import me.ifydev.factionify.spigot.commands.commands.BasicCommands;
import me.ifydev.factionify.spigot.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author Innectic
 * @since 07/27/2018
 */
public class FactionifyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FactionifyMain plugin = FactionifyMain.getInstance();

        if (!sender.hasPermission(FactionifyConstants.PERMISSION_BASE)) {
            sender.sendMessage(ColorUtil.makeReadable(FactionifyConstants.YOU_DONT_HAVE_PERMISSION));
            return false;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            // Make sure we have arguments, but if we don't send the first help response.
            if (args.length < 1) {
                // Not enough arguments to do anything.
                sendHelpResponse(sender);
                return;
            }

            String first = args[0];

            if (first.equalsIgnoreCase("create")) sender.sendMessage(ColorUtil.makeReadable(
                    BasicCommands.createFaction(sender, ArgumentUtil.getRemainingArgs(1, args))));
            else if (first.equalsIgnoreCase("disband")) sender.sendMessage(ColorUtil.makeReadable(
                    BasicCommands.disbandFaction(sender, ArgumentUtil.getRemainingArgs(1, args))));
            else sendHelpResponse(sender);
        });

        return true;
    }

    private void sendResponse(String response, CommandSender sender) {
        sender.sendMessage(ColorUtil.makeReadable(response));
    }

    private void sendResponse(List<String> responses, CommandSender sender) {
        responses.forEach(response -> sendResponse(response, sender));
    }

    private void sendHelpResponse(CommandSender sender) {
        sender.sendMessage(ColorUtil.makeReadable(FactionifyConstants.FACTIONIFY_HELP_HEADER));
        FactionifyConstants.FACTIONIFY_HELP.forEach(response -> sendResponse(response, sender));
        sender.sendMessage(ColorUtil.makeReadable(FactionifyConstants.FACTIONIFY_HELP_FOOTER));
    }
}
