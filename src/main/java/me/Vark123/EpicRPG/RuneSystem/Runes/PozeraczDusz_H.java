package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class PozeraczDusz_H extends ARune {

	private static final Vector horizontalVector = new Vector(0, 1, 0).multiply(0.4);
	
	public PozeraczDusz_H(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1, .1f);
		p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, p.getLocation().add(0,1,0), 12, 0.4f, 0.6f, 0.4f, 0.02f);
		
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
		p.getWorld().playSound(e.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1, .1f);
		
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
				
				loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 4, .2f, .2f, .2f, .02f);
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
				
				loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 4, .2f, .2f, .2f, .02f);
				
				Location tmp = p.getLocation().add(0,1,0);
				Vector vec = new Vector(tmp.getX() - loc.getX(),
						tmp.getY() - loc.getY(),
						tmp.getZ() - loc.getZ()).normalize().multiply(0.2);
				loc.add(vec);
				
				if(loc.distance(tmp) > 1.5)
					return;

				RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
				int amount = (int) (rpg.getStats().getFinalMana()*0.035);
				rpg.getStats().addPresentManaSmart(amount);

				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, .2f);
				p.getWorld().spawnParticle(Particle.SOUL, p.getLocation().add(0,1,0), 12, 0.4f, 0.6f, 0.4f, 0.1f);
				cancel();
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
