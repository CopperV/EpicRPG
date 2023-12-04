package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class Spetanie extends ARune {
	
	private List<Entity> shooted;

	public Spetanie(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		shooted = new ArrayList<Entity>();
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1, .1f);
		new BukkitRunnable() {
			Location loc = p.getLocation();
			Location check = p.getLocation();
			double t = 0;
			PotionEffect stun = new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 7);
			PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 1);
			@Override
			public void run() {
				t+=0.65;
				for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/16)) {
					double x = t*Math.sin(theta);
					double z = t*Math.cos(theta);
					loc.add(x, 0.25, z);
					if(loc.distance(check)>dr.getObszar() || !casterInCastWorld()) {
						p.addPotionEffect(slow);
						this.cancel();
						return;
					}
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 2,0.2F,0.2F,0.2F,0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 2,0.2F,0.2F,0.2F,0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 2,0.2F,0.2F,0.2F,0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 2,0.2F,0.2F,0.2F,0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 2,0.2F,0.2F,0.2F,0.05F);
					loc.add(0,0.25,0);
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 2,0.2F,0.2F,0.2F,0.05F);
					
					loc.subtract(x,1.5,z);
				}
				
				loc.getWorld().getNearbyEntities(loc, t, t, t, e -> {
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(shooted.contains(e))
						return false;
					if(e.getLocation().distance(loc) > t)
						return false;
					if(e instanceof Player) {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
						State flag = set.queryValue(null, Flags.PVP);
						if(flag != null && flag.equals(State.ALLOW)
								&& !e.getWorld().getName().toLowerCase().contains("dungeon"))
							return true;
						return false;
					}
					if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
						return false;
					return true;
				}).stream().forEach(e -> {
					((LivingEntity) e).addPotionEffect(stun);
					if(RuneDamage.damageNormal(p, (LivingEntity) e, dr)) {
						loc.getWorld().playSound(e.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1, 1);
						shooted.add(e);
						spellEffect(e);
					}
				});
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
	}
	
	private void spellEffect(Entity e) {
		new BukkitRunnable() {
			double timer = dr.getDurationTime()*4;
			double _45deg = Math.PI/4.0;
			double r = 0.75;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
				if(e.isDead()) {
					this.cancel();
					return;
				}
				Location loc = e.getLocation().add(0, 0.75, 0);
				for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/16)) {
					double x = r*Math.cos(theta)*Math.cos(_45deg);
					double z = r*Math.cos(theta)*Math.sin(_45deg);
					double y = r*Math.sin(theta);
					loc.add(x, y, z);
					p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 1,0,0,0,0);
					loc.subtract(x,y,z);
				}
				for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/16)) {
					double x = r*Math.cos(theta)*Math.cos(-_45deg);
					double z = r*Math.cos(theta)*Math.sin(-_45deg);
					double y = r*Math.sin(theta);
					loc.add(x, y, z);
					p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 1,0,0,0,0);
					loc.subtract(x,y,z);
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}

}
