package me.Vark123.EpicRPG.RuneSystem.Runes.Woda;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
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

public class RekaAdanosa extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	private DustOptions dust = new DustOptions(Color.BLUE, 2);
	
	public RekaAdanosa(RpgPlayer rpgPlayer, EpicRune rune) {
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
				0.52, 
				2,
				1,
				60, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 2f, 0.3f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.DUST, loc, 5, 
							0.15f, 0.15f, 0.15f, 0.15f, dust);
				}, 
				hitCondition, 
				(loc, e) -> {
					e.getWorld().playSound(loc, Sound.ENTITY_PLAYER_SPLASH_HIGH_SPEED, 1, 0.5f);
					
					RuneUtils.damage(player, e, rune);
				}, 
				loc -> { });
	}

}
