package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class EksplodujacaStrzalaEffectListener implements Listener {

	private static final Random rand = new Random();
	private static final IRuneHitCondition hitCondition = new PvPRuneHitCondition();

	private static final double DEFAULT_EXPLOSION_DMG_MOD = 0.2;
	private static final double DEFAULT_EXPLOSION_DMG_MOD_H = 0.26;
	private static final double DEFAULT_EXPLOSION_DMG_MOD_M = 0.35;

	@EventHandler
	public void onMod(EpicAttackEvent e) {
		if (e.isCancelled())
			return;

		if (!e.getDamageType().equals(DamageType.PROJECTILE))
			return;

		LivingEntity damager = e.getDamager();
		if (!(damager instanceof Player))
			return;

		Entity projectile = e.getDamageSource().getDirectEntity();
		if (!(projectile instanceof Projectile))
			return;
		ItemStack bow = (ItemStack) projectile.getMetadata("rpg_bow").get(0).value();

		if (Utils.hasEntityBuff(damager, EpicModifierTypes.EKSPLODUJACA_STRZALA)) {
			if (bow.getType().equals(Material.CROSSBOW)) {
				int enchant = bow.getEnchantmentLevel(Enchantment.QUICK_CHARGE);
				e.increaseModifier(0.21 - 0.025 * enchant);
			} else {
				e.increaseModifier(0.16);
			}
		} else if (Utils.hasEntityBuff(damager, EpicModifierTypes.EKSPLODUJACA_STRZALA_H)) {
			if (bow.getType().equals(Material.CROSSBOW)) {
				int enchant = bow.getEnchantmentLevel(Enchantment.QUICK_CHARGE);
				e.increaseModifier(0.26 - 0.03 * enchant);
			} else {
				e.increaseModifier(0.2);
			}
		} else if (Utils.hasEntityBuff(damager, EpicModifierTypes.EKSPLODUJACA_STRZALA_M)) {
			if (bow.getType().equals(Material.CROSSBOW)) {
				int enchant = bow.getEnchantmentLevel(Enchantment.QUICK_CHARGE);
				e.increaseModifier(0.32 - 0.035 * enchant);
			} else {
				e.increaseModifier(0.25);
			}
		}

	}

	@EventHandler
	public void onMod(EpicPostDamageEffectEvent e) {
		if (e.isCancelled())
			return;

		if (!e.getDamageType().equals(DamageType.PROJECTILE))
			return;

		LivingEntity damager = e.getDamager();
		if (!(damager instanceof Player))
			return;

		Entity arrow = e.getDamageSource().getDirectEntity();
		if (!arrow.hasMetadata("rpg_bow") || !arrow.hasMetadata("rpg_force"))
			return;

		double modifier = 0;
		double radius = 0;
		if (Utils.hasEntityBuff(damager, EpicModifierTypes.EKSPLODUJACA_STRZALA)) {
			modifier = DEFAULT_EXPLOSION_DMG_MOD;
			radius = 4;
		} else if (Utils.hasEntityBuff(damager, EpicModifierTypes.EKSPLODUJACA_STRZALA_H)) {
			modifier = DEFAULT_EXPLOSION_DMG_MOD_H;
			radius = 5;
		} else if (Utils.hasEntityBuff(damager, EpicModifierTypes.EKSPLODUJACA_STRZALA_M)) {
			modifier = DEFAULT_EXPLOSION_DMG_MOD_M;
			radius = 6;
		}

		if (modifier <= 0)
			return;

		ItemStack bow = (ItemStack) arrow.getMetadata("rpg_bow").get(0).value();
		if (bow.getType().equals(Material.CROSSBOW)) {
			int enchant = bow.getEnchantmentLevel(Enchantment.QUICK_CHARGE);
			switch (enchant) {
			case 0:
				modifier *= 1.3;
				break;
			case 1:
				modifier *= 1.15;
				break;
			case 2:
				modifier *= 1;
				break;
			case 3:
				modifier *= 0.85;
				break;
			case 4:
				modifier *= 0.7;
				break;
			case 5:
				modifier *= 0.65;
				break;
			}
		}

		LivingEntity victim = e.getVictim();
		double dmg = e.getFinalDamage() * modifier;
		castExplodeEffect((Player) damager, arrow.getLocation().clone(), radius, dmg, victim);

	}

	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		if (e.isCancelled())
			return;

		if (e.getHitEntity() != null)
			return;

		Projectile proj = e.getEntity();
		if (!(proj instanceof AbstractArrow))
			return;

		AbstractArrow arrow = (AbstractArrow) proj;
		if (!(arrow.getShooter() instanceof Player shooter))
			return;

		if (!arrow.hasMetadata("rpg_bow") || !arrow.hasMetadata("rpg_force"))
			return;

		double modifier = 0;
		double radius = 0;
		if (Utils.hasEntityBuff(shooter, EpicModifierTypes.EKSPLODUJACA_STRZALA)) {
			modifier = DEFAULT_EXPLOSION_DMG_MOD;
			radius = 4;
		} else if (Utils.hasEntityBuff(shooter, EpicModifierTypes.EKSPLODUJACA_STRZALA_H)) {
			modifier = DEFAULT_EXPLOSION_DMG_MOD_H;
			radius = 5;
		} else if (Utils.hasEntityBuff(shooter, EpicModifierTypes.EKSPLODUJACA_STRZALA_M)) {
			modifier = DEFAULT_EXPLOSION_DMG_MOD_M;
			radius = 6;
		}

		if (modifier <= 0)
			return;

		ItemStack bow = (ItemStack) arrow.getMetadata("rpg_bow").get(0).value();
		if (bow.getType().equals(Material.CROSSBOW)) {
			int enchant = bow.getEnchantmentLevel(Enchantment.QUICK_CHARGE);
			switch (enchant) {
			case 0:
				modifier *= 1.3;
				break;
			case 1:
				modifier *= 1.15;
				break;
			case 2:
				modifier *= 1;
				break;
			case 3:
				modifier *= 0.85;
				break;
			case 4:
				modifier *= 0.7;
				break;
			case 5:
				modifier *= 0.65;
				break;
			}
		}

		double dmg = arrow.getDamage() * proj.getMetadata("rpg_force").get(0).asFloat() * modifier;
		castExplodeEffect(shooter, arrow.getLocation().clone(), radius, dmg, null);
	}

	private void castExplodeEffect(Player damager, Location source, double radius, double damage, Entity target) {

		source.getWorld().playSound(source, Sound.ENTITY_GENERIC_EXPLODE, 2.5f, 0.33f);

		for (int i = 0; i < 25; ++i) {
			double r = rand.nextDouble(radius - 1) + 1;
			double theta = rand.nextDouble(Math.PI * 2);
			double x = Math.sin(theta);
			double y = Math.random() * 2.5 + 0.1;
			double z = Math.cos(theta);
			double force = rand.nextDouble(0.02, 0.1) * r;

			Vector vec = new Vector(x, y, z).normalize();
			source.getWorld().spawnParticle(Particle.CRIT, source, 0, vec.getX(), vec.getY(), vec.getZ(), force);
		}

		source.getWorld().getNearbyEntities(source, radius, radius, radius, entity -> {
			if (target != null && target.equals(entity))
				return false;

			if (entity.getLocation().distanceSquared(source) > radius * radius)
				return false;

			if (!(entity instanceof LivingEntity))
				return false;

			LivingEntity le = (LivingEntity) entity;

			return hitCondition.check((Player) damager, le);
		}).forEach(entity -> {
			Location eLoc = entity.getLocation().clone();
			double dist = eLoc.distance(source);
			double mod = Utils.limitValue(0.5, 1, 1 - dist / radius);
			double eDmg = damage * mod;

			if (DamageUtils.applyDirectDamageEffect(damager, (LivingEntity) entity, eDmg,
					org.bukkit.damage.DamageType.ARROW, DamageCause.CUSTOM)) {
				double strength = mod * 2.5;
				Vector dir = new Vector(eLoc.getX() - source.getX(), 0.5, eLoc.getZ() - source.getZ()).normalize()
						.setY(0.75).multiply(strength);
				entity.setVelocity(dir);
			}
		});
		
		if(target != null) {
			Location eLoc = target.getLocation().clone();
			double dist = eLoc.distance(source);
			double mod = Utils.limitValue(0.5, 1, 1 - dist / radius);
			double strength = mod * 2.5;
			Vector dir = new Vector(eLoc.getX() - source.getX(), 0.5, eLoc.getZ() - source.getZ()).normalize()
					.setY(0.75).multiply(strength);
			target.setVelocity(dir);
		}
	}

}
