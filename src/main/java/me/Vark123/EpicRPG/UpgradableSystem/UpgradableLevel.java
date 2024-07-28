package me.Vark123.EpicRPG.UpgradableSystem;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;

@Getter
public class UpgradableLevel {

	private int level;
	private double chance;
	private Map<String, Integer> mmIdCosts;
	
	private Map<String, ItemStack> mmItemCosts;

	public UpgradableLevel(int level, double chance, Map<String, Integer> mmIdCosts) {
		super();
		this.level = level;
		this.chance = chance;
		this.mmIdCosts = mmIdCosts;
		
		this.mmItemCosts = new LinkedHashMap<>();
	}
	
	@Nullable
	public ItemStack getItem(String mmId) {
		if(mmItemCosts.containsKey(mmId))
			return mmItemCosts.get(mmId);
		
		Random rand = new Random();
		ItemStack it = MythicBukkit.inst().getItemManager().getItemStack(mmId, mmIdCosts.get(mmId));
		
		NBTItem nbt = new NBTItem(it);
		nbt.setInteger("rand-"+rand.nextInt(), rand.nextInt());
		nbt.applyNBT(it);
		
		mmItemCosts.put(mmId, it);
		return it;
	}
	
	public boolean matchRecipe(Collection<ItemStack> items) {
		mmIdCosts.keySet().forEach(this::getItem);
		
		if(items.size() < mmItemCosts.size())
			return false;

		Collection<String> check = new LinkedList<>();
		for(ItemStack it : items) {
			if(it == null || it.getType().equals(Material.AIR))
				continue;
			NBTItem nbt = new NBTItem(it);
			if(!nbt.hasTag("MYTHIC_TYPE"))
				return false;
			String mmId = nbt.getString("MYTHIC_TYPE");
			if(!mmItemCosts.containsKey(mmId))
				continue;
			if(it.getAmount() < mmIdCosts.get(mmId))
				continue;
			if(check.contains(mmId))
				continue;
			check.add(mmId);
		}

		return check.size() == mmItemCosts.size();
	}
	
}
