package com.clusterfactions.clustercore.core.inventory.util.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.clusterfactions.clustercore.core.inventory.util.InventoryClickHandler;
import com.clusterfactions.clustercore.util.NumberUtil;
import com.clusterfactions.clustercore.util.model.Pair;

import de.tr7zw.changeme.nbtapi.NBTItem;

public abstract class PaginatedInventory extends InventoryBase{
	public static final String PAGENUMBER = "$PAGE_NUMBER";
	public static final String PAGEMAX = "$PAGE_MAX";
	public static final String PAGE_TAG = "PAGE";
	
	private Map<Integer, ItemStack> staticItems = new HashMap<>();
	private int pageAmount;
	private int occupiedSlots;  //Amount of slots occupied by static Items
	
	public PaginatedInventory(Player player, String ID, String displayName, int inventorySize) {
		super(player, ID, displayName, inventorySize);
		setStaticItem(nextPageButton().getRight(), 
				l -> {
					NBTItem nbtItem = new NBTItem(l.getItem());
					int page = Integer.parseInt(nbtItem.getString(PAGE_TAG));
					player.closeInventory();
					this.openInventory(player, page+1);	
				}
				, nextPageButton().getLeft());

		setStaticItem(previousPageButton().getRight(), 
				l -> {
					NBTItem nbtItem = new NBTItem(l.getItem());
					int page = Integer.parseInt(nbtItem.getString(PAGE_TAG));
					player.closeInventory();
					this.openInventory(player, page-1);	
				}
				, previousPageButton().getLeft());
	}
	
	public void setStaticItem(ItemStack item, int... index)
	{
		setStaticItem(item, null, null, index);
	}
	
	public void setStaticItem(ItemStack item, InventoryClickHandler handlerL, int... index)
	{
		setStaticItem(item, handlerL, null, index);
	}
	
	/*
	 * Item that persists throughout all pages
	 */
	public void setStaticItem(ItemStack item, InventoryClickHandler handlerL, InventoryClickHandler handlerR, int... index)
	{
		String rid = UUID.randomUUID().toString();
		registerHandler(rid, handlerL, handlerR);
		
		NBTItem nbtItem = new NBTItem(item);
		nbtItem.setString(ITEM_ID_TAG, rid);
		nbtItem.setString(INVENTORY_TAG, inventoryID);
		nbtItem.setString(RANDOM_UUID, uuid.toString());
		for(int i = 0; i < index.length; i++)
			staticItems.put(index[i], nbtItem.getItem());	
	}

	private int getPageAmount() {
		//CALCULATE AMOUNT OF PAGES & OCCUPIED SLOTS
		this.occupiedSlots = staticItems.size();
		int freeSpace = inventorySize - occupiedSlots;
		this.pageAmount = (int)Math.ceil((double)this.itemAddList.size()/freeSpace)-1;
		return this.pageAmount;
	}
	
	public void openInventory(Player player, int page) {
		invInstance = getInventory(player, page);
		player.openInventory(invInstance);
	}
	
	@Override
	public void openInventory(Player player) {
		openInventory(player, 0);
	}

	@Override
	public Inventory getInventory(Player player) {
		return getInventory(player, 0);
	}
	
	public Inventory getInventory(Player player, int page) {
		//index start at 1
		getPageAmount();
		page = NumberUtil.clamp(page--, 0, pageAmount);
		
		Inventory inv = Bukkit.createInventory(player, inventorySize, displayName);
		for(Entry<Integer, ItemStack> entry : staticItems.entrySet() ) {
			inv.setItem(entry.getKey(), setKey(entry.getValue(),PAGE_TAG,page+""));
		}
		
		if(page >= getPageAmount())
			inv.setItem(nextPageButton().getLeft(), setApplicable(emptyPageButton()));
		
		if(page == 0)
			inv.setItem(previousPageButton().getLeft(), setApplicable(emptyPageButton()));
			
			
		for(int i = page * (inventorySize - occupiedSlots); i < (page+1) * (inventorySize - occupiedSlots); i++)
		{
			if(i >= this.itemAddList.size()) continue;
			if(this.itemAddList.get(i) == null) continue;
			inv.addItem(this.itemAddList.get(i));
		}
		return inv;
	}
	
	protected abstract Pair<Integer, ItemStack> nextPageButton();
	protected abstract Pair<Integer, ItemStack> previousPageButton();
	protected abstract ItemStack emptyPageButton();
}





















