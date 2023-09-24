package fr.pandaguerrier.conodiaspawner;

import org.bukkit.plugin.java.JavaPlugin;

public final class ConodiaSpawner extends JavaPlugin {
    private static ConodiaSpawner instance;

    @Override
    public void onEnable() {
        instance = this;

    }

    @Override
    public void onDisable() {
    }

    public static ConodiaSpawner getInstance() {
        return instance;
    }
}
