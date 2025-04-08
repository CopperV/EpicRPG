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

public class WypedzanieZla extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public WypedzanieZla(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.7, -0.7, -0.7, 
				0.7, 0.7, 0.7);
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
					loc.getWorld().playSound(loc, Sound.ENTITY_GHAST_DEATH, 1.3f, 0.7f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.GLOW, loc, 8, 
							0.35f, 0.35f, 0.35f, 0.03f);
					loc.getWorld().spawnParticle(Particle.GLOW_SQUID_INK, loc, 2, 
							0.1f, 0.1f, 0.1f, 0f);
				}, 
				hitCondition, 
				(loc, e) -> {
					
					if(RuneUtils.damage(player, e, rune)) {
						e.getWorld().playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 1f, 0.6f);
					}
				}, 
				loc -> { });
	}

}
