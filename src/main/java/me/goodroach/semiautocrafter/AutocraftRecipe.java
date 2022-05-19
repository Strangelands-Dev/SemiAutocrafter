package me.goodroach.semiautocrafter;

import org.bukkit.Material;

import java.util.HashMap;

public class AutocraftRecipe {
    public final HashMap<Material,Integer> input;
    public final Material output;
    public final int amount;

    public AutocraftRecipe(HashMap<Material,Integer> input, Material output, int amount) {
        this.input = input;
        this.output = output;
        this.amount = amount;
    }

    public HashMap<Material,Integer> getInput() {
        return input;
    }
    public Material getOutput() {
        return output;
    }
    public int getAmount() {
        return amount;
    }

}
