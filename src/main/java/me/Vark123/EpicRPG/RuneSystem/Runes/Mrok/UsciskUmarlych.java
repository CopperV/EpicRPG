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

public class UsciskUmarlych extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public UsciskUmarlych(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.82, -0.82, -0.82, 
				0.82, 0.82, 0.82);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.6, 
				1,
				1,
				42, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_HORSE_DEATH, 1.2f, 0.1f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 7, 
							0.4f, 0.4f, 0.4f, 0.04f);
					loc.getWorld().spawnParticle(Particle.RAID_OMEN, loc, 8, 
							0.5f, 0.5f, 0.5f, 0.06f);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune)) {
						e.getWorld().playSound(loc, Sound.ENTITY_SKELETON_HORSE_DEATH, 1.1f, 0.2f);

						loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 12, 
								0.7f, 0.7f, 0.7f, 0.08f);
						loc.getWorld().spawnParticle(Particle.RAID_OMEN, loc, 12, 
								0.8f, 0.8f, 0.8f, 0.1f);
					}
				}, 
				loc -> { });
	}

}
