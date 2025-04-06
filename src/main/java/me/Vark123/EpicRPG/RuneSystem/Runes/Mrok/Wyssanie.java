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

public class Wyssanie extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public Wyssanie(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.5, -0.5, -0.5, 
				0.5, 0.5, 0.5);
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
					loc.getWorld().playSound(loc, Sound.ENTITY_BLAZE_SHOOT, 1f, 1.2f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.SMOKE, loc, 3, 
							0.3f, 0.3f, 0.3f, 0f);
					loc.getWorld().spawnParticle(Particle.WITCH, loc, 7, 
							0.1f, 0.1f, 0.1f, 0.1f);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune));
				}, 
				loc -> { });
	}

}
