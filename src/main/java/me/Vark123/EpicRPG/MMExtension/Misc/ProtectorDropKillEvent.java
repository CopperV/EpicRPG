package me.Vark123.EpicRPG.MMExtension.Misc;

import java.util.ArrayList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobLootDropEvent;
import io.lumine.mythic.core.mobs.ActiveMob;

public class ProtectorDropKillEvent implements Listener {
	
	@EventHandler()
	public void onDrop(MythicMobLootDropEvent e) {
		Entity killer = e.getKiller();
		if(killer == null)
			return;
		if(killer instanceof Player)
			return;

		ActiveMob mob = MythicBukkit.inst().getMobManager().getMythicMobInstance(killer);
		if(mob == null)
			return;
		if(mob.getFaction() == null || mob.getFaction().isEmpty())
			return;
		if(!mob.getFaction().equalsIgnoreCase("Archolos_Defenders"))
			return;

		e.getDrops().setLootTable(new ArrayList<>());;
		return;
	}

}
