package me.Vark123.EpicRPG.RuneSystem.Runes.Summons;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLocationEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.SummonRuneTemplate;

public class PrzyzwanieSzkieleta extends ACastableRune {

	private IRuneLocationEffect spawnEffect = loc -> {
		loc.getWorld().playSound(loc, Sound.ENTITY_SKELETON_CONVERTED_TO_STRAY, 1.25f, 0.8f);
		
		double radius = 0.6;
		Random rand = new Random();
		
		for(int i = 0; i < 20; ++i) {
			double angle = rand.nextDouble(Math.PI * 2);
			
			double x = Math.sin(angle) * radius;
			double z = Math.cos(angle) * radius;
			
			Location point = loc.clone().add(x, 0.05, z);
			point.getWorld().spawnParticle(Particle.WHITE_SMOKE, point, 0, 0, 1, 0, rand.nextFloat(0.02f, 0.1f));
		}
		for(int i = 0; i < 10; ++i) {
			double angle = rand.nextDouble(Math.PI * 2);
			
			double x = Math.sin(angle) * radius;
			double z = Math.cos(angle) * radius;
			
			Location point = loc.clone().add(x, 0.05, z);
			point.getWorld().spawnParticle(Particle.SMOKE, point, 0, 0, 1, 0, rand.nextFloat(0.02f, 0.1f));
		}
		
		Location partLoc = loc.clone().add(0, 1, 0);
		partLoc.getWorld().spawnParticle(Particle.DRAGON_BREATH, partLoc, 12, 0.4f, 1.1f, 0.4f, 0.01f);
	};
	
	public PrzyzwanieSzkieleta(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		
		Location spawnLoc = player.getLocation().clone().add(0, 0, 0);
		double radius = 1.5;
		
		SummonRuneTemplate.castSummon(
				this,
				spawnLoc,
				radius,
				spawnEffect, 
				spawnEffect);
		
	}

}
