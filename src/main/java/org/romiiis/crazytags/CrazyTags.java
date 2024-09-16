package org.romiiis.crazytags;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.romiiis.crazytags.CMD.CustomCommand;
import org.romiiis.crazytags.Config.CustomConfig;
import org.romiiis.crazytags.Database.Database;
import org.romiiis.crazytags.Database.MySQL.MySQL;
import org.romiiis.crazytags.Database.SQLite.SQLite;
import org.romiiis.crazytags.Listeners.ChatListener;
import org.romiiis.crazytags.Objects.TagInv;

import java.util.Objects;


/**
 * Main class of the plugin
 *
 * @author Romiiis
 * @version 1.0.0
 */
public final class CrazyTags extends JavaPlugin {

    /**
     * Instance of the economy
     */
    private Economy econ = null;

    /**
     * Instance of the database
     */
    private Database db = null;

    public final static String PERM_PREFIX = "crazytags.";


    /**
     * Method called when the plugin is enabled
     */
    @Override
    public void onEnable() {

        // Plugin startup logic
        this.getLogger().info("SuperTags has been enabled!");

        // Load configs
        CustomConfig.getInstance().load();

        // Register commands and events
        registerCommands();

        // Register events
        registerEvents();

        // Check if Vault is installed
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Print the database map
        System.out.println(CustomConfig.getInstance().getDatabase());


        if (CustomConfig.getInstance().getDatabase().get("type").equalsIgnoreCase("mysql")) {
            // Load database
            db = new MySQL(this);

        } else if (CustomConfig.getInstance().getDatabase().get("type").equalsIgnoreCase("sqlite")) {
            // Load database
            db = new SQLite(this);
        }
        else {
            getLogger().severe("Invalid database type in config.yml");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Load database
        db.load();

    }


    /**
     * Method called when the plugin is disabled
     */
    @Override
    public void onDisable() {

        this.getLogger().info("SuperTags has been disabled!");

    }

    /**
     * Register commands
     */
    private void registerCommands() {

        Objects.requireNonNull(this.getCommand("crazytags")).setExecutor(new CustomCommand());

    }

    /**
     * Register events
     */
    private void registerEvents() {

        // Register event class TagInv
        getServer().getPluginManager().registerEvents(TagInv.getInstance(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);


    }


    /**
     * Setup the economy
     *
     * @return true if the economy is setup, false otherwise
     */
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().severe("Economy not found!");
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }


    /** GETTERS **/

    /**
     * Get the economy instance
     *
     * @return Economy instance
     */
    public Economy getEconomy() {
        return econ;
    }

    /**
     * Get the database instance
     *
     * @return Database instance
     */
    public Database getDatabase() {
        return db;
    }


    /**
     * Get the instance of the plugin
     *
     * @return Instance of the plugin
     */
    public static CrazyTags getInstance() {
        return getPlugin(CrazyTags.class);
    }


}
