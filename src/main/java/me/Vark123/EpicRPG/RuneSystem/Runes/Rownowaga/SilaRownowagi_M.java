package me.Vark123.EpicRPG.RuneSystem.Runes.Rownowaga;

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

public class SilaRownowagi_M extends ACastableRune {

	private Random rand = new Random();
	
	public SilaRownowagi_M(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.SILA_ROWNOWAGI_M,
				player,
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.5f, 0.8f);
				}, 
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1, 1.6f);
				
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.01, 0.05);
					_loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, _loc, 6, 0.4f, 0.8f, 0.4f, force);
				},
				new TimingRuneEffect(4, target -> {
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					_loc.getWorld().spawnParticle(Particle.INSTANT_EFFECT, _loc, 4, 0.4f, 0.8f, 0.4f, rand.nextDouble(0.01, 0.1));
					_loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, _loc, 3, 0.4f, 0.8f, 0.4f, rand.nextDouble(0.01, 0.05));
				}));
	}

}
