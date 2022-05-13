package me.goodroach.semiautocrafter;

import me.goodroach.semiautocrafter.listeners.PlacementListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SemiAutoCrafter extends JavaPlugin {

    private static SemiAutoCrafter instance;

    @Override
    public void onEnable() {
        instance = this;
        PluginManager pm = getServer().getPluginManager();
        AutocrafterRecipe recipe = new AutocrafterRecipe(this);
        pm.registerEvents(new PlacementListener(recipe), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
