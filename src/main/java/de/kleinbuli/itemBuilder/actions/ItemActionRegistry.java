package de.kleinbuli.itemBuilder.actions;

import de.kleinbuli.itemBuilder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemActionRegistry {

    private static JavaPlugin javaPlugin;
    private static final Map<String, RegisteredAction> actions = new HashMap<>();

    public static void init(JavaPlugin plugin) {
        javaPlugin = plugin;
        getJavaPlugin().getServer().getPluginManager().registerEvents(new InventoryClickListener(), getJavaPlugin());
    }


    /**
     *  Registers the click-Action for the specified itembuilder
     *  only called in ItemBuilder#build
     * @param itemBuilder specified itemBuilder
     * @param action specified action
     */
    public static void register(ItemBuilder itemBuilder, Consumer<InventoryClickEvent> action) {

        if(javaPlugin == null) {
            throw new IllegalArgumentException("[ItemActionRegistry] javaPlugin cannot be empty -> use ItemActionRegistry.init(this) in onEnable method");
        }

        ItemMeta meta = itemBuilder.getItemMeta();
        String id = UUID.randomUUID().toString();
        NamespacedKey key = new NamespacedKey(getJavaPlugin(), "click_id");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, id);
        itemBuilder.getItemStack().setItemMeta(meta);
        actions.put(id, new RegisteredAction(action, itemBuilder.getClickInventoryHolder()));
    }

    /**
     * Accepts the Consumer if action is nonnull and inventory holder is correct
     * @param event InventoryClickEvent
     */
    public static void handleClick(InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        ItemMeta meta = clicked.getItemMeta();
        if (meta == null) return;

        NamespacedKey key = new NamespacedKey(getJavaPlugin(), "click_id");
        String id = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if (id == null) return;

        Consumer<InventoryClickEvent> action = actions.get(id).eventConsumer();
        if (action == null) return;

        InventoryHolder inventoryHolder = actions.get(id).holder();
        if(inventoryHolder == null) {
            action.accept(event);
            return;
        }

        if(event.getClickedInventory() == null) return;
        if(Objects.equals(event.getClickedInventory().getHolder(), inventoryHolder)) {
            event.setCancelled(true);
            action.accept(event);
        }

    }


    private static JavaPlugin getJavaPlugin() {
        return javaPlugin;
    }
}
