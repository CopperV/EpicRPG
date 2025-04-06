package me.Vark123.EpicRPG.RuneSystem.Runes.Ogien;

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
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class DeszczOgnia extends ACastableRune {
	
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	private IRunePostDamageEffect hitEffect;

	public DeszczOgnia(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.6, -0.6, -0.6, 
				0.6, 0.6, 0.6);
		hitEffect = new DamageBurnEffect(14, rune.getDamage() * 0.1, this);
	}

	@Override
	public void castSpell() {
		RainRuneTemplate.castRain(
				this,
				castLoc,
				rune.getObszar(),
				rune.getDurationTime(),
				0.4, 
				3,
				4, 
				3,
				1,
				(loc1, loc2) -> {
					Vector vec1 = castLoc.getDirection().normalize();
					Vector vec2 = new Vector(0, -4, 0);
					return vec1.add(vec2).normalize();
				}, 
				35, 
				50,
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.BLOCK_PORTAL_TRAVEL, 2, 0.9f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.SMALL_FLAME, loc, 12, 
							0.2f, 0.2f, 0.2f, 0.03f);
					loc.getWorld().spawnParticle(Particle.FLAME, loc, 6, 
							0.2f, 0.2f, 0.2f, 0.03f);
				}, 
				hitCondition, 
				(loc, entity) -> {
					RuneUtils.damage(player, entity, rune, hitEffect);
					
					loc.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 1, 0.75f);
				}, 
				loc -> { },
				loc -> { },
				loc -> { });
	}

}
