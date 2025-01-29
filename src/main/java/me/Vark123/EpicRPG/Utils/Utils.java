package me.Vark123.EpicRPG.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.adapters.AbstractVector;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;

public class Utils {

	private Utils() {}
	
	public static void resetSetInfo(ItemStack it) {
		if(it == null || it.getType().equals(Material.AIR))
			return;
		ReadWriteNBT nbt = NBT.itemStackToNBT(it);
		if(!nbt.hasTag("EpicSet"))
			return;
		
		ItemMeta im = it.getItemMeta();
		List<String> lore = im.getLore();
		if(lore == null || lore.isEmpty())
			return;

		int lineNumber = lore.stream()
				.filter(line -> line.contains("§6§l》 §c§lSet "))
				.map(lore::indexOf)
				.findFirst()
				.orElse(Integer.MIN_VALUE) + 1;
		if(lineNumber < 1)
			return;
		
		while(lineNumber < lore.size() && !lore.get(lineNumber).isBlank() && lore.get(lineNumber).length() > 2) {
			String line = lore.get(lineNumber);
			line = line.replace("§a", "§8").replace("●", "¤");
			lore.set(lineNumber, line);
			++lineNumber;
		}
		
		im.setLore(lore);
		it.setItemMeta(im);
	}
	
	public static void setItemSetInfo(ItemStack it, int level) {
		if(it == null || it.getType().equals(Material.AIR))
			return;
		ReadWriteNBT nbt = NBT.itemStackToNBT(it);
		if(!nbt.hasTag("EpicSet"))
			return;

		ItemMeta im = it.getItemMeta();
		List<String> lore = im.getLore();
		if(lore == null || lore.isEmpty())
			return;
		
		int lineNumber = lore.stream()
				.filter(line -> line.contains("§6§l》 §c§lSet "))
				.map(lore::indexOf)
				.findFirst()
				.orElse(Integer.MIN_VALUE) + 1;
		if(lineNumber < 1)
			return;

		while(lineNumber < lore.size() && !lore.get(lineNumber).isBlank() && lore.get(lineNumber).length() > 2) {
			String line = lore.get(lineNumber);
			if(!line.endsWith(" czesci"))
				continue;
			int presentLevel = Integer.parseInt(line.split(" ")[1]);
			if(presentLevel > level)
				break;
			line = line.replace("§8", "§a").replace("¤", "●");
			lore.set(lineNumber, line);
			++lineNumber;
			while(lineNumber < lore.size() && !lore.get(lineNumber).isBlank() && lore.get(lineNumber).length() > 2
					&& !lore.get(lineNumber).endsWith(" czesci")) {
				line = lore.get(lineNumber);
				line = line.replace("§8", "§a");
				lore.set(lineNumber, line);
				++lineNumber;
			}
		}

		im.setLore(lore);
		it.setItemMeta(im);
	}
	
	public static int getFirstPossibleSlot(@Nonnull Inventory inv, @Nonnull ItemStack it) {
		int slot = -1;
		for(int i = 0; i < inv.getSize(); ++i) {
			ItemStack slotItem = inv.getItem(i);
			if(slotItem == null || slotItem.getType().equals(Material.AIR)) {
				slot = i;
				break;
			}
			if(slotItem.isSimilar(it) && slotItem.getAmount() < slotItem.getMaxStackSize()) {
				slot = i;
				break;
			}
		}
		return slot;
	}
	
	public static String convertToClassConvention(String s) {
		String[] tab = s.split(" ");
		StringBuilder toReturn = new StringBuilder(tab[0]);
		for(int i = 1; i < tab.length; ++i) {
			toReturn.append(StringUtils.capitalize(tab[i]));
		}
		return toReturn.toString();
	}
	
	public static Direction getLookingDirection(Location loc, boolean combineDirections) {
		float yaw = loc.getYaw();
		if(yaw < 0)
			yaw += 360;
		double offset = combineDirections ? 22.5 : 0;
		double offset2 = combineDirections ? 0 : 45;
		int divide = combineDirections ? 45 : 90;
		
		int index = ((int)((yaw + offset2) % 360 + offset)) / divide;
		return combineDirections ? Direction.doubleDirection[index] : Direction.Directions[index];
	}
	
	public static double normalizeValue(double min, double max, double value) {
		if(min > max)
			throw new IllegalArgumentException("Minimum value cannot be greater than maximum value. Min: "+min+"; Max: "+max);
		if(value < min)
			return min;
		if(value > max)
			return max;
		return value;
	}
	
	public static double scaleValue(double min1, double max1, double min2, double max2, double value) {
		double percent = (value - min1) / (max1 - min1);
		return percent*(max2 - min2) + min2;
	}
	
	public static double limitValue(double min, double max, double value) {
		if(value < min)
			return min;
		else if(value > max)
			return max;
		return value;
	}
	
	public static boolean isRune(ItemStack item) {
		if(!MythicBukkit.inst().getItemManager().isMythicItem(item))
			return false;
		if(item.getType().name().contains("MUSIC_DISC")) 
			return true;
		else 
			return false;
	}
	
	public static boolean hasWeapon(Player player) {
		ItemStack hand = player.getInventory().getItemInMainHand();
		if(hand == null || hand.getType().equals(Material.AIR))
			return false;
		if(!hand.hasItemMeta() || !hand.getItemMeta().hasLore())
			return false;
		return hand.getItemMeta().getLore().parallelStream().anyMatch(line -> {
			if(line.contains(" Atrybuty ") || line.contains(" Wymagania "))
				return true;
			return false;
		});
	}
	
	public static void dropItemStack(Player p, ItemStack it) {
		if(it == null || it.getType().equals(Material.AIR))
			return;
		Inventory inv = p.getInventory();
		int freeSlot = inv.firstEmpty();
		if(freeSlot >= 0 && freeSlot < 36) {
			inv.setItem(freeSlot, it);
		} else {
			p.getWorld().dropItem(p.getLocation(), it);
		}
	}
	
	public static void takeItems(Player p, int slot, int amount) {
		ItemStack it = p.getInventory().getItem(slot);
		if(it == null
				|| it.getType().equals(Material.AIR))
			return;
		if(it.getAmount() < amount) {
			p.getInventory().setItem(slot, new ItemStack(Material.AIR));
		} else {
			it.setAmount(it.getAmount() - amount);
		}
		p.updateInventory();
	}
	
	public static void takeItems(Player p, Inventory inv, int slot, int amount) {
		ItemStack it = inv.getItem(slot);
		if(it == null
				|| it.getType().equals(Material.AIR))
			return;
		if(it.getAmount() < amount) {
			inv.setItem(slot, new ItemStack(Material.AIR));
		} else {
			it.setAmount(it.getAmount() - amount);
		}
		p.updateInventory();
	}
	
	public static void takeItems(Player p, EquipmentSlot slot, int amount) {
		if(slot.equals(EquipmentSlot.HAND)) {
			takeItems(p, p.getInventory().getHeldItemSlot(), amount);
			return;
		}
		takeItems(p, slot.ordinal(), amount);
	}
	
	public static List<Integer> intArrayToList(int[] arr){
		return Arrays.stream(arr).boxed().collect(Collectors.toList());
	}
	
	public static double getAngle(Player p, Entity e) {
		return getAngle(p.getEyeLocation(), e.getLocation().clone().add(0,1,0));
	}
	
	public static double getAngle(Entity e1, Entity e2) {
		return getAngle(e1.getLocation().clone().add(0,1,0), e2.getLocation().clone().add(0,1,0));
	}
	
	public static double getAngle(Location loc1, Location loc2) {
		return getAngle(BukkitAdapter.adapt(loc1),
				BukkitAdapter.adapt(loc2));
	}
	
	public static double getAngle(AbstractLocation loc1, AbstractLocation loc2) {
		
		AbstractVector vec1 = loc2.clone().subtract(loc1).toVector().normalize();
		AbstractVector vec2 = loc1.getDirection();
		
		vec2.setY(0);
		AbstractLocation loc3 = loc1.clone().add(vec2.multiply(3));
		vec2 = loc3.clone().subtract(loc1).toVector().normalize();
		
		vec1.setY(0);
		vec2.setY(0);
		
		return Math.abs(Math.toDegrees(vec1.angle(vec2)));
	}
	
	public static void neutralizeEntityNoDamageTicks(LivingEntity damager, LivingEntity entity) {
		if(MythicBukkit.inst().getMobManager().isMythicMob(entity)) {
			ActiveMob mob = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity);
			if(mob.hasImmunityTable()) {
				mob.getImmunityTable().clearCooldown(BukkitAdapter.adapt(damager));
			} else {
				mob.getEntity().setNoDamageTicks(0);
			}
		} else {
			entity.setNoDamageTicks(0);
		}
	}
	
	public static boolean hasNoDamageTicks(LivingEntity damager, LivingEntity victim) {
		if(MythicBukkit.inst().getMobManager().isMythicMob(victim)){
			ActiveMob mob = MythicBukkit.inst().getMobManager().getMythicMobInstance(victim);
			if(mob.hasImmunityTable()) {
				return mob.getImmunityTable().onCooldown(BukkitAdapter.adapt(damager));
			} else {
				return mob.getEntity().getNoDamageTicks() > 0;
			}
		} else {
			return victim.getNoDamageTicks() > 0;
		}
	}
	
	public static void setLastDamageCause(Entity entity, EntityDamageEvent event) {
		AbstractEntity aEntity = BukkitAdapter.adapt(entity);
		if(aEntity == null) {
			return;
		}
		
		aEntity.setMetadata("LastDamageCause", event);
	}
	
	public static EntityDamageEvent getLastDamageCause(Entity entity) {
		AbstractEntity aEntity = BukkitAdapter.adapt(entity);
		if(aEntity == null || !aEntity.hasMetadata("LastDamageCause")) {
			return null;
		}
		
		return (EntityDamageEvent) aEntity.getMetadata("LastDamageCause").get();
	}
	
	public static boolean isMythicMobItem(@NotNull ItemStack it) {
		return MythicBukkit.inst().getItemManager().isMythicItem(it);
	}
	
	public static String getMythicMobItemType(@NotNull ItemStack it) {
		return MythicBukkit.inst().getItemManager().getMythicTypeFromItem(it);
	}
	
}
