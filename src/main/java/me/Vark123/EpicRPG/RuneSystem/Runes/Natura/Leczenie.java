package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

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

public class Leczenie extends ACastableRune {

	private static final Random random = new Random();
	
	public Leczenie(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		RpgStats stats = rpgPlayer.getStats();
		
		double hpToHeal = 0.35 * stats.getFinalMana() + 0.6 * stats.getFinalInteligencja();
		
		RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpgPlayer, hpToHeal);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		
		Location loc = player.getLocation().clone().add(0,0.1,0);
		player.getWorld().playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1.2f, 1.2f);
		
		double hpParticles = Math.max(hpToHeal, 1);
		for(int i = 0; i < hpParticles; ++i) {
			double radius = random.nextDouble(1.25);
			double angle = random.nextDouble(Math.PI * 2);
			
			double x = radius * Math.sin(angle);
			double z = radius * Math.cos(angle);
			double force = random.nextDouble(0.02, 0.15);
			loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, loc.clone().add(x,0,z), 0,
					0, 1, 0, force);
		}
		for(int i = 0; i < 16; ++i) {
			double radius = random.nextDouble(1.25);
			double angle = random.nextDouble(Math.PI * 2);
			
			double x = radius * Math.sin(angle);
			double z = radius * Math.cos(angle);
			double force = random.nextDouble(0.02, 0.15);
			loc.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, loc.clone().add(x,0,z), 0,
					0, 1, 0, force);
		}
		
	}

}
