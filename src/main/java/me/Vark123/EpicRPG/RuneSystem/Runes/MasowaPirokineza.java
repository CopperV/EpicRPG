package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;

public class MasowaPirokineza extends ARune {

	private LivingEntity le;

	public MasowaPirokineza(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, .8f);
		PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 20 * dr.getDurationTime(), 2);
		PotionEffect stun = new PotionEffect(PotionEffectType.SLOW, 20 * dr.getDurationTime(), 7);
		p.addPotionEffect(slow);
		Location loc = p.getLocation();
		
		loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar()).stream().filter(e -> {
			if(e.equals(p) || !(e instanceof LivingEntity))
				return false;
			if(e.getLocation().distance(loc) > dr.getObszar())
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
		}).forEach(e -> {
			le = (LivingEntity) e;
			if (RuneDamage.damageNormal(p, le, dr, (p, le, dr) -> {
				new BukkitRunnable() {
					@Override
					public void run() {
						new BukkitRunnable() {
							int t = 1;

							@Override
							public void run() {
								if (t >= dr.getDurationTime() || !casterInCastWorld()) {
									this.cancel();
									return;
								}
								p.getWorld().spawnParticle(Particle.FLAME, e.getLocation().add(0, 1, 0), 10,
										0.3F, 0.3F, 0.3F, 0);
								if (!RuneDamage.damageTiming(p, le, dr)) {
									this.cancel();
									return;
								}
								;
								t++;
							}
						}.runTaskTimer(Main.getInstance(), 0, 20);
					}
				}.runTaskLater(Main.getInstance(), 20);
			})) {
				le.addPotionEffect(stun);
				e.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
				createLine(loc.clone().add(0, 1, 0), e.getLocation().add(0, 1, 0));
			}
		});
	}

	private void createLine(Location loc1, Location loc2) {
		double space = 0.25;
		Vector p1 = new Vector(loc1.getX(), loc1.getY(), loc1.getZ());
		Vector p2 = new Vector(loc2.getX(), loc2.getY(), loc2.getZ());
		double distance = loc1.distance(loc2);
		Vector vec = p2.clone().subtract(p1).normalize().multiply(space);
		for (double length = 0; length < distance; p1.add(vec), length += space) {
			loc1.getWorld().spawnParticle(Particle.FLAME, p1.getX(), p1.getY(), p1.getZ(), 3, 0.1F, 0.1F, 0.1F, 0.01F);
		}
	}

}
