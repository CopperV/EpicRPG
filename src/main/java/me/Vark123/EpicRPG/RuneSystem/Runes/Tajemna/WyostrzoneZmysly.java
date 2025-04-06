package me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;

public class WyostrzoneZmysly extends ACastableRune {

	private Random rand = new Random();
	
	public WyostrzoneZmysly(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.WYOSTRZONE_ZMYSLY,
				player,
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1f);
				}, 
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1f);
				
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.01, 0.05);
					_loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, _loc, 20, 0.4f, 0.8f, 0.4f, force);
				},
				new TimingRuneEffect(4, target -> {
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.1, 0.6);
					_loc.getWorld().spawnParticle(Particle.ENTITY_EFFECT, _loc, 12, 
							0.4f, 0.8f, 0.4f, force, Color.fromRGB(196, 164, 132));
				}));
	}

}
