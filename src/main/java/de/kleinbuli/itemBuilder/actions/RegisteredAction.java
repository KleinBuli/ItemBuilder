package de.kleinbuli.itemBuilder.actions;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.function.Consumer;

public record RegisteredAction(Consumer<InventoryClickEvent> eventConsumer, InventoryHolder holder) { }
