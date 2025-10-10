package me.Vark123.EpicRPG.RuneSystem.Runes.Krew;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;

public class ZewKrwi extends ACastableRune {

	private Random rand = new Random();
	
	public ZewKrwi(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		BufferRuneTemplate.castEffect(
				this,
				(castableRune, target) -> {
					double percent = rpgPlayer.getModifiers().getZewKrwiMod() * 100;
					return rune.getName()+" §r§7[§c§o"+((int) percent)+"%§7]";
				},
				EpicModifierTypes.ZEW_KRWI,
				player,
				target -> {
					rpgPlayer.getModifiers().resetZewKrwiMod();
					rpgPlayer.getModifiers().addZewKrwiMod(0.02);
					
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1f, 0.75f);
				}, 
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1, 0.9f);
				
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.01, 0.05);
					_loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, _loc, 20, 0.4f, 0.8f, 0.4f, force);
				});
	}

}
