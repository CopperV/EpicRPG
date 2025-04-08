package me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.MultipleProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class SwietyPlomien extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public SwietyPlomien(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.65, -0.65, -0.65, 
				0.65, 0.65, 0.65);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		MultipleProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.5, 
				2,
				1,
				40, 
				6,
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_SHOOT, 0.8f, 0.8f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, loc, 4, 
							0.1f, 0.1f, 0.1f, 0.03f);
					loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 11, 
							0.33f, 0.33f, 0.33f, 0.04f);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune)) {
						e.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_FREEZE, 1, 1.3f);

						loc.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, loc, 15, 
								0.1f, 0.1f, 0.1f, 0.07f);
						loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 35, 
								0.5f, 0.5f, 0.5f, 0.1f);
					}
				}, 
				loc -> { });
	}

}
