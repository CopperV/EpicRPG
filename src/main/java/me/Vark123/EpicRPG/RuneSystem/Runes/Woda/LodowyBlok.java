package me.Vark123.EpicRPG.RuneSystem.Runes.Woda;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;

public class LodowyBlok extends ACastableRune {

	public LodowyBlok(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.LODOWY_BLOK,
				player,
				target -> {
					castLoc.getWorld().playSound(castLoc, Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1, 1.25f);
				}, 
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 1);
				
					Location _loc = target.getLocation().clone().add(0, 1, 0);
					_loc.getWorld().spawnParticle(Particle.SNOWFLAKE, _loc, 30, 0.4f, 0.8f, 0.4f, 0.1f);
				},
				new TimingRuneEffect(4, target -> {
					castLoc.getWorld().spawnParticle(Particle.SNOWFLAKE, player.getLocation().clone().add(0,1,0), 8, 0.4f, 0.8f, 0.4f, 0.1f);
					castLoc.getWorld().spawnParticle(Particle.ITEM_SNOWBALL, player.getLocation().clone().add(0,1,0), 5, 0.4f, 0.8f, 0.4f, 0.1f);
					castLoc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, player.getLocation().clone().add(0,1,0), 5, 0.4f, 0.8f, 0.4f, 0.03f);
				}));
	}

}
