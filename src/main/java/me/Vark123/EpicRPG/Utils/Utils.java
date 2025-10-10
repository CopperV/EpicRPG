package me.Vark123.EpicRPG.Utils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.adapters.AbstractVector;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicComponentAPI.EpicComponent;
import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.RuneEffectType;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.SummonManager;
import me.Vark123.EpicRPG.Utils.Events.PlayersAllyCheckEvent;

public class Utils {

	private static final String[] currencySuffixes = {"", "K", "M", "B", "T"};
	
	private Utils() {}
	
	public static void takeEntityHp(LivingEntity entity, double amount) {
		DamageUtils.applyDirectDamageEffect(entity, amount, DamageType.GENERIC, DamageCause.CONTACT);
	}
	
	public static void resetSetInfo(ItemStack it) {
		if(it == null || it.getType().equals(Material.AIR))
			return;
		EpicComponent comp = new EpicComponent(it, MythicBukkit.inst());
		if(!comp.hasKey("epic_set"))
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
		EpicComponent comp = new EpicComponent(it, MythicBukkit.inst());
		if(!comp.hasKey("epic_set"))
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
		if(item == null || item.getType().equals(Material.AIR))
			return false;
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
		int numSlot;
		switch(slot) {
			case BODY:
				numSlot = 38;
				break;
			case HEAD:
				numSlot = 39;
				break;
			case CHEST:
				numSlot = 38;
				break;
			case LEGS:
				numSlot = 37;
				break;
			case FEET:
				numSlot = 36;
				break;
			case HAND:
				numSlot = p.getInventory().getHeldItemSlot();
				break;
			case OFF_HAND:
				numSlot = 40;
				break;
			default:
				numSlot = -1;
				break;
		}
		
		if(numSlot < 0)
			return;
		
		takeItems(p, numSlot, amount);
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
	
	public static void setEntityNoDamageTicks(LivingEntity damager, LivingEntity entity) {
		if(MythicBukkit.inst().getMobManager().isMythicMob(entity)) {
			ActiveMob mob = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity);
			if(mob.hasImmunityTable()) {
				mob.getImmunityTable().setCooldown(BukkitAdapter.adapt(damager));
			} else {
				mob.getEntity().setNoDamageTicks(mob.getNoDamageTicks());
			}
		} else {
			entity.setNoDamageTicks(entity.getNoDamageTicks());
		}
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
	
	public static void setLastDamageCalc(Entity entity, double damage) {
		AbstractEntity aEntity = BukkitAdapter.adapt(entity);
		if(aEntity == null) {
			return;
		}
		
		aEntity.setMetadata("EpicLastDamage", damage);
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
	
	public static Vector transferSphericalToVector(double radius, double angle1, double angle2) {
		double x = radius * Math.cos(angle1) * Math.cos(angle2);
		double y = radius * Math.sin(angle1);
		double z = radius * Math.cos(angle1) * Math.sin(angle2);
		
		return new Vector(x, y, z);
	}
	
	public static String formatCurrency(double value) {
		int index = 0;
		while(value >= 1000 && index < (currencySuffixes.length - 1)) {
			value *= 0.001;
			++index;
		}
		return String.format(value % 1 == 0 ? "%.0f%s" : "%.1f%s", value, currencySuffixes[index]);
	}
	
	public static String formatCurrencyGrouped(double value) {
		return formatCurrencyGrouped(value, Locale.GERMANY);
	}
	
	public static String formatCurrencyGrouped(double value, Locale locale) {
		return formatCurrencyGrouped(value, 2, locale);
	}
	
	public static String formatCurrencyGrouped(double value, int maxFractionDigits) {
		return formatCurrencyGrouped(value, maxFractionDigits, Locale.GERMANY);
	}
	
	public static String formatCurrencyGrouped(double value, int maxFractionDigits, Locale locale) {
		NumberFormat formatter = NumberFormat.getInstance(locale);
		formatter.setMaximumFractionDigits(maxFractionDigits);
		formatter.setMinimumFractionDigits(0);
        return formatter.format(value);
	}
	
	public static String toRomeValue(int num) {
		String[] thousands = {"", "M", "MM", "MMM"};
        String[] hundreds = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] tens = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] ones = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
        
        return thousands[num / 1000] +
               hundreds[(num % 1000) / 100] +
               tens[(num % 100) / 10] +
               ones[num % 10];
	}
	
	public static void drawPentagram(Particle particle, Location center, Vector axis, int points, double radius, double lineOffset, int step, double rotation) {
		double angle = Math.PI*2 / (double) points;
		Vector normalizedAxis = axis.clone().normalize();

	    Location[] vertices = new Location[points];
    	Vector base = getPerpendicularVectorUsingCrossProduct(axis).normalize();
	    
		for(int i = 0; i < points; ++i) {
			double _angle = i*angle + rotation;
			Vector v = base.clone().rotateAroundAxis(normalizedAxis.clone(), _angle)
					.normalize()
					.multiply(radius);
			vertices[i] = center.clone().add(v);
		}
		
		for(int i = 0; i < vertices.length; ++i) {
			Location loc1 = vertices[i];
			Location loc2 = vertices[(i+step)%vertices.length];
			drawLine(particle, loc1, loc2, lineOffset, 0, 0, -1f, 0, 0.05f);
		}
	}
	
	public static void drawLine(Particle particle, Location start, Location end, double step, 
			int amount, float offsetX, float offsetY, float offsetZ, float speed) {
		drawLine(particle, start, end, step, amount, offsetX, offsetY, offsetZ, speed, null);
	}
	
	public static <T> void drawLine(Particle particle, Location start, Location end, double step, 
			int amount, float offsetX, float offsetY, float offsetZ, float speed, @Nullable T data) {
		Vector dir = new Vector(
				end.getX() - start.getX(),
				end.getY() - start.getY(),
				end.getZ() - start.getZ()
		).normalize().multiply(step);
		Location loc = start.clone();
		while(start.distanceSquared(loc) <= start.distanceSquared(end)) {
			loc.getWorld().spawnParticle(particle, loc, amount, offsetX, offsetY, offsetZ, speed, data);
			loc.add(dir);
		}
	}
	
	public static Vector getPerpendicularVectorUsingCrossProduct(Vector mainAxis) {
	    Vector arbitraryVector = new Vector(1, 0, 0);

	    if (mainAxis.getX() == 1 && mainAxis.getY() == 0 && mainAxis.getZ() == 0) {
	        arbitraryVector = new Vector(0, 1, 0);
	    }

	    return mainAxis.clone().crossProduct(arbitraryVector).normalize();
	}
	
	public static void setEntityBuff(LivingEntity entity, EpicModifierTypes modifier) {
		if(hasEntityBuff(entity, modifier))
			return;
		
		if(entity instanceof Player) {
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer((Player) entity);
			rpg.getModifiers().addActiveModifier(modifier);
		} else {
			AbstractEntity ae = BukkitAdapter.adapt(entity);
			ae.setMetadata(modifier.name(), true);
		}
	}
	
	public static void unsetEntityBuff(LivingEntity entity, EpicModifierTypes modifier) {
		if(!hasEntityBuff(entity, modifier))
			return;
		
		if(entity instanceof Player) {
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer((Player) entity);
			rpg.getModifiers().removeActiveModifier(modifier);
		} else {
			AbstractEntity ae = BukkitAdapter.adapt(entity);
			ae.removeMetadata(modifier.name());
		}
	}
	
	public static boolean hasEntityBuff(LivingEntity entity, EpicModifierTypes modifier) {
		if(entity instanceof Player) {
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer((Player) entity);
			if(rpg == null)
				return false;
			return rpg.getModifiers().hasActiveModifier(modifier);
		} else {
			AbstractEntity ae = BukkitAdapter.adapt(entity);
			return ae.hasMetadata(modifier.name());
		}
	}
	
	public static void setEntityEffect(LivingEntity caster, LivingEntity entity, RuneEffectType effect) {
		if(hasEntityEffect(caster, entity, effect))
			return;
		
		AbstractEntity ae = BukkitAdapter.adapt(entity);
		ae.setMetadata(effect.name(), caster);
	}
	
	public static void unsetEntityEffect(LivingEntity entity, RuneEffectType effect) {
		if(!hasEntityEffect(entity, effect))
			return;
		
		AbstractEntity ae = BukkitAdapter.adapt(entity);
		ae.removeMetadata(effect.name());
	}
	
	public static boolean hasEntityEffect(LivingEntity caster, LivingEntity entity, RuneEffectType effect) {
		AbstractEntity ae = BukkitAdapter.adapt(entity);
		if(!ae.hasMetadata(effect.name()))
			return false;
		
		Object obj = ae.getMetadata(effect.name()).get();
		return obj instanceof LivingEntity && obj.equals(caster);
	}
	
	public static boolean hasEntityEffect(LivingEntity entity, RuneEffectType effect) {
		AbstractEntity ae = BukkitAdapter.adapt(entity);
		return ae.hasMetadata(effect.name()) && ae.getMetadata(effect.name()).get() instanceof LivingEntity;
	}
	
	public static LivingEntity getEntityEffectCaster(LivingEntity entity, RuneEffectType effect) {
		if(!hasEntityEffect(entity, effect))
			return null;
		
		AbstractEntity ae = BukkitAdapter.adapt(entity);
		return (LivingEntity) ae.getMetadata(effect.name()).get();
	}
	
	public static boolean isItemSoulbinded(ItemStack it) {
		EpicComponent comp = new EpicComponent(it, MythicBukkit.inst());
		return comp.hasKey("soulbind");
	}
	
	public static boolean isItemSoulbindedToPlayer(ItemStack it, Player player) {
		return isItemSoulbindedToPlayer(it, player.getUniqueId());
	}
	
	public static boolean isItemSoulbindedToPlayer(ItemStack it, UUID uid) {
		if(!isItemSoulbinded(it))
			return false;

		EpicComponent comp = new EpicComponent(it, MythicBukkit.inst());
		UUID soulbinded = comp.getUUID("soulbind");
		return soulbinded != null && soulbinded.equals(uid);
	}
	
	public static void setItemSoulbinded(ItemStack it, Player player) {
		if(isItemSoulbinded(it))
			return;
		
		ItemMeta im = it.getItemMeta();
		List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<>();
		lore.add(" ");
		lore.add("§aPrzypisanie: §e§o"+player.getName());
		lore.add(" ");
		im.setLore(lore);
		it.setItemMeta(im);
		
		EpicComponent itComp = new EpicComponent(it, MythicBukkit.inst());
		itComp.setUUID("soulbind", player.getUniqueId());
		itComp.setUUID("random", UUID.randomUUID());
		itComp.applyTo(it);
	}
	
	public static Player getSoulbindedPlayer(ItemStack it) {
		if(!isItemSoulbinded(it))
			return null;
		
		EpicComponent comp = new EpicComponent(it, MythicBukkit.inst());
		UUID uid = comp.getUUID("soulbind");
		
		return Bukkit.getPlayer(uid);
	}
	
	public static UUID getSoulbindedUUID(ItemStack it) {
		if(!isItemSoulbinded(it))
			return null;
		
		EpicComponent comp = new EpicComponent(it, MythicBukkit.inst());
		UUID uid = comp.getUUID("soulbind");
		
		return uid;
	}
	
	public static boolean canUseItem(ItemStack it, Player player) {
		if(isItemSoulbinded(it) && !isItemSoulbindedToPlayer(it, player))
			return false;
		
		return true;
	}

	public static boolean isItemCrafted(ItemStack it) {
		EpicComponent comp = new EpicComponent(it, MythicBukkit.inst());
		return comp.hasKey("crafted_by");
	}
	
	public static boolean isItemCraftedByPlayer(ItemStack it, Player player) {
		return isItemCraftedByPlayer(it, player.getUniqueId());
	}
	
	public static boolean isItemCraftedByPlayer(ItemStack it, UUID uid) {
		if(!isItemCrafted(it))
			return false;

		EpicComponent comp = new EpicComponent(it, MythicBukkit.inst());
		UUID craftedBy = comp.getUUID("crafted_by");
		return craftedBy != null && craftedBy.equals(uid);
	}
	
	public static void setItemCraftedBy(ItemStack it, Player player) {
		if(isItemCrafted(it))
			return;
		
		ItemMeta im = it.getItemMeta();
		List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<>();
		lore.add(" ");
		lore.add("§aStworzyl: §7§o"+player.getName());
		lore.add(" ");
		im.setLore(lore);
		it.setItemMeta(im);
		
		EpicComponent itComp = new EpicComponent(it, MythicBukkit.inst());
		itComp.setUUID("crafted_by", player.getUniqueId());
		itComp.applyTo(it);
	}
	
	public static Player getItemCrafterPlayer(ItemStack it) {
		if(!isItemCrafted(it))
			return null;
		
		EpicComponent comp = new EpicComponent(it, MythicBukkit.inst());
		UUID uid = comp.getUUID("crafted_by");
		
		return Bukkit.getPlayer(uid);
	}
	
	public static UUID getItemCrafterUUID(ItemStack it) {
		if(!isItemCrafted(it))
			return null;
		
		EpicComponent comp = new EpicComponent(it, MythicBukkit.inst());
		UUID uid = comp.getUUID("crafted_by");
		
		return uid;
	}
	
	private static final Collection<String> playerAlliesFactions = Set.of(
			"DEFENDERS",
			"SUMMONS",
			"NEUTRAL",
			"NEUTRAL_ANIMALS"
	);
	
	public static boolean isEntityAlly(LivingEntity checking, LivingEntity checked) {
		// Can't attack yourself
        if (checking.equals(checked)) {
            return true;
        }

        AbstractEntity abstractChecking = BukkitAdapter.adapt(checking);
        AbstractEntity abstractChecked = BukkitAdapter.adapt(checked);

        boolean checkingIsAllyMob = isEntityMythicMobAlly(abstractChecking);
        boolean checkedIsAllyMob = isEntityMythicMobAlly(abstractChecked);
        
        if(SummonManager.get().isSummon(abstractChecking))
        	checking = SummonManager.get().getSummonOwner(abstractChecking);
        if(SummonManager.get().isSummon(abstractChecked))
        	checked = SummonManager.get().getSummonOwner(abstractChecked);
        
        boolean checkingIsPlayer = checking instanceof Player;
        boolean checkedIsPlayer = checked instanceof Player;
        
        if(!(checkingIsAllyMob || checkingIsPlayer) || !(checkedIsAllyMob || checkedIsPlayer))
        	return false;

        boolean checkingInPvp = isInPvPZone(checking);
        boolean checkedInPvp = isInPvPZone(checked);
        boolean pvpAllowed = checkingInPvp && checkedInPvp;

        if(checkingIsPlayer && checkedIsPlayer) {
        	Player p1 = (Player) checking;
        	Player p2 = (Player) checked;
        	if(p1.getGameMode() == GameMode.SPECTATOR || p1.getGameMode() == GameMode.CREATIVE
        			|| p2.getGameMode() == GameMode.SPECTATOR || p2.getGameMode() == GameMode.CREATIVE)
        		return true;
        	
        	if(!pvpAllowed)
        		return true;
        	
        	PlayersAllyCheckEvent event = new PlayersAllyCheckEvent(p1, p2);
        	Bukkit.getPluginManager().callEvent(event);
        	return event.isAllies();
        }

        return true;
	}
	
	public static boolean isEntityMythicMobAlly(AbstractEntity entity) {
		if(!MythicBukkit.inst().getMobManager().isActiveMob(entity))
			return false;
		
		ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity);
		if(aMob == null || aMob.isDead())
			return false;
		
		String faction = aMob.getFaction();
		return faction != null && playerAlliesFactions.contains(faction.toUpperCase());
	}
	
	public static boolean isInPvPZone(Entity entity) {
        RegionQuery query = WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer()
                .createQuery();
        
        ApplicableRegionSet set = query.getApplicableRegions(
                com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(entity.getLocation()));
        State flag = set.queryValue(null, Flags.PVP);
        String worldName = entity.getWorld().getName().toLowerCase();
        boolean isDungeonOrRaid = worldName.contains("dungeon") || worldName.contains("raid");
            
        return flag == State.ALLOW && !isDungeonOrRaid;
	}
	
}
