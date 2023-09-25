package fr.pandaguerrier.conodiaspawner;

import fr.pandaguerrier.conodiaspawner.commands.SpawnerAdminCommand;
import fr.pandaguerrier.conodiaspawner.commands.SpawnerCommand;
import fr.pandaguerrier.conodiaspawner.events.InventoryListener;
import fr.pandaguerrier.conodiaspawner.events.PlayerListener;
import fr.pandaguerrier.conodiaspawner.events.SpawnerListener;
import fr.pandaguerrier.conodiaspawner.managers.PlayerSpawner;
import fr.pandaguerrier.conodiaspawner.managers.Spawner;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ConodiaSpawner extends JavaPlugin {
    private static ConodiaSpawner instance;
    private final Map<Location, Spawner> placedSpawners = new HashMap<>();
    private final Map<UUID, PlayerSpawner> playerSpawners = new HashMap<>();
    private final Map<UUID, Spawner> waitingPlaceSpawners = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnerListener(), this);

        getCommand("spawneradmin").setExecutor(new SpawnerAdminCommand());
        getCommand("spawner").setExecutor(new SpawnerCommand());
    }

    @Override
    public void onDisable() {
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
}
