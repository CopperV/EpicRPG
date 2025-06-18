package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

import java.util.Random;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.RainRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class ZeslanieMroku extends ACastableRune {
	
	private static Random rand = new Random();
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;

	public ZeslanieMroku(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.35, -0.35, -0.35, 
				0.35, 0.35, 0.35);
	}

	@Override
	public void castSpell() {
		RainRuneTemplate.castRain(
				this,
				castLoc,
				rune.getObszar(),
				rune.getDurationTime(),
				0.44, 
				2,
				4, 
				2,
				1,
				(loc1, loc2) -> {
					return new Vector(
							rand.nextDouble() - 0.5,
							rand.nextDouble() * -2.5 - 0.5,
							rand.nextDouble() - 0.5
							).normalize();
				}, 
				35, 
				50,
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_SCREAM, 1.5f, 0.2f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.SMOKE, loc, 7, 
							0.15f, 0.6f, 0.15f, 0.03f);
					loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 2, 
							0.1f, 0.4f, 0.1f, 0.01f);
				}, 
				hitCondition, 
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune)) {
						loc.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_HURT, 0.9f, 0.7f);
					}
					
				}, 
				loc -> { },
				loc -> { },
				loc -> { });
	}

}
