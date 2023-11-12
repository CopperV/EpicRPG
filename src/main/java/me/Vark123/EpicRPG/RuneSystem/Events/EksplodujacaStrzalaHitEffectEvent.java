package me.Vark123.EpicRPG.RuneSystem.Events;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.FightSystem.ManualDamage;
import me.Vark123.EpicRPG.FightSystem.Events.CustomProjectileEntityDamageEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;

public class EksplodujacaStrzalaHitEffectEvent implements Listener {
	
	private static final double DEFAULT_EXPLOSION_DMG_MOD = 0.2;
	private static final double DEFAULT_EXPLOSION_DMG_MOD_H = 0.26;
	private static final double DEFAULT_EXPLOSION_DMG_MOD_M = 0.35;

	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		if(e.isCancelled())
			return;
		
		Projectile proj = e.getEntity();
		if(!(proj instanceof AbstractArrow))
			return;
		
		AbstractArrow arrow = (AbstractArrow) proj;
		if(!(arrow.getShooter() instanceof Player))
			return;
		
		Player p = (Player) arrow.getShooter();
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		if(!modifiers.hasEksplodujacaStrzala()
				&& !modifiers.hasEksplodujacaStrzala_h()
				&& !modifiers.hasEksplodujacaStrzala_m())
			return;
		
		if(!arrow.hasMetadata("rpg_bow") || !arrow.hasMetadata("rpg_force"))
			return;
		
		boolean hitEntity = e.getHitEntity() != null;
		ItemStack bow = (ItemStack) arrow.getMetadata("rpg_bow").get(0).value();
//		double dmg = arrow.getDamage();
		
		MutableDouble baseDmg;
		if(modifiers.hasEksplodujacaStrzala()) {
			baseDmg = new MutableDouble(DEFAULT_EXPLOSION_DMG_MOD);
		} else if(modifiers.hasEksplodujacaStrzala_h()) {
			baseDmg = new MutableDouble(DEFAULT_EXPLOSION_DMG_MOD_H);
		} else {
			baseDmg = new MutableDouble(DEFAULT_EXPLOSION_DMG_MOD_M);
		}
		if(bow.getType().equals(Material.CROSSBOW)) {
			double enchantMod = 0;
			int enchant = bow.getEnchantmentLevel(Enchantment.QUICK_CHARGE);
			switch(enchant) {
				case 0:
					enchantMod = 1.3;
					break;
				case 1:
					enchantMod = 1.15;
					break;
				case 2:
					enchantMod = 1;
					break;
				case 3:
					enchantMod = 1;
					break;
				case 4:
					enchantMod = 0.85;
					break;
				case 5:
					enchantMod = 0.7;
					break;
			}
			baseDmg.setValue(baseDmg.doubleValue()*enchantMod);
		}

		final Location loc = arrow.getLocation();
		loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 3, 0.3F);
		
		double r = 2;
		for(int i = 0; i < 25; ++i) {
			double theta = Math.random()*Math.PI*2;
			double x = r * Math.sin(theta);
			double y = Math.random()*2.5+0.1;
			double z = r * Math.cos(theta);
			Vector v = new Vector(x, y, z).normalize();
			loc.getWorld().spawnParticle(Particle.CRIT, loc.clone().add(v), 0, 
					v.getX()*(-2),v.getY(),v.getZ()*(-2));
		}

		final double dmg = arrow.getDamage() * proj.getMetadata("rpg_force").get(0).asFloat();
		loc.getWorld().getNearbyEntities(loc, 6, 6, 6, entity -> {
			if(entity.equals(p) || !(entity instanceof LivingEntity))
				return false;
			if(hitEntity && entity.equals(e.getHitEntity()))
				return false;
			if(entity instanceof Player) {
				RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
				ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
				State flag = set.queryValue(null, Flags.PVP);
				if(flag != null && flag.equals(State.ALLOW)
						&& !entity.getWorld().getName().toLowerCase().contains("dungeon"))
					return true;
				return false;
			}
			if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(entity).isDamageable())
				return false;
			return true;
		}).forEach(entity -> {
			Location eLoc = entity.getLocation();
			double dist = eLoc.distance(loc);
			double eDmg = dmg * baseDmg.doubleValue() * (24.0 - dist) / 24.0;
			
			CustomProjectileEntityDamageEvent event = new CustomProjectileEntityDamageEvent(proj, entity, eDmg);
			Bukkit.getPluginManager().callEvent(event);
			if(event.isCancelled()) {
				return;
			}
			ManualDamage.doDamageNoCheck(p, (LivingEntity) entity, event.getDamage(), event);
			
			double strength = (18.0 - dist) / 18.0 * 1.25;
			Vector dir = new Vector(eLoc.getX() - loc.getX(),
					0,
					eLoc.getZ() - loc.getZ())
					.normalize()
					.setY(0.8)
					.multiply(strength);
			entity.setVelocity(dir);
		});
	}
	
}
