package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;

public class SekretWielkanocy extends ACastableRune {

	private static final Random rand = new Random();
	
	private PotionEffect effect1;
	private PotionEffect effect2;
	
	public SekretWielkanocy(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		effect1 = new PotionEffect(PotionEffectType.SATURATION, rune.getDurationTime()*20, 19);
		effect2 = new PotionEffect(PotionEffectType.SPEED, rune.getDurationTime()*20, 1);
	}

	@Override
	public void castSpell() {
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.SEKRET_WIELKANOCY,
				player,
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.5f, 1.5f);
				
					target.addPotionEffect(effect1);
					target.addPotionEffect(effect2);
				}, 
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1, 1.7f);
				
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.01, 0.05);
					_loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, _loc, 20, 0.4f, 0.8f, 0.4f, force);
				},
				new TimingRuneEffect(4, target -> {
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.01, 0.07);
					_loc.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, _loc, 7, 
							0.4f, 0.9f, 0.4f, force);
				}));
	}

}
