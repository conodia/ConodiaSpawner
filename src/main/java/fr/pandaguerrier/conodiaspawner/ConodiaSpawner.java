package fr.pandaguerrier.conodiaspawner;

import fr.pandaguerrier.conodiaspawner.commands.SpawnerAdminCommand;
import fr.pandaguerrier.conodiaspawner.commands.SpawnerCommand;
import fr.pandaguerrier.conodiaspawner.configuration.LevelConfiguration;
import fr.pandaguerrier.conodiaspawner.events.InventoryListener;
import fr.pandaguerrier.conodiaspawner.events.PlayerListener;
import fr.pandaguerrier.conodiaspawner.events.SpawnerListener;
import fr.pandaguerrier.conodiaspawner.managers.PlayerManager;
import fr.pandaguerrier.conodiaspawner.spawner.Spawner;
import fr.pandaguerrier.conodiaspawner.spawner.player.PlayerSpawner;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ConodiaSpawner extends JavaPlugin {
    private static ConodiaSpawner instance;
    private final Map<Location, Spawner> placedSpawners = new HashMap<>();
    private final Map<UUID, PlayerSpawner> playerSpawners = new HashMap<>();
    private final Map<UUID, Spawner> waitingPlaceSpawners = new HashMap<>();
    private static Economy econ = null;
    private PlayerManager playerManager;
    private LevelConfiguration levelConfiguration;

    @Override
    public void onEnable() {
        instance = this;
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnerListener(), this);

        getCommand("spawneradmin").setExecutor(new SpawnerAdminCommand());
        getCommand("spawner").setExecutor(new SpawnerCommand());

        playerManager = new PlayerManager();
        levelConfiguration = new LevelConfiguration();

        playerManager.sync();
        levelConfiguration.sync();
    }

    @Override
    public void onDisable() {}

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        econ = rsp.getProvider();
        return econ != null;
    }

    public static ConodiaSpawner getInstance() {
        return instance;
    }
    public Map<Location, Spawner> getPlacedSpawners() {
        return placedSpawners;
    }
    public Map<UUID, PlayerSpawner> getPlayerSpawners() {
        return playerSpawners;
    }
    public Map<UUID, Spawner> getWaitingPlaceSpawners() {
        return waitingPlaceSpawners;
    }
    public static Economy getEconomy() {
        return econ;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
    public LevelConfiguration getLevelConfiguration() {
        return levelConfiguration;
    }
}
