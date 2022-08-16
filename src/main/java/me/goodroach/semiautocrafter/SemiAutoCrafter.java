package me.goodroach.semiautocrafter;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public final class SemiAutoCrafter extends JavaPlugin {

    public static SemiAutoCrafter instance;
    public static HashMap<String, AutocraftRecipe> autocraftRecipeHashMap= new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        PluginManager pm = getServer().getPluginManager();
        AutocrafterCraftingRecipe recipe = new AutocrafterCraftingRecipe(this);
        pm.registerEvents(new Listeners(recipe), this);
        addData();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void addData() {
        for (String name : getConfig().getConfigurationSection("Recipes").getKeys(false)) {
            String recipePath = "Recipes." + name;
            //input
            List<String> l = getConfig().getStringList(recipePath + ".Input");
            HashMap<Material, Integer> input = new HashMap<>();
            for (String y : l) {
                String[] a = y.split(",");
                input.put(Material.getMaterial(a[0].toUpperCase()), Integer.parseInt(a[1]));
            }
            //output
            ItemStack output = new ItemStack(
                    Material.getMaterial(getConfig().getString(recipePath + ".Output").toUpperCase()),
                    getConfig().getInt(recipePath + ".Amount"));
            System.out.println(output);

            AutocraftRecipe recipe = new AutocraftRecipe(input,output);
            autocraftRecipeHashMap.put(name.toLowerCase(Locale.ENGLISH), recipe);
            System.out.println("The plugin has successfully registered a recipe.");
        }
    }
}
