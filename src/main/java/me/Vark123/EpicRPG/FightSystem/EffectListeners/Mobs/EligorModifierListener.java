package me.Vark123.EpicRPG.FightSystem.EffectListeners.Mobs;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDamageEffectEvent;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import net.md_5.bungee.api.ChatColor;

public class EligorModifierListener implements Listener {

	@EventHandler
	private void runeModifier(EpicDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.MAGIC))
			return;
		
		Entity victim = e.getVictim();
		
		String bossName = ChatColor.stripColor(victim.getName());
		if(!bossName.equals("Eligor - Legendary Boss"))
			return;

		Object[] args = e.getArgs();
		if(args == null 
				|| args.length < 2
				|| !(args[1] instanceof EpicRune))
			return;

		EpicRune rune = (EpicRune) args[1];
		String magicType = rune.getMagicType().toLowerCase();

		ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(victim);
		
		Collection<String> blockedTypes = new HashSet<>();
		switch(aMob.getStance()) {
			case "phase4":
				blockedTypes.add("woda");
			case "phase3":
				blockedTypes.add("chaos");
			case "phase2":
				blockedTypes.add("tajemna");
			case "phase1":
				blockedTypes.add("mrok");
		}
		
		if(!blockedTypes.contains(magicType))
			return;
		
		e.setCancelled(true);
	}

	@EventHandler
	private void meleeModifier(EpicDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.MELEE))
			return;
		
		Entity victim = e.getVictim();
		
		String bossName = ChatColor.stripColor(victim.getName());
		if(!bossName.equals("Eligor - Legendary Boss"))
			return;
		
		e.setDamage(e.getDamage() * 0.75);
	}

	@EventHandler
	private void projectileModifier(EpicDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.MELEE))
			return;
		
		Entity victim = e.getVictim();
		
		String bossName = ChatColor.stripColor(victim.getName());
		if(!bossName.equals("Eligor - Legendary Boss"))
			return;

		ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(victim);
		if(!aMob.getStance().equals("phase4"))
			return;
		
		e.setDamage(e.getDamage() * 0.5);
	}
	
}
