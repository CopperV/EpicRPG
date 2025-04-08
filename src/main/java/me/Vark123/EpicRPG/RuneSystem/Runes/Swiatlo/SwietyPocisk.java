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

public class SwietyPocisk extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public SwietyPocisk(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.6, -0.6, -0.6, 
				0.6, 0.6, 0.6);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		MultipleProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.48, 
				2,
				1,
				40, 
				4,
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ITEM_TRIDENT_RIPTIDE_3, 1f, 1f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 3, 
							0.05f, 0.05f, 0.05f, 0.02f);
					loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 7, 
							0.2f, 0.2f, 0.2f, 0.03f);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune)) {
						e.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_FREEZE, 1, 1.2f);

						loc.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 15, 
								0.1f, 0.1f, 0.1f, 0.1f);
						loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 35, 
								0.5f, 0.5f, 0.5f, 0.1f);
					}
				}, 
				loc -> { });
	}

}
