package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class Drenaz extends ARune {

	private static final DustOptions dust = new DustOptions(Color.fromRGB(205, 0, 0), 1.6f);
	private static final Vector horizontalVector = new Vector(0, 1, 0).multiply(0.4);
	
	public Drenaz(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1, .5f);
		p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation().add(0,1,0), 12, 0.4f, 0.6f, 0.4f, 0.1f, dust);
		
		p.getWorld().getNearbyEntities(p.getLocation(), dr.getObszar(), dr.getObszar(), dr.getObszar()).stream()
		.filter(e -> {
			if(e.equals(p) || !(e instanceof LivingEntity))
				return false;
			if(e instanceof Player)
				return false;
			if(e.getLocation().distance(p.getLocation()) > dr.getObszar())
				return false;
			if(!MythicBukkit.inst().getMobManager().isMythicMob(e)
					&& e.getType().equals(EntityType.ARMOR_STAND))
				return false;
			if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
				return false;
			return true;
		}).forEach(this::playEffect);
		
	}
	
	private void playEffect(Entity e) {
		if(!RuneDamage.damageNormal(p, (LivingEntity) e, dr))
			return;

		Location loc = e.getLocation().add(0, 1, 0);
		final Location startLoc = loc.clone();
		p.getWorld().playSound(e.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1, .5f);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(loc.distance(startLoc) >= 4) {
					castHealingProjectile(loc);
					cancel();
					return;
				}
				if(!casterInCastWorld()) {
					cancel();
					return;
				}
				
				loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 4, .2f, .2f, .2f, .1f, dust);
				loc.add(horizontalVector);
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
	private void castHealingProjectile(Location loc) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(!casterInCastWorld()) {
					cancel();
					return;
				}
				
				loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 4, .2f, .2f, .2f, .1f, dust);
				
				Location tmp = p.getLocation().add(0,1,0);
				Vector vec = new Vector(tmp.getX() - loc.getX(),
						tmp.getY() - loc.getY(),
						tmp.getZ() - loc.getZ()).normalize().multiply(0.2);
				loc.add(vec);
				
				if(loc.distance(tmp) > 1.5)
					return;

				RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
				double amount = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*0.07;

				RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, amount);
				Bukkit.getPluginManager().callEvent(event);

				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, .65f);
				p.getWorld().spawnParticle(Particle.HEART, p.getLocation().add(0,1,0), 12, 0.4f, 0.6f, 0.4f, 0.1f);
				cancel();
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
