package me.ifydev.factionify.spigot;

import lombok.Getter;
import me.ifydev.factionify.api.FactionifyAPI;
import me.ifydev.factionify.api.database.HandlerType;
import me.ifydev.factionify.spigot.commands.FactionifyCommand;
import me.ifydev.factionify.spigot.util.ConfigVerifier;
import me.ifydev.factionify.spigot.util.SpigotDisplayUtil;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Optional;
import java.util.logging.Level;

/**
 * @author Innectic
 * @since 07/27/2018
 */
public class FactionifyMain extends JavaPlugin {

    @Getter private FactionifyAPI api;

    @Override
    public void onEnable() {
        createConfig();

        String handler = getConfig().getString("storage", HandlerType.SQLITE.getDisplayName());
        Optional<HandlerType> handlerType = HandlerType.findType(handler);
        if (!handlerType.isPresent()) {
            getLogger().severe("Invalid data handler type!");
            return;
        }

        getLogger().info("Initializing Factionify API...");
        long start = System.currentTimeMillis();
        api = new FactionifyAPI();

        try {
            api.initialize(handlerType.get(), ConfigVerifier.verifyConnectionInformation(), new SpigotDisplayUtil());
        } catch (Exception e) {
            getLogger().severe("Could not initialize Factionify API!");
            e.printStackTrace();
            return;
        }
        long time = System.currentTimeMillis() - start;
        getLogger().info("Factionify API initialized in " + (time / 1000) + " seconds (" + time + "ms)!");

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
        PluginManager manager = getServer().getPluginManager();
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
