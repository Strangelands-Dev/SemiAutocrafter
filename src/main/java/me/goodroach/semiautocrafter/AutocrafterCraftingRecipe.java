package me.goodroach.semiautocrafter;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;



public class AutocrafterCraftingRecipe {
    private SemiAutoCrafter plugin;
    final ItemStack autocrafter = new ItemStack(Material.DROPPER, 1);
    final ItemMeta autocrafterMeta = autocrafter.getItemMeta();

    public AutocrafterCraftingRecipe(SemiAutoCrafter plugin) {
        this.plugin = plugin;
        craftRecipe();
    }

    public void craftRecipe(){
        autocrafterMeta.setDisplayName(ChatColor.GOLD + "Semi-autocrafter");
        autocrafterMeta.addEnchant(Enchantment.ARROW_INFINITE, 2 ,true);
        autocrafterMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        autocrafter.setItemMeta(autocrafterMeta);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "semiautocrafter"), autocrafter);
        recipe.shape("XYX","ZWZ","XYX");
        recipe.setIngredient('X',Material.SMOOTH_STONE);
        recipe.setIngredient('Y',Material.PISTON);
        recipe.setIngredient('Z',Material.EMERALD_BLOCK);
        recipe.setIngredient('W',Material.DISPENSER);
        plugin.getServer().addRecipe(recipe);
    }

    public ItemStack getItem() {
        return autocrafter;
    }
}
