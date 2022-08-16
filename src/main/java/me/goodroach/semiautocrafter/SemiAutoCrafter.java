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
    public static HashMap<String, AutocraftRecipe> recipes = new HashMap<>();

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
        for (String x : getConfig().getConfigurationSection("Recipes").getKeys(false)) {
            String recipePath = "Recipes." + x;
            System.out.println(x);
            int amount = getConfig().getInt(recipePath + ".Amount");
            System.out.println(amount);
            Material output = Material.getMaterial(getConfig().getString(recipePath + ".Output").toUpperCase());
            System.out.println(output.toString());
            List<String> l = getConfig().getStringList(recipePath + ".Input");
            HashMap<Material, Integer> input = new HashMap<>();
            for (String y : l) {
                String[] a = y.split(",");
                input.put(Material.getMaterial(a[0].toUpperCase()), Integer.parseInt(a[1]));
            }
            AutocraftRecipe recipe = new AutocraftRecipe(x, input, output, amount);
            recipes.put(x.toLowerCase(Locale.ENGLISH), recipe);
            System.out.println("The plugin has successfully registered a recipe.");
        }

        //Gets the server's crafting recipes.
        Iterator<Recipe> it = Bukkit.getServer().recipeIterator();
        Recipe r;
        while (it.hasNext()) {
            r = it.next();
            String name = r.getResult().getType().toString();
            if (recipes.containsKey(name)) continue;
            if (r.getResult().containsEnchantment(Enchantment.ARROW_INFINITE)) continue;
            if (r instanceof ShapedRecipe) { //Shaped recipes are awful, will change this code later.
                ItemStack output = new ItemStack(r.getResult().getType(), r.getResult().getAmount());
                HashMap<Material, Integer> input = new HashMap<>();
                for (ItemStack i : ((ShapedRecipe) r).getIngredientMap().values()) {
                    if (i == null) continue;
                    Material m = i.getType();
                    input.put(m, input.getOrDefault(m, 0) + 1);
                }
                AutocraftRecipe recipe = new AutocraftRecipe(name, input, output.getType(), output.getAmount());
                recipes.put(name, recipe);
            }
            if (r instanceof ShapelessRecipe) {
                ItemStack output = new ItemStack(r.getResult().getType(), r.getResult().getAmount());
                HashMap<Material, Integer> input = new HashMap<>();
                for (ItemStack i : ((ShapelessRecipe) r).getIngredientList()) {
                    Material m = i.getType();
                    input.put(m, input.getOrDefault(m, 0) + 1);
                }
                AutocraftRecipe recipe = new AutocraftRecipe(name, input, output.getType(), output.getAmount());
                recipes.put(name, recipe);
            }
        }
    }
}
