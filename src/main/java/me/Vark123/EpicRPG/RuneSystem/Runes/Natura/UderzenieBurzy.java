package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class UderzenieBurzy extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public UderzenieBurzy(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.9, -0.9, -0.9, 
				0.9, 0.9, 0.9);
	}

	@Override
	public void castSpell() {
		ACastableRune castableRune = this;
		
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		Vector projDir = startLoc.getDirection().normalize();
		Vector throwDir = projDir.clone().setY(1.8).normalize().multiply(3.5);
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				projDir,
				0.5, 
				3,
				1,
				40, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_BREEZE_IDLE_GROUND, 1f, 0.6f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.CLOUD, loc, 17, 
							0.3f, 0.3f, 0.3f, 0.06f);
					loc.getWorld().spawnParticle(Particle.WHITE_SMOKE, loc, 12, 
							0.3f, 0.3f, 0.3f, 0.06f);
					loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 2, 
							0.2f, 0.2f, 0.2f, 0.02f);
				}, 
				hitCondition, 
				(loc, e) -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_ENDER_DRAGON_FLAP, 1.5f, 0.2f);
					loc.getWorld().playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, (float) (Math.random() * 0.5 + 1));
					loc.getWorld().spawnParticle(Particle.CLOUD, loc, 40, 
							1.5f, 1.5f, 1.5f, 0.12f);
					loc.getWorld().spawnParticle(Particle.WHITE_SMOKE, loc, 35, 
							1.5f, 1.5f, 1.5f, 0.1f);
					loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 15, 
							1.3f, 1.3f, 1.3f, 0.03f);
					
					InstantRangeRuneTemplate.castEffect(
							castableRune,
							loc, 
							6.5, 
							0,
							0,
							_loc -> { }, 
							hitCondition,
							(_loc, entity) -> {
								if(RuneUtils.damage(player, entity, rune)) {
									entity.setVelocity(throwDir);
								}
							});
					
				}, 
				loc -> { });
	}

}
