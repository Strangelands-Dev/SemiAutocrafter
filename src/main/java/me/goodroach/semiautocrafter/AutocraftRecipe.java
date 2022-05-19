package me.goodroach.semiautocrafter;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class AutocraftRecipe {
    public final HashMap<Material,Integer> input;
    public final ItemStack output;

    public AutocraftRecipe(HashMap<Material,Integer> input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    public HashMap<Material,Integer> getInput() {
        return input;
    }
    public ItemStack getOutput() {
        return output;
    }

}
