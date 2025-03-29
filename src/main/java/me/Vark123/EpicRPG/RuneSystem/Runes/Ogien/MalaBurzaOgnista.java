package me.Vark123.EpicRPG.RuneSystem.Runes.Ogien;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Effects.DamageBurnEffect;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRunePostDamageEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class MalaBurzaOgnista extends ACastableRune {
	
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	private IRunePostDamageEffect hitEffect;

	public MalaBurzaOgnista(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				0.5, 0.5, 0.5, 
				0.5, 0.5, 0.5);
		hitEffect = new DamageBurnEffect(4, rune.getDamage() * 0.1, this);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this,
				startLoc,
				startLoc.getDirection().normalize(),
				0.37,
				3,
				1, 
				30,
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_GHAST_SHOOT, 1, 1f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.FLAME, loc, 7, 
							0.5f, 0.5f, 0.5f, 0.01f);
					loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, loc, 1, 
							0.1f, 0.1f, 0.1f, 0.02f);
				},
				hitCondition, 
				(loc, e) -> {
					InstantRangeRuneTemplate.castEffect(
							this,
							loc,
							rune.getObszar(),
							0,
							0,
							_loc -> {
								_loc.getWorld().spawnParticle(Particle.FLAME, _loc, 7, 
										0.5f, 0.5f, 0.5f, 0.01f);
								_loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, _loc, 1, 
										0.1f, 0.1f, 0.1f, 0.02f);
								_loc.getWorld().playSound(_loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1.2f);
							}, 
							hitCondition, 
							(_loc, _e) -> {
								RuneUtils.damage(player, e, rune, hitEffect);
							});
				}, 
				loc -> { });
	}

}
