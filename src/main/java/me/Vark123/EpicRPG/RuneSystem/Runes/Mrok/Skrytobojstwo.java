package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

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

public class Skrytobojstwo extends ACastableRune {

	private Random rand = new Random();
	private PotionEffect potion;
	
	public Skrytobojstwo(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		potion = new PotionEffect(PotionEffectType.SPEED, 20*rune.getDurationTime(), 1);
	}

	@Override
	public void castSpell() {
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.SKRYTOBOJSTWO,
				player,
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_VEX_AMBIENT, 3, 0.2f);
					target.addPotionEffect(potion);
				}, 
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_VEX_AMBIENT, 3, 1.8f);
				
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.01, 0.05);
					_loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, _loc, 20, 0.4f, 0.8f, 0.4f, force);
				},
				new TimingRuneEffect(4, target -> {
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.01, 0.04);
					_loc.getWorld().spawnParticle(Particle.WHITE_SMOKE, _loc, 4, 0.4f, 0.8f, 0.4f, force);
				}));
	}

}
