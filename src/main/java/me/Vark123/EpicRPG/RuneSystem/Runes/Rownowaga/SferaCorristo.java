package me.Vark123.EpicRPG.RuneSystem.Runes.Rownowaga;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class SferaCorristo extends ACastableRune {
	
	private IRuneHitCondition hitCondition;

	public SferaCorristo(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}
	
	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 0.5, 0);
		InstantRangeRuneTemplate.castEffect(
				this,
				startLoc,
				rune.getObszar(),
				0,
				0,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ITEM_MACE_SMASH_GROUND_HEAVY, 1.1f, 0.65f);
					
					double radius = rune.getObszar() * 0.8;
					loc.getWorld().spawnParticle(Particle.END_ROD, loc, 100, radius, radius, radius, 0.06);
					loc.getWorld().spawnParticle(Particle.DUST_PLUME, loc, 100, radius, radius, radius, 0.02);
					loc.getWorld().spawnParticle(Particle.FIREWORK, loc, 100, radius, radius, radius, 0.01);
				},
				hitCondition,
				(loc, entity) -> {
					RuneUtils.damage(player, entity, rune);
				});
	}

}
