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

public class SmiercOzywiencom extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public SmiercOzywiencom(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.75, -0.75, -0.75, 
				0.75, 0.75, 0.75);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.48, 
				2,
				1,
				35, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_BREEZE_SHOOT, 1f, 0.5f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 3, 
							0.25f, 0.25f, 0.25f, 0f);
					loc.getWorld().spawnParticle(Particle.SMOKE, loc, 7, 
							0.1f, 0.1f, 0.1f, 0.08f);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune)) {
						e.getWorld().playSound(loc, Sound.ENTITY_BREEZE_SHOOT, 0.9f, 1.25f);

						loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 7, 
								0.4f, 0.4f, 0.4f, 0.04f);
						loc.getWorld().spawnParticle(Particle.SMOKE, loc, 13, 
								0.15f, 0.15f, 0.15f, 0.1f);
					}
				}, 
				loc -> { });
	}

}
