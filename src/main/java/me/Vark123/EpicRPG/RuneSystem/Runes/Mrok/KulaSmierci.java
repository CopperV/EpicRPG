package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class KulaSmierci extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public KulaSmierci(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.8, -0.8, -0.8, 
				0.8, 0.8, 0.8);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.45, 
				2,
				1,
				32, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_SKELETON_HORSE_HURT, 1.2f, 0.5f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 10, 
							0.5f, 0.5f, 0.5f, 0.03f);
					loc.getWorld().spawnParticle(Particle.LAVA, loc, 3, 
							0.3f, 0.3f, 0.3f, 0.05f);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune)) {
						e.getWorld().playSound(loc, Sound.ENTITY_SKELETON_DEATH, 0.9f, 0.5f);

						loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 19, 
								0.7f, 0.7f, 0.7f, 0.07f);
						loc.getWorld().spawnParticle(Particle.LAVA, loc, 10, 
								0.5f, 0.5f, 0.5f, 0.12f);
					}
				}, 
				loc -> { });
	}

}
