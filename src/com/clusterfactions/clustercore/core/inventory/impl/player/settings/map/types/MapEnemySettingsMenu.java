package com.clusterfactions.clustercore.core.inventory.impl.player.settings.map.types;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.clusterfactions.clustercore.ClusterCore;
import com.clusterfactions.clustercore.core.factions.map.MapColour;
import com.clusterfactions.clustercore.core.inventory.impl.player.settings.map.MapMainSettingsMenu;
import com.clusterfactions.clustercore.core.inventory.util.model.InventoryBase;
import com.clusterfactions.clustercore.core.player.PlayerData;
import com.clusterfactions.clustercore.util.ItemBuilder;
import com.clusterfactions.clustercore.util.unicode.CharRepo;

public class MapEnemySettingsMenu extends InventoryBase{

	public MapEnemySettingsMenu(Player player) {
		super(player, "MAP_ENEMY_MENU_SETTINGS", "&f" + CharRepo.MENU_CONTAINER_27, 27);
		PlayerData playerData = ClusterCore.getInstance().getPlayerManager().getPlayerData(player);
		for(MapColour colour : MapColour.values())
		{
			this.addItem(new ItemBuilder(Material.WHITE_WOOL).coloredName(colour.toString()).coloredLore("Click me to choose colour").create(), e -> {
				playerData.setMapEnemyColour(colour); 
				playerData.saveData("mapEnemyColour");
				new MapMainSettingsMenu(player).openInventory(player);});
		}
		 
	}
}
