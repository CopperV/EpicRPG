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

public class SwietaStrzala extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public SwietaStrzala(RpgPlayer rpgPlayer, EpicRune rune) {
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
					loc.getWorld().playSound(loc, Sound.BLOCK_AMETHYST_BLOCK_HIT, 1.3f, 0.4f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.GLOW, loc, 6, 
							0.15f, 0.15f, 0.15f, 0.01f);
				}, 
				hitCondition, 
				(loc, e) -> {
					
					if(RuneUtils.damage(player, e, rune)) {
						e.getWorld().playSound(loc, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1.3f, 0.4f);
					}
				}, 
				loc -> { });
	}

}
