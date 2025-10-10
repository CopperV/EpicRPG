package me.Vark123.EpicRPG.FightSystem.EffectListeners.Mobs;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDamageEffectEvent;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import net.md_5.bungee.api.ChatColor;

public class SindragosaEffectListener implements Listener {

	private Collection<String> affectingTypes = Arrays.asList(
			"mrok",
			"krew",
			"woda",
			"natura"
			);
	
	@EventHandler
	private void onDamage(EpicDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.MAGIC))
			return;
		
		Entity victim = e.getVictim();
		
		String bossName = ChatColor.stripColor(victim.getName());
		if(!bossName.equals("Sindragosa"))
			return;

		Object[] args = e.getArgs();
		if(args == null 
				|| args.length < 2
				|| !(args[1] instanceof EpicRune))
			return;

		EpicRune rune = (EpicRune) args[1];
		String magicType = rune.getMagicType().toLowerCase();
		
		if(!affectingTypes.contains(magicType))
			return;

		String world = victim.getWorld().getName().toLowerCase();
		if(!world.startsWith("raid_1"))
			return;
		
		if(world.startsWith("raid_1_mythic"))
			e.decreaseModifier(0.5);
		else if(world.startsWith("raid_1_heroic"))
			e.decreaseModifier(0.4);
		else
			e.decreaseModifier(0.3);
	}
	
}
