package me.Vark123.EpicRPG.UpgradableSystem;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.Vark123.EpicRPG.Core.MoneySystem;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
@AllArgsConstructor
@Builder
public class UpgradableInhibitor {

	private static final long CRAFT_COOLDOWN = 200;
	private static final Map<Player, Date> cd = new ConcurrentHashMap<>();

	private String mmId;
	private double chance;
	private InhibitorCrafting crafting;
	
	@Getter(value = AccessLevel.NONE)
	private ItemStack item;
	
	public ItemStack getItem() {
		if(item != null)
			return item;
		
		item = MythicBukkit.inst().getItemManager().getItemStack(mmId);
		
		Random rand = new Random();
		NBTItem nbt = new NBTItem(item);
		nbt.setInteger("rand-"+rand.nextInt(), rand.nextInt());
		nbt.applyNBT(item);
		
		return item;
	}
	
	@Getter
	public static class InhibitorCrafting {
		private double moneyCost;
		private Map<String, Integer> mmIdCosts;
		
		private Map<String, ItemStack> mmItemCosts;

		public InhibitorCrafting(double moneyCost, Map<String, Integer> mmIdCosts) {
			super();
			this.moneyCost = moneyCost;
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
		
		public boolean matchRecipe(Player p, Collection<ItemStack> items) {
			if(!PlayerManager.getInstance().getRpgPlayer(p).getVault().hasEnoughMoney(moneyCost))
				return false;
			
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
		
		public void craft(Player p, UpgradableInhibitor inhibitor, Inventory inv) {
			if(cd.containsKey(p) 
					&& (new Date().getTime() - cd.get(p).getTime()) < CRAFT_COOLDOWN)
				return;

			Collection<ItemStack> items = InhibitorMenuManager.get().getCraftingSlots()
					.stream()
					.map(inv::getItem)
					.filter(item -> item != null && !item.getType().equals(Material.AIR))
					.collect(Collectors.toList());
			if(!matchRecipe(p, items))
				return;
			
			MoneySystem.getInstance().removeMoney(PlayerManager.getInstance().getRpgPlayer(p), moneyCost);
			
			ItemStack result = MythicBukkit.inst().getItemManager().getItemStack(inhibitor.getMmId());
			Collection<String> check = new LinkedList<>();
			InhibitorMenuManager.get().getCraftingSlots().forEach(slot -> {
				ItemStack it = inv.getItem(slot);
				if(it == null || it.getType().equals(Material.AIR))
					return;
				NBTItem nbt = new NBTItem(it);
				if(!nbt.hasTag("MYTHIC_TYPE"))
					return;
				String mmId = nbt.getString("MYTHIC_TYPE");
				if(!mmIdCosts.containsKey(mmId))
					return;
				if(it.getAmount() < mmIdCosts.get(mmId))
					return;
				if(check.contains(mmId))
					return;
				
				if(it.getAmount() > mmIdCosts.get(mmId))
					it.setAmount(it.getAmount() - mmIdCosts.get(mmId));
				else
					inv.setItem(slot, null);
				check.add(mmId);
			});

			p.playSound(p, Sound.ENTITY_DROWNED_AMBIENT_WATER, 1, 1.3f);
			p.spawnParticle(Particle.CRIT_MAGIC, p.getLocation().add(0,1,0), 16, .5f, .5f, .5f, .1f);

			Utils.dropItemStack(p, result);
			cd.put(p, new Date());

			Collection<ItemStack> newItems = InhibitorMenuManager.get().getCraftingSlots()
					.stream()
					.map(inv::getItem)
					.filter(item -> item != null && !item.getType().equals(Material.AIR))
					.collect(Collectors.toList());
			inv.setItem(49, matchRecipe(p, newItems) ? 
					InhibitorMenuManager.get().getGreenCreate() : InhibitorMenuManager.get().getRedCreate());
			
		}
		
	}
	
}
