package com.clusterfactions.clustercore.core.items.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.clusterfactions.clustercore.ClusterCore;
import com.clusterfactions.clustercore.core.items.CustomItemType;
import com.clusterfactions.clustercore.core.items.types.interfaces.StackableItem;
import com.clusterfactions.clustercore.core.player.PlayerData;
import com.clusterfactions.clustercore.util.Colors;
import com.clusterfactions.clustercore.util.ItemBuilder;

import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.Getter;

public class CustomItem implements Listener{
	public final static String NBT_ITEM_TAG_TYPE = "CLUSTER_CUSTOM_ITEM";
	public final static String NBT_ITEM_TAG_UNIQUE = "CLUSTER_TAG_UNIQUE";
	
	@Getter CustomItemType itemCustomType;
	@Getter String ID;
	@Getter ItemStack item;
	@Getter String displayName;
	@Getter List<String> lore;
	
	public CustomItem(CustomItemType type, ItemStack item, String displayName, String... lore)
	{
		this.ID = type.getId();
		this.item = item;
		this.displayName = displayName;
		this.itemCustomType = type;
		this.lore = Arrays.asList(lore);
	}
	
	public List<String> getItemLore(PlayerData user, List<String> modLore)
	{
		return lore == null ? new ArrayList<>() : new ArrayList<String>(lore);
	}
	
    public boolean isApplicableItem(ItemStack itemStack) {
        if (itemStack == null) return false;
        if (!getItemType().equals(itemStack.getType())) return false;
        
        CustomItemType type = ClusterCore.getInstance().getItemManager().getCustomItemType(itemStack);
        return type == this.itemCustomType;
    }
    
    public Material getItemType() {
        return item.getType();
    }   
    
    public CustomItemType getType() {
        return itemCustomType;
    }
	
    public ItemStack getNewStack()
    {
    	return getNewStack(1);
    }
    
    public ItemStack getNewStack(int amount) {
        NBTItem nbtItem = new NBTItem(new ItemBuilder(item).create());

        nbtItem.setInteger(NBT_ITEM_TAG_TYPE, itemCustomType.ordinal());
        if(!(this instanceof StackableItem))nbtItem.setString(NBT_ITEM_TAG_UNIQUE, UUID.randomUUID().toString());

        ItemStack itemStack = nbtItem.getItem();
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta == null) itemMeta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());

        itemMeta.setDisplayName(Colors.parseColors(displayName));
        itemStack.setItemMeta(itemMeta);
        itemStack.setAmount(amount);
        return itemStack;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAnvilClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null || event.getClickedInventory().getType() != InventoryType.ANVIL) return;
        if (event.getSlot() == 2 && isApplicableItem(event.getCurrentItem())) event.setCancelled(true);
        
    }

    // disable using custom items in enchanting table
    @EventHandler
    public void onItem(EnchantItemEvent event) {
        if (isApplicableItem(event.getItem())) event.setCancelled(true);
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent  event) {
    	if(!(event.getEntity() instanceof Player)) return;
    	ItemStack item = event.getItem().getItemStack().clone();
    	Player player = (Player)event.getEntity();
    	if(!isApplicableItem(item)) return;
    	player.updateInventory();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        for (ItemStack itemStack : event.getInventory().getMatrix()) {
            if (isApplicableItem(itemStack)) {
                event.getInventory().setResult(new ItemStack(Material.AIR));
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null
                && event.getClickedBlock().getType() == Material.SPAWNER && isApplicableItem(event.getItem())) {
            event.setUseItemInHand(Event.Result.DENY);
        }
    }

    @EventHandler
    public void dropEvent(PlayerDropItemEvent e) {
    	if(!this.isApplicableItem(e.getItemDrop().getItemStack())) return;
    }
}