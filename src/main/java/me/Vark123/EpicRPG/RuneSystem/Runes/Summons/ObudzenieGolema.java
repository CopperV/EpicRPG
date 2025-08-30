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

public class ObudzenieGolema extends ACastableRune {

	private IRuneLocationEffect spawnEffect = loc -> {
		loc.getWorld().playSound(loc, Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1.2f, 0.8f);
		
		BlockData partData1 = Material.POLISHED_ANDESITE.createBlockData();
		Location partLoc = loc.clone().add(0, 1.25, 0);
		partLoc.getWorld().spawnParticle(Particle.BLOCK, partLoc, 30, 0.5f, 0.6f, 1.25f, 0.6f, partData1);
		partLoc.getWorld().spawnParticle(Particle.GUST, partLoc, 20, 0.5f, 0.6f, 1.25f, 0.6f);
	};
	
	public ObudzenieGolema(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		
		Location spawnLoc = player.getLocation().clone().add(0, 0, 0);
		double radius = 2;
		
		SummonRuneTemplate.castSummon(
				this,
				spawnLoc,
				radius,
				spawnEffect, 
				spawnEffect);
		
	}

}
