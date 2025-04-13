package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class PiachWOczy extends ACastableRune {
	
	private static final BlockData blockData = Material.SAND.createBlockData();
	
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;

	private PotionEffect effect1;
	private PotionEffect effect2;
	
	public PiachWOczy(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.65, -0.65, -0.65, 
				0.65, 0.65, 0.65);
		
		effect1 = new PotionEffect(PotionEffectType.SLOWNESS, rune.getDurationTime()*20, 3);
		effect2 = new PotionEffect(PotionEffectType.BLINDNESS, rune.getDurationTime()*20, 3);
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 0.1, 0);
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				startLoc, 
				startLoc.getDirection().normalize(),
				0.8, 
				1,
				2,
				22, 
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.BLOCK_SAND_STEP, 1.5f, 0.7f);
				}, 
				loc -> {
					loc.getWorld().playSound(loc, Sound.BLOCK_SAND_STEP, 1f, 1f);
					loc.getWorld().spawnParticle(Particle.CRIT, loc, 8, 
							0.35, 0.35, 0.35, 0.03f);
					loc.getWorld().spawnParticle(Particle.BLOCK, loc, 7, 
							0.2, 0.2, 0.2, 0.07f, blockData);
				}, 
				hitCondition, 
				(loc, e) -> {	
					if(RuneUtils.damage(player, e, rune)) {
						loc.getWorld().playSound(loc, Sound.BLOCK_SAND_STEP, 1.8f, 0.65f);
						
						loc.getWorld().spawnParticle(Particle.CRIT, loc, 20, 
								0.65, 0.65, 0.65, 0.1f);
						loc.getWorld().spawnParticle(Particle.BLOCK, loc, 7, 
								0.5, 0.5, 0.5, 0.2f, blockData);
						
						e.addPotionEffect(effect1);
						e.addPotionEffect(effect2);
					}
				}, 
				loc -> { });
	}

}
