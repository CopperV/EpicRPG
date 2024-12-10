package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Misc;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import net.md_5.bungee.api.ChatColor;

public class SindragosaDamageAttackListener implements Listener {
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void runeResistance(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(EpicDamageType.MAGIC))
			return;

		Object[] args = e.getArgs();
		if(args == null 
				|| args.length <= 0
				|| !(args[0] instanceof ItemStackRune))
			return;

		ItemStackRune ir = (ItemStackRune) args[0];
		String type = ir.getMagicType().toLowerCase();
		
		Entity victim = e.getVictim();
		Entity damager = e.getDamager();
		if(damager == null)
			return;
		
		String bossName = ChatColor.stripColor(victim.getName());
		if(!bossName.equals("Sindragosa"))
			return;
		
		AbstractEntity aVictim = BukkitAdapter.adapt(victim);
		if(!MythicBukkit.inst().getMobManager().isActiveMob(aVictim))
			return;
		
		Collection<String> lookingTypes = Arrays.asList(
				"mrok",
				"krew",
				"woda",
				"natura"
				);
		
		if(!lookingTypes.contains(type))
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
