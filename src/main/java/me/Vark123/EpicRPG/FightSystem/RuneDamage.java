package me.Vark123.EpicRPG.FightSystem;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.RuneEffect;

public class RuneDamage {
	
	@Deprecated
	public static boolean damageSwietaStrzala(Player p, LivingEntity e, double dmg) {
		return true;
	}

	@Deprecated
	public static boolean damageKrwawaStrzala(Player p, LivingEntity e, double damage) {
		return true;
	}

	@Deprecated
	public static boolean damageOgnistaStrzala(Player p, LivingEntity e, double damage) {
		return true;		
	}

	@Deprecated
	public static boolean damageZatrutaStrzala(Player p, LivingEntity e, double damage) {
		return true;
	}
	
	public static boolean damageNormal(Player p, LivingEntity e, ItemStackRune dr) {
		return true;
	}
	
	public static boolean damageNormal(Player p, LivingEntity e, ItemStackRune dr, double dmg) {
		return true;
	}
	
	public static boolean damageNormal(Player p, LivingEntity e, ItemStackRune dr, RuneEffect effect) {
		return true;
	}
	
	public static boolean damageNormal(Player p, LivingEntity e, ItemStackRune dr, double dmg, RuneEffect effect) {
		return true;
	}
	
	public static boolean damageTiming(Player p, LivingEntity e, ItemStackRune dr) {
		return true;
	}
	
	public static boolean damageTiming(Player p, LivingEntity e, ItemStackRune dr, double dmg) {
		return true;
	}

	private static void krewPrzodkowEffect(RpgPlayer rpg) {
		
	}
	
}
