package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class TchnienieSmierci extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	private BlockData particleBlockData;
	
	public TchnienieSmierci(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				0.8, 0.8, 0.8, 
				0.8, 0.8, 0.8);
		
		particleBlockData = Material.COAL_BLOCK.createBlockData();
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
				35, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_HORSE_DEATH, 1f, 0.8f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 8, 
							0.3f, 0.3f, 0.3f, 0.03f);
					loc.getWorld().spawnParticle(Particle.FALLING_DUST, loc, 8, 
							0.3f, 0.3f, 0.3f, 0.05f, particleBlockData);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune)) {
						e.getWorld().playSound(loc, Sound.ENTITY_SKELETON_HORSE_DEATH, 0.8f, 0.8f);

						loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 12, 
								0.45f, 0.45f, 0.45f, 0.08f);
						loc.getWorld().spawnParticle(Particle.FALLING_DUST, loc, 12, 
								0.45f, 0.45f, 0.45f, 0.08f, particleBlockData);
					}
				}, 
				loc -> { });
	}

}
