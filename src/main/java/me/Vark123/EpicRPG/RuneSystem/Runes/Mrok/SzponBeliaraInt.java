package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.MultipleProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class SzponBeliaraInt extends ACastableRune {

	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;

	private DustOptions dust = new DustOptions(Color.RED, 1.5f);
	private BlockData particleBlockData = Material.REDSTONE_BLOCK.createBlockData();
	
	public SzponBeliaraInt(RpgPlayer rpgPlayer, EpicRune rune) {
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
		double velocity = 0.7;
		
		MultipleProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				velocity, 
				1,
				1,
				35, 
				0,
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_EVOKER_CAST_SPELL, 1.2f, 0.6f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.DUST, loc, 3, 
							0.2f, 0.2f, 0.2f, 0.03f, dust);
					loc.getWorld().spawnParticle(Particle.FALLING_DUST, loc, 5, 
							0.15f, 0.15f, 0.15f, 0.05f, particleBlockData);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune)) {
						e.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_HORSE_HURT, 0.9f, 0.7f);

						loc.getWorld().spawnParticle(Particle.DUST, loc, 15, 
								0.35f, 0.35f, 0.35f, 0.12f, dust);
						loc.getWorld().spawnParticle(Particle.FALLING_DUST, loc, 25, 
								0.25f, 0.25f, 0.25f, 0.1f, particleBlockData);
					}
				}, 
				loc -> {
					Vector vec = startLoc.getDirection().normalize().multiply(-1);
					Location startLoc2 = loc.clone().add(vec.clone().multiply(velocity));
					double distance = startLoc2.distance(startLoc);
					
					MultipleProjectileRuneTemplate.castProjectile(
							this, 
							startLoc2, 
							vec,
							velocity, 
							1,
							1,
							distance, 
							0,
							boundingBox,
							__ -> { }, 
							_loc -> {
								_loc.getWorld().spawnParticle(Particle.DUST, _loc, 3, 
										0.2f, 0.2f, 0.2f, 0.03f, dust);
								_loc.getWorld().spawnParticle(Particle.FALLING_DUST, _loc, 5, 
										0.15f, 0.15f, 0.15f, 0.05f, particleBlockData);
							}, 
							hitCondition, 
							(_loc, e) -> {
								if(RuneUtils.damage(player, e, rune)) {
									e.getWorld().playSound(_loc, Sound.ENTITY_ZOMBIE_HORSE_HURT, 0.9f, 0.7f);

									_loc.getWorld().spawnParticle(Particle.DUST, _loc, 15, 
											0.35f, 0.35f, 0.35f, 0.12f, dust);
									_loc.getWorld().spawnParticle(Particle.FALLING_DUST, _loc, 25, 
											0.25f, 0.25f, 0.25f, 0.1f, particleBlockData);
								}
							}, 
							_loc -> { });
				});
	}

}
