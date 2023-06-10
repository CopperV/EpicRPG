package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;

public class Swiatlo extends ARune {

	public Swiatlo(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1.5f);
		//Particles
		new BukkitRunnable() {
			
			int timer = dr.getDurationTime()*10;
			
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				--timer;
				Location light = p.getEyeLocation().add(0, 1.25, 0);
				light.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, light, 5, 0.15f, 0.15f, 0.15f, 0.1f);
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
		
		//Light
		new BukkitRunnable() {
			
			int timer = dr.getDurationTime();
			
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
				
				p.getWorld().getNearbyEntities(p.getLocation(), 8, 8, 8).stream().filter(e -> {
					return e instanceof Player;
				}).forEach(e -> {
					Player tmp = (Player) e;
					tmp.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20, 0));
				});
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
//		
	}

}
