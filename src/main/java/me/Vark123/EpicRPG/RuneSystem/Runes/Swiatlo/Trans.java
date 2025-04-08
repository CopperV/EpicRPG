package me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo;

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

public class Trans extends ACastableRune {

	private Random rand = new Random();
	
	public Trans(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.TRANS,
				player,
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 1.2f, 0.7f);
				}, 
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1f);
				
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.01, 0.05);
					_loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, _loc, 20, 0.4f, 0.8f, 0.4f, force);
				},
				new TimingRuneEffect(4, target -> {
					Location _loc = target.getLocation().clone().add(0, 0.05, 0);

					for(int i = 0; i < 12; ++i) {
						double radius = rand.nextDouble(0.8);
						double angle = rand.nextDouble(Math.PI*2);
						double force = rand.nextDouble(0, 0.1);
						
						double x = radius * Math.sin(angle);
						double z = radius * Math.cos(angle);
						
						_loc.getWorld().spawnParticle(Particle.TRIAL_OMEN, _loc.clone().add(x,0,z), 0,
								0, 1, 0, force);
					}
				}));
	}

}
