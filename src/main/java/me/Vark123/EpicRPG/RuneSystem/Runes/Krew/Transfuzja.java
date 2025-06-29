package me.Vark123.EpicRPG.RuneSystem.Runes.Krew;

import java.util.Random;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;

public class Transfuzja extends ACastableRune {

	private Random rand = new Random();
	
	public Transfuzja(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		double angle =Math.PI;
		MutableDouble theta = new MutableDouble(0);
		double radius = 0;
		double thetaStep = Math.PI * 0.0625;
		
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.TRANSFUZJA,
				player,
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_MAGMA_CUBE_JUMP, 1, 0.6f);
				}, 
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1, 1.6f);
				
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.01, 0.05);
					_loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, _loc, 6, 0.4f, 0.8f, 0.4f, force);
				},
				new TimingRuneEffect(2, target -> {
					Location loc1 = target.getLocation().clone().add(0,1,0);
					Location loc2 = loc1.clone();
					
					double x1 = radius * Math.cos(theta.doubleValue()) * Math.cos(angle);
					double z1 = radius * Math.cos(theta.doubleValue()) * Math.sin(angle);
					double x2 = radius * Math.cos(theta.doubleValue()) * Math.cos(-angle);
					double z2 = radius * Math.cos(theta.doubleValue()) * Math.sin(-angle);
					double y = radius * Math.sin(theta.doubleValue());
					
					loc1.add(x1,y,z1);
					loc2.add(x2, y, z2);
					
					target.getWorld().spawnParticle(Particle.HEART, loc1, 1, 0.05f, 0.05f, 0.05f, 0.2f);
					target.getWorld().spawnParticle(Particle.HEART, loc2, 1, 0.05f, 0.05f, 0.05f, 0.2f);
					
					theta.add(thetaStep);
				})
		);
	}

}
