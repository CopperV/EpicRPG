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
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class SwietaKrucjata extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public SwietaKrucjata(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.72, -0.72, -0.72, 
				0.72, 0.72, 0.72);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.5, 
				2,
				1,
				40, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.BLOCK_BEACON_POWER_SELECT, 1f, 1.1f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 7, 
							0.25f, 0.25f, 0.25f, 0.04f);
					loc.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 3, 
							0.15f, 0.15f, 0.15f, 0.02f);
				}, 
				hitCondition, 
				(loc, e) -> {
					
					if(RuneUtils.damage(player, e, rune)) {
						e.getWorld().playSound(loc, Sound.ENTITY_ALLAY_HURT, 0.8f, 0.8f);
					}
				}, 
				loc -> { });
	}

}
