package me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna;

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

public class PrzyplywEnergii extends ACastableRune {

	private Random rand = new Random();
	
	public PrzyplywEnergii(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.PRZYPLYW_ENERGII,
				player,
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1.1f, 0.7f);
				}, 
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1f);
				
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.01, 0.05);
					_loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, _loc, 20, 0.4f, 0.8f, 0.4f, force);
				},
				new TimingRuneEffect(4, target -> {
					Location _loc = target.getLocation().clone().add(0, 0.1, 0);

					double r = 0.7;
					for(int i = 0; i < 12; ++i) {
						double angle = rand.nextDouble(Math.PI * 2);
						
						double x = r * Math.sin(angle);
						double z = r * Math.cos(angle);

						double force = rand.nextDouble(0.03, 0.12);
						
						Location point = _loc.clone().add(x,0,z);
						point.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, point, 0, 0, 1, 0, force);
					}
					
					_loc.getWorld().spawnParticle(Particle.WITCH, _loc.clone().add(0, 0.9, 0), 3,
							0.3f, 0.8f, 0.3f, 0.05f);
				}));
	}

}
