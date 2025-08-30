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

public class PrzyzwanieNiedzwiedzia extends ACastableRune {

	private IRuneLocationEffect spawnEffect = loc -> {
		loc.getWorld().playSound(loc, Sound.ENTITY_POLAR_BEAR_WARNING, 1.2f, 0.8f);
		
		BlockData partData = Material.FLOWERING_AZALEA_LEAVES.createBlockData();
		Location partLoc = loc.clone().add(0, 0.9, 0);
		partLoc.getWorld().spawnParticle(Particle.BLOCK, partLoc, 50, 0.9f, 0.9f, 0.9f, 0.25f, partData);
	};
	
	public PrzyzwanieNiedzwiedzia(RpgPlayer rpgPlayer, EpicRune rune) {
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
