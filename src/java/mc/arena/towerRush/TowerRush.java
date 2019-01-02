package mc.arena.towerRush;

import mc.alk.arena.BattleArena;
import mc.alk.arena.util.Log;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class TowerRush extends JavaPlugin {
    static TowerRush plugin;

    public void onEnable() {
        plugin = this;
        loadConfig();
        TowerRushListener l = new TowerRushListener();
        BattleArena.registerCompetition(this, "TowerRush", "tr", TowerRushArena.class, new TowerRushExecutor(l));
        Bukkit.getServer().getPluginManager().registerEvents(l, this);
        Log.info("[" + getName() + "] v" + getDescription().getVersion() + " enabled!");
    }

    public void onDisable() {
        Log.info("[" + getName() + "] v" + getDescription().getVersion() + " stopping!");
    }

    public static TowerRush getSelf() {
        return plugin;
    }

    public void reloadConfig() {
        super.reloadConfig();
        loadConfig();
    }

    public void loadConfig() {
        saveDefaultConfig();

        FileConfiguration config = getConfig();
        CaptureBlock.MAX_HEALTH = config.getInt("blockMaxHealth", CaptureBlock.MAX_HEALTH);
    }
}
