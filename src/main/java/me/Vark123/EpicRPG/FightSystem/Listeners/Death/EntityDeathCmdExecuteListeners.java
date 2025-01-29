package me.Vark123.EpicRPG.FightSystem.Listeners.Death;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.EpicRPGMobManager;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDeathEvent;

public class EntityDeathCmdExecuteListeners implements Listener {

	@EventHandler
	private void OnDeath(EpicDeathEvent e) {
		Player killer = e.getPlayerKiller();
		if(killer == null)
			return;
		
		LivingEntity victim = e.getVictim();
		String name = victim.getName();
		EpicRPGMobManager.getInstance().executeCommands(killer, name);
	}

}
