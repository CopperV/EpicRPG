package me.Vark123.EpicRPG.RuneSystem.Runes.Summons;

import org.bukkit.Location;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.SummonRuneTemplate;

public class BaseSummonRune extends ACastableRune {

	public BaseSummonRune(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		
		Location spawnLoc = player.getLocation().clone().add(0, 0.5, 0);
		double radius = 1;
		
		SummonRuneTemplate.castSummon(
				this,
				spawnLoc,
				radius,
				__ -> { }, 
				__ -> { });
		
	}

}
