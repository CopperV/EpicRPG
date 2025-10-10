package me.Vark123.EpicRPG.RuneSystem.Runes.Ogien;

import java.util.Random;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Effects.DamageBurnEffect;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRunePostDamageEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.RainRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.WaveRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class ZwiastunWojny extends ACastableRune {
	
	private Random rand = new Random();
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	private IRunePostDamageEffect hitEffect;

	public ZwiastunWojny(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.1, -0.1, -0.1, 
				0.1, 0.1, 0.1);
		hitEffect = new DamageBurnEffect(6, rune.getDamage() * 0.07, this);
	}

	@Override
	public void castSpell() {
		RainRuneTemplate.castRain(
				this,
				castLoc,
				rune.getObszar(),
				rune.getDurationTime(),
				0.4, 
				2,
				5, 
				2,
				1,
				(loc1, loc2) -> {
					return new Vector(
							rand.nextDouble(-1, 1),
							rand.nextDouble(-0.5, -2),
							rand.nextDouble(-1, 1));
				}, 
				20, 
				30,
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.BLOCK_PORTAL_TRAVEL, 2, 0.6f);
				}, 
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 0.2f, 1);
					loc.getWorld().spawnParticle(Particle.EXPLOSION, loc, 4, 
							0.15f, 0.15f, 0.15f, 0);
				}, 
				(__1, __2) -> false, 
				(__1, __2) -> { }, 
				loc -> { },
				loc -> { },
				loc -> {
					loc.add(0, 0.5, 0);
					
					WaveRuneTemplate.castWave(
							this,
							loc,
							rune.getObszar() * 0.15,
							4,
							0.25,
							1,
							1,
							_loc -> { },
							_loc -> {
								_loc.getWorld().playSound(_loc, Sound.ENTITY_GENERIC_EXPLODE, rand.nextFloat(0.15f, 0.25f), rand.nextFloat(0.8f, 1.5f));
								_loc.getWorld().spawnParticle(Particle.EXPLOSION, _loc, 1, 0.1f, 0.1f, 0.1f, 0.1f);
							}, 
							hitCondition,
							(_loc, entity) -> {
								RuneUtils.damage(player, entity, rune, hitEffect);
							}, 
							_loc -> { });
				});
	}

}
