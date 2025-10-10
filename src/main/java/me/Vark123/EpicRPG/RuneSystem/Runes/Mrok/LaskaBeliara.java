package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

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

public class LaskaBeliara extends ACastableRune {

	private Random rand = new Random();
	
	public LaskaBeliara(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.LASKA_BELIARA,
				player,
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1, 0.6f);
				}, 
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 1.6f);
				
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.01, 0.05);
					_loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, _loc, 6, 0.4f, 0.8f, 0.4f, force);
				},
				new TimingRuneEffect(4, target -> {
					Location _loc = target.getLocation().clone().add(0, 3, 0);

					for(int i = 0; i < 4; ++i) {
						double r = rand.nextDouble(0.5, 1.5);
						double angle = rand.nextDouble(0, Math.PI*2);
						double x = r * Math.sin(angle);
						double z = r * Math.cos(angle);

						double force = rand.nextDouble(0.1, 0.25);
						Location tmp = _loc.clone().add(x, 0, z);
						_loc.getWorld().spawnParticle(Particle.SMOKE, tmp, 0, 0f, -1f, 0f, force);
					}
				}));
	}

}
