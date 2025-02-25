package me.Vark123.EpicRPG.RuneSystem.Runes._InProgress;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.SummonRuneTemplate;

public class PrzyzwanieWilka extends ACastableRune {

	public PrzyzwanieWilka(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	//TODO
	@Override
	public void castSpell() {
		
		Location spawnLoc = player.getLocation().clone().add(0, 0.5, 0);
		double radius = 1;
		
		SummonRuneTemplate.castSummon(
				this,
				"SummonWilk", 
				spawnLoc,
				radius,
				loc -> {
					Bukkit.broadcastMessage("Test1");
				}, 
				loc -> {
					Bukkit.broadcastMessage("Test2");
				});
		
	}

}
