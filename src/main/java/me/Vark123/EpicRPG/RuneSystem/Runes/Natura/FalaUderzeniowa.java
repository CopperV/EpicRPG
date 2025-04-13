package me.Vark123.EpicRPG.RuneSystem.Runes.Natura;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.WaveRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class FalaUderzeniowa extends ACastableRune {
	
	private static final BlockData blockData = Material.DIRT.createBlockData();
	
	private IRuneHitCondition hitCondition;
	
	public FalaUderzeniowa(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 0.4, 0);
		
		WaveRuneTemplate.castWave(
				this,
				startLoc, 
				rune.getObszar(),
				7, 
				0.3, 
				1,
				1, 
				loc -> {
					loc.getWorld().playSound(loc, Sound.ITEM_MACE_SMASH_GROUND_HEAVY, 1.2f, 0.7f);
				},
				loc -> {
					loc.getWorld().spawnParticle(Particle.BLOCK, loc, 12,
							0.2, 0.6, 0.2, 0.12, blockData);
					loc.getWorld().spawnParticle(Particle.CLOUD, loc.clone().add(0, -0.35, 0), 3,
							0.1, 0.05, 0.1, 0.02);
				}, 
				hitCondition, 
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune)) {
						loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_SMALL_FALL, 1f, 0.8f);
						
						Location eLoc = entity.getLocation();
						Vector vec = new Vector(
								eLoc.getX() - startLoc.getX(),
								0.5,
								eLoc.getZ() - startLoc.getZ())
								.normalize().setY(1.1).normalize().multiply(2.2);
						entity.setVelocity(vec);
					}
					
				},
				loc -> { });
	}

}
