package de.kleinbuli.itemBuilder.actions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void handleClick(InventoryClickEvent event) {
        ItemActionRegistry.handleClick(event);
    }

}
