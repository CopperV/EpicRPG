package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Misc;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import net.md_5.bungee.api.ChatColor;

public class EligorDamageEffectListener implements Listener {
	
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
		String type = ir.getMagicType();
		
		Entity victim = e.getVictim();
		Entity damager = e.getDamager();
		if(damager == null)
			return;
		
		String bossName = ChatColor.stripColor(victim.getName());
		if(!bossName.equals("Eligor - Legendary Boss"))
			return;
		
		AbstractEntity aVictim = BukkitAdapter.adapt(victim);
		if(!MythicBukkit.inst().getMobManager().isActiveMob(aVictim))
			return;

		ActiveMob am = MythicBukkit.inst().getMobManager().getMythicMobInstance(aVictim);
		String stance = am.getStance();
		if(stance == null)
			return;
		
		Collection<String> blockedTypes = new HashSet<>();
		switch(stance) {
			case "stance4":
				blockedTypes.add("woda");
			case "stance3":
				blockedTypes.add("chaos");
			case "stance2":
				blockedTypes.add("tajemna");
			case "stance1":
				blockedTypes.add("mrok");
		}
		
		if(!blockedTypes.contains(type))
			return;
		
		e.setCancelled(true);
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void meleeResistance(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(EpicDamageType.MELEE))
			return;
		
		Entity victim = e.getVictim();
		Entity damager = e.getDamager();
		if(damager == null)
			return;
		
		String bossName = ChatColor.stripColor(victim.getName());
		if(!bossName.equals("Eligor - Legendary Boss"))
			return;
		
		e.setDmg(e.getDmg()*0.75);
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void projectileResistance(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(EpicDamageType.PROJECTILE))
			return;
		
		Entity victim = e.getVictim();
		Entity damager = e.getDamager();
		if(damager == null)
			return;
		
		String bossName = ChatColor.stripColor(victim.getName());
		if(!bossName.equals("Eligor - Legendary Boss"))
			return;
		
		AbstractEntity aVictim = BukkitAdapter.adapt(victim);
		if(!MythicBukkit.inst().getMobManager().isActiveMob(aVictim))
			return;

		ActiveMob am = MythicBukkit.inst().getMobManager().getMythicMobInstance(aVictim);
		String stance = am.getStance();
		if(stance == null || !stance.equals("stance4"))
			return;
		
		e.setDmg(e.getDmg()*0.5);
		
	}

}
