package de.kleinbuli.itemBuilder;

import de.kleinbuli.itemBuilder.actions.ItemActionRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

public class ItemBuilder  {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    private InventoryHolder clickInventoryHolder;
    private Consumer<InventoryClickEvent> clickAction;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
        itemStack.setAmount(amount);
    }

    public ItemBuilder color(Color color) {

        if(itemMeta instanceof LeatherArmorMeta leatherArmorMeta) {
            leatherArmorMeta.setColor(color);
        }

        return this;

    }

    public ItemBuilder displayName(String componentText, TextColor textColor) {
        itemMeta.displayName(Component.text(componentText).color(textColor));
        return this;
    }

    public ItemBuilder addLoreLine(Component line) {
        Objects.requireNonNull(itemMeta.lore()).add(line);
        return this;
    }

    public ItemBuilder displayName(Component component) {
        itemMeta.displayName(component);
        return this;
    }

    public ItemBuilder lore(Component... lines) {
        itemMeta.lore(Arrays.asList(lines));
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int value) {
        itemMeta.addEnchant(enchantment, value, true);
        return this;
    }

    public ItemBuilder flags(ItemFlag... flags) {
        itemMeta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder unbreakable() {
        itemMeta.setUnbreakable(true);
        return this;
    }

    public ItemBuilder hiddenUnbreakable() {
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        return this;
    }

    public ItemBuilder inventoryHolder(InventoryHolder inventoryHolder) {
        this.clickInventoryHolder = inventoryHolder;
        return this;
    }

    public ItemBuilder onClick(Consumer<InventoryClickEvent> eventConsumer) {
        this.clickAction = eventConsumer;
        return this;
    }

    public ItemStack build() {

        if(clickAction != null) {
            ItemActionRegistry.register(this, clickAction);
        }

        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }

    public InventoryHolder getClickInventoryHolder() {
        return clickInventoryHolder;
    }

    public Consumer<InventoryClickEvent> getClickAction() {
        return clickAction;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ItemMeta getItemMeta() {
        return itemMeta;
    }
}
