package com.clusterfactions.clustercore.core.factions.util;

import org.bukkit.Material;

import lombok.Getter;

public enum FactionPerm {
	
	AUDIT("FACTION.AUDIT", "Audit", "", Material.BOOK),
	ALLY("FACTION.ALLY", "ALLY", "Form an allyship with a faction", Material.GOLDEN_APPLE),//
	BAN("FACTION.BAN", "Ban", "", Material.DIRT),
	BUILD("FACTION.BUILD", "Build", "Allow player to build on faction claimed chunks", Material.BRICKS),//
	CHECK("FACTION.CHECK", "Check", "", Material.DIRT),
	CHEST("FACTION.CHEST", "Use Chests", "", Material.CHEST), //
	CLAIM("FACTION.CLAIM", "Claim Land", "Allow player to claim faction chunks", Material.DIRT),
	CONTAINER("FACTION.CONTAINER", "Use Containers", "", Material.DISPENSER), //
	DESTROY("FACTION.DESTROY", "Destroy Blocks", "", Material.DIRT),
	DISBAND("FACTION.DISBAND", "Disband Faction", "", Material.DIRT),
	DRAIN("FACTION.DRAIN", "Use Drain", "", Material.DIRT),
	FLY("FACTION.FLY", "Use Fly", "", Material.DIRT),
	HOME("FACTION.HOME", "Teleport Home", "", Material.DIRT),
	INVITE("FACTION.INVITE","Invite Players", "", Material.DIRT),
	ITEMS("FACTION.ITEMS", "", "", Material.DIRT),
	INTERACT("FACTION.INTERACT", "Interact", "Allow the player to interact with blocks", Material.STONE_BUTTON), //
	KICK("FACTION.KICK", "Kick Players", "", Material.DIRT),
	PAINBUILD("FACTION.PAINBUILD", "", "", Material.DIRT),
	PROMOTE("FACTION.PROMOTE", "Promote Players", "", Material.DIRT),
	SETHOME("FACTION.SETHOME", "Set Faction Home", "", Material.DIRT),
	SETWARP("FACTION.SETWARP", "Set Faction Warp", "", Material.DIRT),
	SPAWNER("FACTION.SPAWNER", "", "", Material.DIRT),
	TERRITORY("FACTION.TERRITORY", "", "", Material.DIRT),
	VAULT("FACTION.VAULT", "Use Faction Vault", "", Material.DIRT),
	WARP("FACTION.WARP", "Use Faction Warps", "", Material.DIRT);
	
	
	@Getter private String name;
	@Getter private String description;
	@Getter private String id;
	@Getter private Material displayMat;
	
	FactionPerm(String id, String name, String description, Material displayMat)
	{
		this.id = id;
		this.name = name;
		this.description = description;
		this.displayMat = displayMat;
	}
}