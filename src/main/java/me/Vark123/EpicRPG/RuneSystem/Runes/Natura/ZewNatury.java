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

public class ZewNatury extends ACastableRune {

	private Random rand = new Random();
	private PotionEffect potion;
	
	public ZewNatury(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		potion = new PotionEffect(PotionEffectType.SPEED, 20*rune.getDurationTime(), 1);
	}

	@Override
	public void castSpell() {
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.ZEW_NATURY,
				player,
				target -> {
					target.addPotionEffect(potion);
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_WOLF_GROWL, 1f, 0.7f);
				}, 
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 0.9f);
				
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.01, 0.05);
					_loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, _loc, 20, 0.4f, 0.8f, 0.4f, force);
				},
				new TimingRuneEffect(4, target -> {
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.01, 0.04);
					_loc.getWorld().spawnParticle(Particle.SCRAPE, _loc, 6,
							0.4, 0.8, 0.4, force);
					_loc.getWorld().spawnParticle(Particle.GLOW, _loc, 9,
							0.4, 0.8, 0.4, force);
				}));
	}

}
