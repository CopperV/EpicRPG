package me.Vark123.EpicRPG.RuneSystem.Runes.Chaos;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TotemRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class SzalPustki extends ACastableRune {
	
	private IRuneHitCondition hitCondition;
	
	public SzalPustki(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 2, 0);
		
		TotemRuneTemplate.castTotem(
				this,
				startLoc,
				rune.getDurationTime(),
				rune.getObszar(),
				4,
				20,
				loc -> {
					loc.getWorld().playSound(loc, Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1.2f, 0.6f);
				},
				loc -> {
					loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 10,
							0.8, 0.8, 0.8, 0.02);
				},
				hitCondition,
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune)) {
						loc.getWorld().playSound(loc, Sound.ENTITY_ENDER_DRAGON_GROWL, 0.8f, 0.2f);
						Utils.drawLine(Particle.SMOKE, startLoc, entity.getLocation().clone().add(0,1,0), 0.15, 2, 0.05f, 0.05f, 0.05f, 0.01f);
					}
				},
				loc -> { });
	}

}
