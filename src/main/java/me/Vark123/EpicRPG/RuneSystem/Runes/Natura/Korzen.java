package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import java.util.Random;

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
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.StunRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class Korzen extends ACastableRune {

	private static final Random rand = new Random();
	private static final BlockData blockData = Material.ROOTED_DIRT.createBlockData();
	
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	
	public Korzen(RpgPlayer rpgPlayer, EpicRune rune) {
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
				0.4, 
				1,
				1,
				35, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.BLOCK_CHEST_OPEN, 0.8f, 0.6f);
				}, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.BLOCK, loc, 12, 
							0.33f, 0.33f, 0.33f, 0.1f, blockData);
					loc.getWorld().spawnParticle(Particle.FALLING_DUST, loc, 4, 
							0.2f, 0.2f, 0.2f, 0.04f, blockData);
				}, 
				hitCondition, 
				(loc, e) -> {
					if(RuneUtils.damage(player, e, rune)) {
						loc.getWorld().playSound(loc, Sound.ENTITY_EVOKER_FANGS_ATTACK, 0.8f, 1.2f);

						StunRuneTemplate.castEffect(this, e);
						
						TimingEffectRuneTemplate.castEffect(
								this,
								e,
								__ -> { },
								__ -> { }, 
								new TimingRuneEffect(
										3,
										entity -> {
											loc.getWorld().playSound(loc, Sound.BLOCK_CHERRY_LEAVES_BREAK, rand.nextFloat(0.6f, 0.9f), rand.nextFloat(0.8f, 1.2f));
											
											Location loc2 = entity.getLocation().clone().add(0, 0.15, 0);
											for(int i = 0; i < 5; ++i) {
												double r = rand.nextDouble(0.2);
												double angle = rand.nextDouble(Math.PI * 2);
												double x = rand.nextDouble(-1, 1);
												double y = rand.nextDouble(1);
												double z = rand.nextDouble(-1, 1);
												double f = rand.nextDouble(0.01, 0.06);
												
												Location pos = loc2.clone().add(r * Math.sin(angle), y, r * Math.cos(angle));
												pos.getWorld().spawnParticle(Particle.WAX_ON, pos, 0,
														x, y, z, f);
											}
										}), 
								new TimingRuneEffect(
										2,
										entity -> {
											Location loc2 = entity.getLocation().clone().add(0, 0.15, 0);
											for(int i = 0; i < 5; ++i) {
												double r = rand.nextDouble(0.2);
												double angle = rand.nextDouble(Math.PI * 2);
												double x = rand.nextDouble(-1, 1);
												double y = rand.nextDouble(1);
												double z = rand.nextDouble(-1, 1);
												double f = rand.nextDouble(0.02, 0.09);
												
												Location pos = loc2.clone().add(r * Math.sin(angle), 0, r * Math.cos(angle));
												pos.getWorld().spawnParticle(Particle.TRIAL_SPAWNER_DETECTION, pos, 0,
														x, y, z, f);
											}
										}));
					}
				}, 
				loc -> { });
	}

}
