package me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna;

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

public class Haduoken extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;

	private DustOptions dust = new DustOptions(Color.AQUA, 0.7f);
	
	public Haduoken(RpgPlayer rpgPlayer, EpicRune rune) {
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
				0.75, 
				2,
				1,
				36, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_EVOKER_CAST_SPELL, 1, 0.75f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.DUST, loc, 13, 
							0.2f, 0.2f, 0.2f, 0.05f, dust);
					loc.getWorld().spawnParticle(Particle.WHITE_SMOKE, loc, 3, 
							0.05f, 0.05f, 0.05f, 0.01f);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune))
						loc.getWorld().playSound(loc, Sound.ENTITY_MAGMA_CUBE_JUMP, 1, 0.8f);
				}, 
				loc -> { });
	}

}
