package me.Vark123.EpicRPG.RuneSystem;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.FightSystem.ManualDamage;
import me.Vark123.EpicRPG.FightSystem.Events.MagicEntityDamageByEntityEvent;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRunePostDamageEffect;
import me.Vark123.EpicRPG.Utils.Utils;

public final class RuneUtils {

	private RuneUtils() { }
	
	public static boolean damage(Player player, LivingEntity entity, EpicRune rune) {
		return damage(player, entity, rune, rune.getDamage());
	}
	
	public static boolean damage(Player player, LivingEntity entity, EpicRune rune, double damage) {
		return damage(player, entity, rune, damage, null);
	}
	
	public static boolean damage(Player player, LivingEntity entity, EpicRune rune, IRunePostDamageEffect effect) {
		return damage(player, entity, rune, rune.getDamage(), effect);
	}
	
	public static boolean damage(Player player, LivingEntity entity, EpicRune rune, double damage, IRunePostDamageEffect effect) {
		Utils.neutralizeEntityNoDamageTicks(player, entity);

		MagicEntityDamageByEntityEvent event = new MagicEntityDamageByEntityEvent(player, entity, damage, rune);
		Bukkit.getPluginManager().callEvent(event);
		if(!ManualDamage.tryDoDamage(player, entity, event.getFinalDamage(), event))
			return false;
		
		if(effect != null
				&& entity.getHealth() > 1
				&& !entity.isDead())
			effect.playeEffect(player, entity, rune);
		
		return true;
	}
	
}
