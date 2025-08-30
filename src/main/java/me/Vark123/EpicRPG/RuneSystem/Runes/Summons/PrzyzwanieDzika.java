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

public class PrzyzwanieDzika extends ACastableRune {

	private IRuneLocationEffect spawnEffect = loc -> {
		loc.getWorld().playSound(loc, Sound.ENTITY_HOGLIN_RETREAT, 1.1f, 0.9f);
		
		BlockData partData = Material.DARK_OAK_LEAVES.createBlockData();
		Location partLoc = loc.clone().add(0, 0.7, 0);
		partLoc.getWorld().spawnParticle(Particle.BLOCK, partLoc, 35, 0.7f, 0.7f, 0.7f, 0.22f, partData);
	};
	
	public PrzyzwanieDzika(RpgPlayer rpgPlayer, EpicRune rune) {
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
