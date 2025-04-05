package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.WaveRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class SmiertelnaFala extends ACastableRune {
	
	private IRuneHitCondition hitCondition;
	private BlockData particleBlockData1;
	private BlockData particleBlockData2;

	public SmiertelnaFala(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();

		particleBlockData1 = Material.OBSIDIAN.createBlockData();
		particleBlockData2 = Material.CRYING_OBSIDIAN.createBlockData();
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.25, 0);
		
		WaveRuneTemplate.castWave(
				this,
				startLoc, 
				rune.getObszar(),
				6, 
				0.18, 
				1,
				1, 
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_HORSE_DEATH, 1f, 0.85f);
				},
				loc -> {
					loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 3,
							0.15, 1.25, 0.15, 0.02);
					loc.getWorld().spawnParticle(Particle.FALLING_DUST, loc, 5,
							0.15, 1.25, 0.15, 0.06, particleBlockData1);
					loc.getWorld().spawnParticle(Particle.FALLING_DUST, loc, 5,
							0.15, 1.25, 0.15, 0.06, particleBlockData2);
				}, 
				hitCondition, 
				(loc, entity) -> {
					RuneUtils.damage(player, entity, rune);
					
					loc.getWorld().playSound(loc, Sound.ENTITY_SKELETON_HORSE_DEATH, 0.8f, 0.85f);
				},
				loc -> { });
	}

}
