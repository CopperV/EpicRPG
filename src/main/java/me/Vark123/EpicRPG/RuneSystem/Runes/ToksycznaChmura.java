package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
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
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.RuneDamage;

public class ToksycznaChmura extends ARune {

	public ToksycznaChmura(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		
		final Location loc = p.getLocation();
		loc.getWorld().playSound(loc, Sound.ENTITY_SLIME_HURT, 1, 0.5F);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*10;
			DustOptions dust = new DustOptions(Color.fromRGB(0, 187, 0), 3);
			Random rand = new Random();
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
				
				for(int i = 0; i < 35; ++i) {
					Location tmp = loc.clone();
					double x = (rand.nextDouble()*2*dr.getObszar() - dr.getObszar()) * Math.sin(rand.nextDouble()*Math.PI*2);
					double y = (rand.nextDouble()*2*dr.getObszar() - dr.getObszar()) * Math.sin(rand.nextDouble()*Math.PI*2);
					double z = (rand.nextDouble()*2*dr.getObszar() - dr.getObszar()) * Math.sin(rand.nextDouble()*Math.PI*2);
					tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp.add(x,y,z), 1, 0, 0, 0, 0.1f, dust);
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime();
			PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, 20, 2);
			DustOptions dust = new DustOptions(Color.fromRGB(0, 187, 0), 1.25f);
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
				for(Entity entity : loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar())) {
					if(entity.equals(p) || !(entity instanceof LivingEntity))
						continue;
					if(entity.getLocation().distance(loc) > dr.getObszar())
						continue;
					if(entity instanceof Player || entity.hasMetadata("NPC")) {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
						if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon")) {
							continue;
						}
					}
					entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1, 2);
					entity.getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation().add(0,1,0), 12, 0.4f, 0.4f, 0.4f, 0.05f, dust);
					if(RuneDamage.damageNormal(p, (LivingEntity) entity, dr)) {
						((LivingEntity)entity).addPotionEffect(effect);
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
	}

}
