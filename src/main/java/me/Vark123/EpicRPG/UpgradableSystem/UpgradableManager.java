package me.Vark123.EpicRPG.UpgradableSystem;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import me.Vark123.EpicRPG.Utils.Pair;

@Getter
public final class UpgradableManager {

	private static final UpgradableManager inst = new UpgradableManager();

	private static final double UPGRADE_PERCENT = 0.03;
	
	private final Map<Integer, UpgradableLevel> levels;
	private final Map<String, UpgradableInhibitor> inhibitors;

	private UpgradableManager() {
		levels = new LinkedHashMap<>();
		inhibitors = new LinkedHashMap<>();
	}

	public static final UpgradableManager get() {
		return inst;
	}

	public void registerUpgradableLevel(UpgradableLevel level) {
		levels.put(level.getLevel(), level);
	}

	public Optional<UpgradableLevel> getUpgradableLevel(int level) {
		return Optional.ofNullable(levels.get(level));
	}
	
	public void registerInhibitor(UpgradableInhibitor inhibitor) {
		inhibitors.put(inhibitor.getMmId(), inhibitor);
	}

	public Optional<UpgradableInhibitor> getInhibitor(@Nonnull ItemStack it) {
		if(it.getType().equals(Material.AIR))
			return Optional.empty();

		NBTItem nbt = new NBTItem(it);
		if(!nbt.hasTag("MYTHIC_TYPE"))
			return Optional.empty();
		
		String mythicType = nbt.getString("MYTHIC_TYPE");
		return getInhibitorChance(mythicType);
	}
	
	public Optional<UpgradableInhibitor> getInhibitorChance(String mythicType) {
		return Optional.ofNullable(inhibitors.get(mythicType));
	}

	public int getMaxLevel() {
		return levels.keySet()
				.stream()
				.max(Integer::compare)
				.orElse(0);
	}
	
	public boolean isItemUpgradable(@Nonnull ItemStack it) {
		if(it.getType().equals(Material.AIR))
			return false;
		
		List<String> lore = it.getItemMeta().getLore();
		if(lore == null || lore.isEmpty())
			return false;
		
		if(lore.stream()
				.filter(line -> line.contains("Atrybuty"))
				.findFirst()
				.isEmpty())
			return false;
		
		NBTItem nbt = new NBTItem(it);
		if(!nbt.hasTag("MYTHIC_TYPE"))
			return false;
		
		if(nbt.hasTag("Klejnot") 
				|| (nbt.hasTag("RPGType") && nbt.getString("RPGType").equals("gem")))
			return false;
		
		ReadWriteNBT upgradesNbt = nbt.getOrCreateCompound("epic-upgrades");
		if(upgradesNbt.hasTag("level") && upgradesNbt.getInteger("level") >= getMaxLevel())
			return false;
		
		return true;
	}
	
	public boolean isItemInhibitor(@Nonnull ItemStack it) {
		if(it.getType().equals(Material.AIR))
			return false;
		
		NBTItem nbt = new NBTItem(it);
		if(!nbt.hasTag("MYTHIC_TYPE"))
			return false;
		
		String mythicType = nbt.getString("MYTHIC_TYPE");
		return inhibitors.containsKey(mythicType);
	}
	
	public boolean canUseInhibitor(double chance, @Nonnull ItemStack inhibitor) {
		if(!isItemInhibitor(inhibitor))
			return false;

		NBTItem nbt = new NBTItem(inhibitor);
		String mythicType = nbt.getString("MYTHIC_TYPE");
		if(!inhibitors.containsKey(mythicType))
			return false;
		
		double inhibitorChance = inhibitors.get(mythicType).getChance();
		double totalChance = chance + inhibitorChance;
		
		return totalChance <= 1;
	}
	
	public void upgradeItem(Player p, ItemStack item, double chance, int newLevel) {
		Random random = new Random();
		double rand = random.nextDouble();
		if(rand > chance) {
			p.spawnParticle(Particle.SMOKE_LARGE, p.getLocation().clone().add(0,1,0), 
					18, .5f, 1.1f, .5f, .08f);
			p.playSound(p, Sound.ENTITY_ZOGLIN_ANGRY, 1, 1);
			return;
		}
		
		Map<String, Integer> stats = new LinkedHashMap<>();
		Map<String, Integer> pickedStats = new LinkedHashMap<>();
		ItemMeta im = item.getItemMeta();
		List<String> lore = im.getLore();
		lore.stream()
			.filter(s -> s.contains(": §7"))
			.filter(s -> s.contains("§4- §8"))
			.filter(s -> !s.endsWith("%"))
			.map(s -> s.replace("+", ""))
			.map(s -> s.split("§7"))
			.filter(s -> s.length > 1)
			.filter(s -> StringUtils.isNumeric(s[1]))
			.filter(s -> !s[1].contains("."))
			.map(s -> new Pair<>(s[0], Integer.parseInt(s[1])))
			.filter(pair -> Math.round(pair.getValue()*UPGRADE_PERCENT) > 0)
			.forEach(pair -> stats.put(pair.getKey(), pair.getValue()));
		Object[] keys = stats.keySet().toArray();
		Collection<Object> randomStats = new HashSet<>();
		if(keys.length < 2) {
			randomStats.addAll(Arrays.asList(keys));
		} else {
			do {
				Object result = keys[random.nextInt(keys.length)];
				if(randomStats.contains(result))
					continue;
				randomStats.add(result);
			} while(randomStats.size() < 2);
		}
		
		for(Object stat : randomStats) {
			pickedStats.put((String) stat, (int) Math.round(stats.get(stat)*UPGRADE_PERCENT));
		}
		
		List<String> loreCopy = new LinkedList<>(lore);
		for(String line : loreCopy) {
			if(line.contains(": §c"))
				continue;
			pickedStats.keySet().stream()
				.filter(_line -> line.startsWith(_line))
				.findFirst()
				.ifPresent(_line -> {
					lore.set(loreCopy.indexOf(line),
							(_line+(line.contains("§7+")?"§7+":"§7")
									+(stats.get(_line)+pickedStats.get(_line))));
				});
		}
		
		NBTItem nbt = new NBTItem(item);
		ReadWriteNBT upgradesNbt = nbt.getOrCreateCompound("epic-upgrades");
		upgradesNbt.setInteger("level", newLevel);
		ReadWriteNBT statsNbt = upgradesNbt.getOrCreateCompound("stats");
		pickedStats.forEach((stat, value) -> {
			if(statsNbt.hasTag(stat))
				statsNbt.setInteger(stat, statsNbt.getInteger(stat)+value);
			else
				statsNbt.setInteger(stat, value);
		});
		nbt.applyNBT(item);

		String mmId = nbt.getString("MYTHIC_TYPE");
		ItemStack baseItem = MythicBukkit.inst().getItemManager().getItemStack(mmId);
		
		im = item.getItemMeta();
		im.setDisplayName(baseItem.getItemMeta().getDisplayName()+" §r§7§l+"+newLevel);
		im.setLore(lore);
		item.setItemMeta(im);

		p.spawnParticle(Particle.COMPOSTER, p.getLocation().clone().add(0,1,0), 
				18, .5f, 1.1f, .5f, .08f);
		p.playSound(p, Sound.BLOCK_ANVIL_USE, 1, 1);
	}

}
