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

/**
 * Utility class for building customized ItemStacks
 * Allows setting item properties such as material, displayname, amount, lore, enchantments, item flags, ...
 * You can also add a custom clickAction handled by the InventoryClickEvent
 */

public class ItemBuilder  {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    private InventoryHolder clickInventoryHolder;
    private Consumer<InventoryClickEvent> clickAction;

    /**
     * Creates an ItemBuilder instance with the specified material
     * @param material type of the itemstack
     */
    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    /**
     * Creates an ItemBuilder instance with the specified material and amount
     * @param material type of the itemstack
     * @param amount the amount of items in the stack
     */
    public ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
        itemStack.setAmount(amount);
    }

    /**
     * Sets the color of the item if it is leather armor.
     *
     */
    public ItemBuilder leatherColor(Color color) {

        if(itemMeta instanceof LeatherArmorMeta leatherArmorMeta) {
            leatherArmorMeta.setColor(color);
        }

        return this;

    }

    /** Sets the displayName using a raw text and color.
     *
     * @param componentText text of the component
     * @param textColor color of the text
     * @return this ItemBuilder for chaining
     */
    public ItemBuilder displayName(String componentText, TextColor textColor) {
        itemMeta.displayName(Component.text(componentText).color(textColor));
        return this;
    }

    /** Adds a new line to the lore.
     *
     * @param line of the lore
     * @return this ItemBuilder for chaining
     */
    public ItemBuilder addLoreLine(Component line) {
        Objects.requireNonNull(itemMeta.lore()).add(line);
        return this;
    }

    /** Sets the displayname of the itemstack
     *
     * @param component component of the displayname
     * @return this ItemBuilder for chaining
     */
    public ItemBuilder displayName(Component component) {
        itemMeta.displayName(component);
        return this;
    }

    /** Sets the lore of the itemstack with multiple lines
     *
     * @param lines the lore lines as components
     * @return this ItemBuilder for chaining
     */
    public ItemBuilder lore(Component... lines) {
        itemMeta.lore(Arrays.asList(lines));
        return this;
    }

    /** Enchants the itemstack with the specified enchantment and level
     *
     * @param enchantment enchantment of the itemstack
     * @param value level of the enchantment
     * @return this ItemBuilder for chaining
     */
    public ItemBuilder enchant(Enchantment enchantment, int value) {
        itemMeta.addEnchant(enchantment, value, true);
        return this;
    }

    /** Adds itemflags to the itemStack
     *
     * @param flags the flags to add
     * @return this ItemBuilder for chaining
     */
    public ItemBuilder flags(ItemFlag... flags) {
        itemMeta.addItemFlags(flags);
        return this;
    }

    /** Makes the itemstack unbreakable
     *
     * @return this ItemBuilder for chaining
     */
    public ItemBuilder unbreakable() {
        itemMeta.setUnbreakable(true);
        return this;
    }

    /** Makes the itemstack unbreakable and hides that
     *
     * @return this ItemBuilder for chaining
     */

    public ItemBuilder hiddenUnbreakable() {
        itemMeta.setUnbreakable(true);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        return this;
    }


    /**
     * Sets the InventoryHolder for the onClick()-Action
     * @param inventoryHolder inventoryholder for which the click is valid
     * @return this ItemBuilder for chaining
     */
    public ItemBuilder inventoryHolder(InventoryHolder inventoryHolder) {
        this.clickInventoryHolder = inventoryHolder;
        return this;
    }

    /**
     * Sets a click action (consumer) to be triggered when the item is clicked in an inventory.
     * @param eventConsumer the click event consumer
     * @return this ItemBuilder for chaining
     */
    public ItemBuilder onClick(Consumer<InventoryClickEvent> eventConsumer) {
        this.clickAction = eventConsumer;
        return this;
    }

    /**
     * Builds and returns the finished itemStack
     * Registers the click if one is set.
     * @return the finished itemStack
     */
    public ItemStack build() {

        if(clickAction != null) {
            ItemActionRegistry.register(this, clickAction);
        }

        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }

    /**
     * Returns the InventoryHolder associated with this item for click handling.
     * @return the inventory holder or null if none set
     */
    public InventoryHolder getClickInventoryHolder() {
        return clickInventoryHolder;
    }

    /**
     * Returns the click action consumer associated with this item.
     * @return the click action consumer or null if none set
     */
    public Consumer<InventoryClickEvent> getClickAction() {
        return clickAction;
    }

    /**
     * Returns the underlying ItemStack.
     * @return the item stack
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Returns the ItemMeta of the item.
     * @return the item meta
     */
    public ItemMeta getItemMeta() {
        return itemMeta;
    }
}
