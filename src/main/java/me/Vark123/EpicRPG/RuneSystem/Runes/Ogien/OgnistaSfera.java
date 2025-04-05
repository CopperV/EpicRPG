package me.Vark123.EpicRPG.RuneSystem.Runes.Ogien;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;

public class OgnistaSfera extends ACastableRune {

	private Random rand = new Random();
	
	public OgnistaSfera(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.SFERA,
				player,
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 0.6f);
				}, 
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1, 0.8f);
				
					int points = 12;
					Location loc = target.getLocation().clone().add(0, 0.1, 0);
					for(int i = 0; i < points; ++i) {
						double angle = (i * 2 * Math.PI) / (double) points;
						double radius = 1.5;
						
						double x = Math.sin(angle) * radius;
						double z = Math.cos(angle) * radius;
						Location p = loc.clone().add(x,0,z);
						
						double force = rand.nextDouble(0.02, 0.3);
						loc.getWorld().spawnParticle(Particle.SMOKE, p, 0, 0, 1, 0, force);
					}
				},
				new TimingRuneEffect(1, target -> {
					int points = 6;
					Location loc = target.getLocation().clone().add(0, 0.1, 0);
					double radius = 1.5;
					
					for(int i = 0; i < points; ++i) {
						double angle = rand.nextDouble(2*Math.PI);
						
						double x = Math.sin(angle) * radius;
						double z = Math.cos(angle) * radius;
						Location p = loc.clone().add(x,0,z);
						
						double force = rand.nextDouble(0.01, 0.1);
						loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, p, 0, 0, 1, 0, force);
					}
				}));
	}

}
