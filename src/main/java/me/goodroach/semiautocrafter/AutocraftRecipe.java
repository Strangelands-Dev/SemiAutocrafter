package me.goodroach.semiautocrafter;

import org.bukkit.Material;

import java.util.HashMap;

public class AutocraftRecipe {
    public final String name;
    public final HashMap<Material,Integer> input;
    public final Material output;
    public final int amount;

    public AutocraftRecipe(String name, HashMap<Material,Integer> input, Material output, int amount) {
        this.name = name;
        this.input = input;
        this.output = output;
        this.amount = amount;
    }

    public String getName(){
        return name;
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
