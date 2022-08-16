package me.goodroach.semiautocrafter;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.*;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class Listeners implements Listener {
    private NamespacedKey key1 = new NamespacedKey(SemiAutoCrafter.getPlugin(SemiAutoCrafter.class), "autocrafter");
    private NamespacedKey key2 = new NamespacedKey(SemiAutoCrafter.getPlugin(SemiAutoCrafter.class), "output");
    private final AutocrafterCraftingRecipe autocrafterRecipe;

    public Listeners(AutocrafterCraftingRecipe autocrafterRecipe) {
        this.autocrafterRecipe = autocrafterRecipe;
    }

    private InventoryUtilities invUtils = new InventoryUtilities();


    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (!event.getItemInHand().getItemMeta().equals(autocrafterRecipe.getItem().getItemMeta())) return;
        if (!(event.getBlock().getState() instanceof TileState)) return;

        TileState state = (TileState) event.getBlock().getState();
        PersistentDataContainer container = state.getPersistentDataContainer();

        Dropper dropper = (Dropper) event.getBlock().getState();
        dropper.setCustomName(ChatColor.GRAY + "Autocrafter");
        dropper.update(true);

        container.set(key1, PersistentDataType.STRING, "semiautocrafter");
        state.update();

        event.getPlayer().sendMessage(ChatColor.YELLOW +
                "Place a sign with the item's name to assign what the autocrafter should make and right click it.");
    }

    @EventHandler
    public void onBreakSign(BlockBreakEvent event) {
        Block b = event.getBlock();
        if (!(b.getBlockData() instanceof WallSign)) return;

        Sign s = (Sign) b.getState();
        org.bukkit.material.Directional signData = (org.bukkit.material.Directional) b.getState().getData();
        Block dropper = b.getRelative(signData.getFacing().getOppositeFace());
        TileState state = (TileState) dropper.getState();
        PersistentDataContainer container = state.getPersistentDataContainer();

        if (container.has(key2, PersistentDataType.STRING)) {
            dropper.setType(Material.AIR);
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You broke the autocrafter.");
            return;
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
        if (!(event.getBlock().getType().equals(Material.DROPPER))) return;
        TileState state = (TileState) event.getBlock().getState();
        PersistentDataContainer container = state.getPersistentDataContainer();
        Block b = event.getBlock();
        if (container.has(key2, PersistentDataType.STRING)) {
            event.setCancelled(true);
            b.setType(Material.AIR);
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You broke the autocrafter.");
            return;
        }
        if (container.has(key1, PersistentDataType.STRING)) {
            event.setCancelled(true);
            b.setType(Material.AIR);
            b.getWorld().dropItem(b.getLocation(), autocrafterRecipe.getItem().clone());
            return;
        }
    }

    @EventHandler
    public void onRegister(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block b = event.getClickedBlock();
        if (!(b.getBlockData() instanceof WallSign)) return;
        Sign s = (Sign) b.getState();
        org.bukkit.material.Directional signData = (org.bukkit.material.Directional) b.getState().getData();
        String assignedName = s.getLine(0).toLowerCase(Locale.ENGLISH);

        if (SemiAutoCrafter.autocraftRecipeHashMap.get(assignedName) == null) {
            event.getPlayer().sendMessage(ChatColor.YELLOW + "Invalid Name!");
            event.setCancelled(true);
            return;
        }

        Block dropper = b.getRelative(signData.getFacing().getOppositeFace());
        TileState state = (TileState) dropper.getState();
        PersistentDataContainer container = state.getPersistentDataContainer();
        if (!container.has(key1, PersistentDataType.STRING)) return;

        if (container.has(key2, PersistentDataType.STRING)) {
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You've already assigned this autocrafter.");
            event.setCancelled(true);
            return;
        }

        container.set(key2, PersistentDataType.STRING, assignedName);
        state.update();
        event.getPlayer().sendMessage(ChatColor.YELLOW + "Autocrafter assigned to: " + assignedName);

    }

    @EventHandler
    public void onTransfer(InventoryMoveItemEvent event) {
        Block b = event.getSource().getLocation().getBlock();
        TileState state = (TileState) b.getState();
        PersistentDataContainer container = state.getPersistentDataContainer();

        if (!container.has(key1, PersistentDataType.STRING)) return;
        if (!container.has(key2, PersistentDataType.STRING)) return;

        AutocraftRecipe recipe = SemiAutoCrafter.autocraftRecipeHashMap.get(container.get(key2, PersistentDataType.STRING));

        int recipeCheck = recipe.getInput().size();
        int correctRecipes = 0;
      //  System.out.println("Recipe input size: " + recipeCheck);
        ItemStack input;

        ArrayList<ItemStack> removeAmount = new ArrayList<>();
        for(Material m : recipe.getInput().keySet()){
            input = new ItemStack(m,recipe.getInput().get(m));

            if(event.getSource().contains(m, recipe.getInput().get(m))) {
               // System.out.println("Checks out.");
                correctRecipes++;
                removeAmount.add(input);
               // System.out.println("size: " + correctRecipes);
            }
        }

        if (correctRecipes != recipeCheck) {
            event.setCancelled(true);
            return;
        }

        removeAmount.forEach(Item -> {
            invUtils.removeItems(event.getSource(), Item, Item.getAmount());
        });
        event.setItem(recipe.getOutput());
    }
}
