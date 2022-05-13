package me.goodroach.semiautocrafter.listeners;

import me.goodroach.semiautocrafter.AutocrafterRecipe;
import me.goodroach.semiautocrafter.SemiAutoCrafter;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlacementListener implements Listener{
    private NamespacedKey key = new NamespacedKey(SemiAutoCrafter.getPlugin(SemiAutoCrafter.class), "autocrafter");
    private final AutocrafterRecipe autocrafterRecipe;

    public PlacementListener(AutocrafterRecipe autocrafterRecipe) {
        this.autocrafterRecipe = autocrafterRecipe;
    }


    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (!event.getItemInHand().getItemMeta().equals(autocrafterRecipe.getItem().getItemMeta())) return;
        System.out.println("I see the autocrafter.");
        if (!(event.getBlock().getState() instanceof TileState)) return;
        System.out.println("I see the autocrafter again.");
        TileState state = (TileState) event.getBlock().getState();
        PersistentDataContainer container = state.getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, "autocrafter");
        state.update();

        event.getPlayer().sendMessage(ChatColor.YELLOW +
                "Place a sign with the item's name to assign what the autocrafter should make.");
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
        System.out.println("I see the player is in survival.");
        TileState state = (TileState) event.getBlock().getState();
        PersistentDataContainer container = state.getPersistentDataContainer();
        Block b = event.getBlock();
        if (container.has(key, PersistentDataType.STRING)){
            System.out.println("I see the autocrafter is being broken.");
            event.setCancelled(true);
            b.setType(Material.AIR);
            b.getWorld().dropItem(b.getLocation(), autocrafterRecipe.getItem().clone());
        }
    }
}
