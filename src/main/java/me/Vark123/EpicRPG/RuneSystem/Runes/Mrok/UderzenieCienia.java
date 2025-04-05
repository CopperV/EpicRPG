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

public class UderzenieCienia extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	private BlockData particleBlockData;
	
	public UderzenieCienia(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				0.8, 0.8, 0.8, 
				0.8, 0.8, 0.8);
		
		particleBlockData = Material.LAVA.createBlockData();
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
					loc.getWorld().playSound(loc, Sound.ENTITY_BREEZE_SHOOT, 1.2f, 0.7f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.SMOKE, loc, 4, 
							0.25f, 0.25f, 0.25f, 0.02f);
					loc.getWorld().spawnParticle(Particle.LAVA, loc, 1, 
							0.05f, 0.05f, 0.05f, 0.02f);
					loc.getWorld().spawnParticle(Particle.FALLING_DUST, loc, 12, 
							0.35f, 0.35f, 0.35f, 0.07f, particleBlockData);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune)) {
						e.getWorld().playSound(loc, Sound.ENTITY_SKELETON_HURT, 0.8f, 0.7f);

						loc.getWorld().spawnParticle(Particle.SMOKE, loc, 10, 
								0.4f, 0.4f, 0.4f, 0.04f);
						loc.getWorld().spawnParticle(Particle.LAVA, loc, 6, 
								0.12f, 0.12f, 0.12f, 0.05f);
						loc.getWorld().spawnParticle(Particle.FALLING_DUST, loc, 20, 
								0.5f, 0.5f, 0.5f, 0.12f, particleBlockData);
					}
				}, 
				loc -> { });
	}

}
