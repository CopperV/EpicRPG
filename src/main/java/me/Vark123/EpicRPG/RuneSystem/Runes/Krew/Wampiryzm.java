package me.Vark123.EpicRPG.RuneSystem.Runes.Krew;

import java.util.Collection;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;

public class Wampiryzm extends ACastableRune {
	
	private static final DustOptions dust = new DustOptions(Color.fromRGB(224, 32, 32), 0.8f);
	
	public Wampiryzm(RpgPlayer rpgPlayer, EpicRune rune, Collection<RuneLockerTypes> lockers) {
		super(rpgPlayer, rune, lockers);
	}


	private static final Random rand = new Random();
	

	@Override
	public void castSpell() {
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.WAMPIRYZM,
				player,
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1f);
				}, 
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1.6f);
				
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.01, 0.05);
					_loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, _loc, 20, 0.4f, 0.8f, 0.4f, force);
				},
				new TimingRuneEffect(4, target -> {
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.1, 0.24);
					_loc.getWorld().spawnParticle(Particle.DUST, _loc, 4, 0.4f, 0.9f, 0.4f, force, dust);
				}));
	}

}
