package me.Vark123.EpicRPG.RuneSystem.Runes.Summons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneLocationEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.SummonRuneTemplate;

public class PrzyzwanieGoblina extends ACastableRune {

	private IRuneLocationEffect spawnEffect = loc -> {
		loc.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_AMBIENT, 1.15f, 1.35f);
		
		BlockData partData1 = Material.MANGROVE_LEAVES.createBlockData();
		BlockData partData2 = Material.RAW_GOLD_BLOCK.createBlockData();
		Location partLoc = loc.clone().add(0, 0.4, 0);
		partLoc.getWorld().spawnParticle(Particle.BLOCK, partLoc, 20, 0.5f, 0.4f, 0.6f, 0.4f, partData1);
		partLoc.getWorld().spawnParticle(Particle.BLOCK, partLoc, 20, 0.5f, 0.4f, 0.6f, 0.4f, partData2);
	};
	
	public PrzyzwanieGoblina(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		
		Location spawnLoc = player.getLocation().clone().add(0, 0, 0);
		double radius = 1.5;
		
		SummonRuneTemplate.castSummon(
				this,
				spawnLoc,
				radius,
				spawnEffect, 
				spawnEffect);
		
	}

}
