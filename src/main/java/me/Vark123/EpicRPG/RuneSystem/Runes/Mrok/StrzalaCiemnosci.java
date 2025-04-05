package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

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

public class StrzalaCiemnosci extends ACastableRune {

	private DustOptions dust = new DustOptions(Color.PURPLE, 2f);
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public StrzalaCiemnosci(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				0.75, 0.75, 0.75, 
				0.75, 0.75, 0.75);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.3, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.55, 
				2,
				1,
				30, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_STRAY_HURT, 1.2f, 0.5f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 3, 
							0.03f, 0.03f, 0.03f, 0.03f);
					loc.getWorld().spawnParticle(Particle.DUST, loc, 8, 
							0.3f, 0.3f, 0.3f, 0.1f, dust);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune)) {
						e.getWorld().playSound(loc, Sound.ENTITY_HUSK_AMBIENT, 1, 0.5f);

						loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 8, 
								0.1f, 0.9f, 0.1f, 0.08f);
						loc.getWorld().spawnParticle(Particle.DUST, loc, 18, 
								0.7f, 0.7f, 0.7f, 0.5f, dust);
					}
				}, 
				loc -> { });
	}

}
