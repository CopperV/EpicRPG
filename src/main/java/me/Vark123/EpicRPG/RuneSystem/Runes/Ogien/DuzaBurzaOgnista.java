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

public class DuzaBurzaOgnista extends ACastableRune {
	
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	private IRunePostDamageEffect hitEffect;

	public DuzaBurzaOgnista(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.9, -0.9, -0.9, 
				0.9, 0.9, 0.9);
		hitEffect = new DamageBurnEffect(10, rune.getDamage() * 0.12, this);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this,
				startLoc,
				startLoc.getDirection().normalize(),
				0.38,
				3,
				1, 
				30,
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_GHAST_SHOOT, 1, 0.8f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.FLAME, loc, 15, 
							0.8f, 0.8f, 0.8f, 0.02f);
					loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, loc, 7, 
							0.3f, 0.3f, 0.3f, 0.06f);
					loc.getWorld().spawnParticle(Particle.LAVA, loc, 4, 
							0.15f, 0.15f, 0.15f, 0.02f);
					loc.getWorld().spawnParticle(Particle.SMOKE, loc, 2, 
							0.1f, 0.1f, 0.1f, 0.01f);
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
								double points = 128;
								for(int i = 0; i < points; ++i) {
									double angle = i * (Math.PI * 2) / points;
									double x = Math.sin(angle);
									double z = Math.cos(angle);
									
									_loc.getWorld().spawnParticle(Particle.FLAME, _loc, 0,
											x, 0, z, 0.45f);
									_loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, _loc, 0,
											x, 0, z, 0.35f);
									_loc.getWorld().spawnParticle(Particle.SMOKE, _loc, 0,
											x, 0, z, 0.1f);
								}
								_loc.getWorld().spawnParticle(Particle.LAVA, _loc, 27, 
										0.33f, 0.33f, 0.33f, 0.08f);
								_loc.getWorld().playSound(_loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 0.9f);
							}, 
							hitCondition, 
							(_loc, _e) -> {
								RuneUtils.damage(player, _e, rune, hitEffect);
							});
				}, 
				loc -> { });
	}

}
