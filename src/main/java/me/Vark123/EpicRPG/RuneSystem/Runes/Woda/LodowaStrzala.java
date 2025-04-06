package me.Vark123.EpicRPG.RuneSystem.Runes.Woda;

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

public class LodowaStrzala extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public LodowaStrzala(RpgPlayer rpgPlayer, EpicRune rune) {
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
				0.4, 
				3,
				1,
				30, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_BAT_TAKEOFF, 1.2f, 0.7f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 6, 
							0.05f, 0.05f, 0.05f, 0.01f);
				}, 
				hitCondition, 
				(loc, e) -> {
					e.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_FREEZE, 1, 1.5f);
					
					RuneUtils.damage(player, e, rune);
				}, 
				loc -> { });
	}

}
