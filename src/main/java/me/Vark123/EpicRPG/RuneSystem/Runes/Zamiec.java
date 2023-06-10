package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Collection;
import java.util.Date;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.RuneManager;

public class Zamiec extends ARune {

	public Zamiec(ItemStackRune dr, Player p) {
		super(dr, p);
		this.modifier2 = true;
	}

	@Override
	public void castSpell() {
		
		//DZWIEK
		new BukkitRunnable() {
			
			int timer = dr.getDurationTime()/4;
			int t = 0;
			
			@Override
			public void run() {
				if(t > timer || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				++t;
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_MINECART_INSIDE, .6F, 1.35F);
			}
		}.runTaskTimer(Main.getInstance(), 0, 20*4);
		
		new BukkitRunnable() {
			double t = 0;
			double timer = dr.getDurationTime();
			@Override
			public void run() {
				if(t > timer || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				++t;
				Location loc = p.getLocation();
				Location particle = loc.clone();
				for(int i = 0; i < 12*dr.getObszar(); ++i) {
					randomParticle(particle);
				}
				
				Collection<Entity> tmpList = loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar());
				tmpList.stream().filter(e -> {
					if(e.equals(p) || !(e instanceof LivingEntity))
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
					loc.getWorld().playSound(e.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 0.75F, .5F);
					RuneDamage.damageNormal(p, (LivingEntity) e, dr);
				});
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);

		RuneManager.getInstance().getObszarowkiCd().put(p, new Date());
	}
	
	private void randomParticle(Location loc) {
		double theta1 = Math.random() * Math.PI * 2;
		double theta2 = Math.random() * Math.PI * 2;
		double x = dr.getObszar() * Math.sin(theta1);
		double y = Math.random()*4+-2;
		double z = dr.getObszar() * Math.cos(theta2);
		Vector v = Vector.getRandom().normalize();
		loc.add(x, y, z);
		loc.getWorld().spawnParticle(Particle.CLOUD, loc, 0, v.getX(), v.getY(), v.getZ());
	}

}
