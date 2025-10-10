package me.Vark123.EpicRPG.RuneSystem.Functional;

import org.bukkit.entity.Entity;

import me.Vark123.EpicRPG.RuneSystem.ACastableRune;

public interface IRuneDisplayGetter {

	public String getDisplay(ACastableRune rune, Entity target);
	
}
