package me.goodroach.semiautocrafter;

import com.sun.tools.javac.jvm.Items;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtilities {

    public int getItemCount(Inventory inventory, ItemStack countItem) {
        ItemStack[] inventorySlots = inventory.getStorageContents();

        int size = 0;
        for (ItemStack is : inventorySlots) {
            if (is != null && is.isSimilar(countItem)) {
                size += is.getAmount();
            }
        }
        return size;
    }

    public void removeItems(Inventory inventory, ItemStack removeItem, int amount) {
        int removeAmount = amount;

        if (getItemCount(inventory, removeItem) < amount) return;
        for(ItemStack invItem : inventory) {
            if(invItem != null) {
                if(invItem.isSimilar(removeItem)) {
                    int preAmount = invItem.getAmount();
                    int newAmount = Math.max(0, preAmount - removeAmount);
                    removeAmount = Math.max(0, removeAmount - preAmount);
                    invItem.setAmount(newAmount);
                    if(removeAmount == 0) {
                        break;
                    }
                }
            }
        }

    }
}
