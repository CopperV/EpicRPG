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
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import me.Vark123.EpicRPG.Utils.Pair;
import me.Vark123.EpicRPG.Utils.Utils;

@Getter
public final class UpgradableManager {

	private static final UpgradableManager inst = new UpgradableManager();

	private static final double UPGRADE_PERCENT = 0.03;
	
	private final Map<Integer, UpgradableLevel> levels;
	private final Map<String, UpgradableInhibitor> inhibitors;
	private final Map<String, UpgradableFlask> flasks;

	private UpgradableManager() {
		levels = new LinkedHashMap<>();
		inhibitors = new LinkedHashMap<>();
		flasks = new LinkedHashMap<>();
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

		if(!Utils.isMythicMobItem(it))
			return Optional.empty();
		
		String mythicType = Utils.getMythicMobItemType(it);
		return getInhibitorChance(mythicType);
	}
	
	public Optional<UpgradableInhibitor> getInhibitorChance(String mythicType) {
		return Optional.ofNullable(inhibitors.get(mythicType));
	}
	
	public void registerFlask(UpgradableFlask flask) {
		flasks.put(flask.getMmId(), flask);
	}

	public Optional<UpgradableFlask> getFlask(@Nonnull ItemStack it) {
		if(it.getType().equals(Material.AIR))
			return Optional.empty();

		if(!Utils.isMythicMobItem(it))
			return Optional.empty();
		
		String mythicType = Utils.getMythicMobItemType(it);
		return getFlask(mythicType);
	}

	public Optional<UpgradableFlask> getFlask(@Nonnull String mmId) {
		return Optional.ofNullable(flasks.get(mmId));
	}

	public int getMaxLevel() {
		return levels.keySet()
				.stream()
				.max(Integer::compare)
				.orElse(0);
	}
	
	public boolean isItemUpgradable(@Nonnull ItemStack it) {
		if(it.getType().equals(Material.AIR) || it.getAmount() > 1)
			return false;
		
		List<String> lore = it.getItemMeta().getLore();
		if(lore == null || lore.isEmpty())
			return false;
		
		if(lore.stream()
				.filter(line -> line.contains("Atrybuty"))
				.findFirst()
				.isEmpty())
			return false;
		
		ReadWriteNBT nbt = NBT.itemStackToNBT(it);
		if(!Utils.isMythicMobItem(it))
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
		
		if(!Utils.isMythicMobItem(it))
			return false;
		
		String mythicType = Utils.getMythicMobItemType(it);
		return inhibitors.containsKey(mythicType);
	}
	
	public boolean canUseInhibitor(double chance, @Nonnull ItemStack inhibitor) {
		if(!isItemInhibitor(inhibitor))
			return false;

		String mythicType = Utils.getMythicMobItemType(inhibitor);
		if(!inhibitors.containsKey(mythicType))
			return false;
		
		double inhibitorChance = inhibitors.get(mythicType).getChance();
		double totalChance = chance + inhibitorChance;
		
		return totalChance <= 1;
	}
	
	public boolean isItemFlask(@Nonnull ItemStack it) {
		if(it.getType().equals(Material.AIR))
			return false;
		
		if(!Utils.isMythicMobItem(it))
			return false;
		
		String mythicType = Utils.getMythicMobItemType(it);
		return flasks.containsKey(mythicType);
	}
	
	public boolean canUseFlask(@Nonnull ItemStack flask, @Nonnull ItemStack item) {
		if(!isItemFlask(flask))
			return false;
		
		UpgradableFlask upgradableFlask = getFlask(flask).get();
		
		String searchLine = upgradableFlask.getLineModifier();
		List<String> lore = item.getItemMeta().getLore();
		
		MutableBoolean result = new MutableBoolean(true);
		lore.stream()
			.filter(line -> line.contains(searchLine))
			.findAny()
			.ifPresentOrElse(line -> {
				String strNum = ChatColor.stripColor(line.replace(searchLine, "").replace("+", ""));
				if(!StringUtils.isNumeric(strNum) || strNum.contains(".")) {
					result.setFalse();
					return;
				}
				
				int num = Integer.parseInt(strNum);
				if(num * UPGRADE_PERCENT < 1) {
					result.setFalse();
					return;
				}
			}, () -> result.setFalse());
		
		return result.booleanValue();
	}
	
	public void upgradeItem(Player p, ItemStack item, ItemStack flask, double chance, int newLevel) {
		Random random = new Random();
		double rand = random.nextDouble();
		if(rand > chance) {
			p.spawnParticle(Particle.LARGE_SMOKE, p.getLocation().clone().add(0,1,0), 
					18, .5f, 1.1f, .5f, .08f);
			p.playSound(p, Sound.ENTITY_ZOGLIN_ANGRY, 1, 1);
			return;
		}

		UpgradableFlask upgradableFlask = getFlask(flask).get();
		String searchLine = upgradableFlask.getLineModifier();
		MutableInt statValue = new MutableInt();
		
		ItemMeta im = item.getItemMeta();
		List<String> lore = im.getLore();
		lore.stream()
			.filter(line -> line.contains(searchLine))
			.map(lore::indexOf)
			.filter(index -> index >= 0)
			.findAny()
			.ifPresent(i -> {
				String line = lore.get(i);
				
				String strNum = ChatColor.stripColor(line.replace(searchLine, "").replace("+", ""));
				if(!StringUtils.isNumeric(strNum) || strNum.contains(".")) {
					return;
				}
				
				int num = Integer.parseInt(strNum);
				if(num * UPGRADE_PERCENT < 1) {
					return;
				}
				
				int stat = (int) Math.round(num * UPGRADE_PERCENT);
				statValue.setValue(stat);
				num += stat;
				lore.set(i, searchLine+(line.contains("§7+")?"§7+":"§7")+num);
			});
		
		ReadWriteNBT nbt = NBT.itemStackToNBT(item);
		ReadWriteNBT upgradesNbt = nbt.getOrCreateCompound("epic-upgrades");
		upgradesNbt.setInteger("level", newLevel);
		ReadWriteNBT statsNbt = upgradesNbt.getOrCreateCompound("stats");
		if(statsNbt.hasTag(searchLine)) {
			statsNbt.setInteger(searchLine, statsNbt.getInteger(searchLine)+statValue.getValue());
		} else {
			statsNbt.setInteger(searchLine, statValue.getValue());
		}
//		nbt.applyNBT(item);
		item = NBT.itemStackFromNBT(nbt);

		String mmId = Utils.getMythicMobItemType(item);
		ItemStack baseItem = MythicBukkit.inst().getItemManager().getItemStack(mmId);
		
		im = item.getItemMeta();
		im.setDisplayName(baseItem.getItemMeta().getDisplayName()+" §r§7§l+"+newLevel);
		im.setLore(lore);
		item.setItemMeta(im);

		p.spawnParticle(Particle.COMPOSTER, p.getLocation().clone().add(0,1,0), 
				18, .5f, 1.1f, .5f, .08f);
		p.playSound(p, Sound.BLOCK_ANVIL_USE, 1, 1);
	}
	
	public void upgradeItem(Player p, ItemStack item, double chance, int newLevel) {
		Random random = new Random();
		double rand = random.nextDouble();
		if(rand > chance) {
			p.spawnParticle(Particle.LARGE_SMOKE, p.getLocation().clone().add(0,1,0), 
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
//		Object[] keys = stats.entrySet().stream()
//				.map(entry -> Map.entry(entry.getKey(), 
//						entry.getKey().contains("Walka") ? 
//							entry.getValue() / 2 * 5 :
//							entry.getKey().contains("Mana") ? 
//								entry.getValue() / 2 :
//								entry.getValue()))
//				.sorted((e1, e2) -> e2.getValue() - e1.getValue())
//				.limit(4)
//				.map(entry -> entry.getKey())
//				.collect(Collectors.toList()).toArray();
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
		
		ReadWriteNBT nbt = NBT.itemStackToNBT(item);
		ReadWriteNBT upgradesNbt = nbt.getOrCreateCompound("epic-upgrades");
		upgradesNbt.setInteger("level", newLevel);
		ReadWriteNBT statsNbt = upgradesNbt.getOrCreateCompound("stats");
		pickedStats.forEach((stat, value) -> {
			if(statsNbt.hasTag(stat))
				statsNbt.setInteger(stat, statsNbt.getInteger(stat)+value);
			else
				statsNbt.setInteger(stat, value);
		});
//		nbt.applyNBT(item);
		item = NBT.itemStackFromNBT(nbt);

		String mmId = Utils.getMythicMobItemType(item);
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
