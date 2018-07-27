package me.ifydev.factionify.spigot;

import me.ifydev.factionify.spigot.commands.FactionifyCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

/**
 * @author Innectic
 * @since 07/27/2018
 */
public class FactionifyMain extends JavaPlugin {

    @Override
    public void onEnable() {
        createConfig();

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {

    }

    private void registerCommands() {
        getCommand("factionify").setExecutor(new FactionifyCommand());
    }

    private void registerListeners() {

    }

    private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                boolean created = getDataFolder().mkdirs();
                if (!created) getLogger().log(Level.SEVERE, "Could not create config!");
            }

            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("Config.yml not found, creating!");
                saveDefaultConfig();
            } else getLogger().info("Config.yml found, loading!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FactionifyMain getInstance() {
        return FactionifyMain.getPlugin(FactionifyMain.class);
    }
}
