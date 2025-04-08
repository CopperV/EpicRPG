package me.Vark123.EpicRPG.RuneSystem.Runes.Chaos;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;

public class RozerwanieDuszy extends ACastableRune {

	private static final Random random = new Random();
	
	public RozerwanieDuszy(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		RpgStats stats = rpgPlayer.getStats();
		
		int manaToTake = (int) Math.min(stats.getPresentMana(), stats.getFinalMana() * 0.05);
		double hpToHeal = manaToTake * 0.85;
		
		RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpgPlayer, hpToHeal);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		stats.removePresentManaSmart(manaToTake);
		
		Location loc = player.getLocation().clone().add(0,0.1,0);
		player.getWorld().playSound(loc, Sound.ENTITY_WITHER_SHOOT, 0.8f, 0.7f);
		
		double manaParticles = Math.max(manaToTake*0.5, 1);
		double hpParticles = Math.max(hpToHeal*0.5, 1);
		for(int i = 0; i < manaParticles; ++i) {
			double radius = random.nextDouble(1.25);
			double angle = random.nextDouble(Math.PI * 2);
			
			double x = radius * Math.sin(angle);
			double z = radius * Math.cos(angle);
			double force = random.nextDouble(0.02, 0.15);
			loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, loc.clone().add(x,0,z), 0,
					0, 1, 0, force);
		}
		for(int i = 0; i < hpParticles; ++i) {
			double radius = random.nextDouble(1.25);
			double angle = random.nextDouble(Math.PI * 2);
			
			double x = radius * Math.sin(angle);
			double z = radius * Math.cos(angle);
			double force = random.nextDouble(0.02, 0.15);
			loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, loc.clone().add(x,0,z), 0,
					0, 1, 0, force);
		}
		
	}

}
