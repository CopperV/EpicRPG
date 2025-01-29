package me.Vark123.EpicRPG.FightSystem.Listeners.Death;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.EpicRPGMobManager;
import me.Vark123.EpicRPG.Core.CoinsSystem;
import me.Vark123.EpicRPG.Core.ExpSystem;
import me.Vark123.EpicRPG.Core.MoneySystem;
import me.Vark123.EpicRPG.Core.StygiaSystem;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDeathEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class EntityDeathVaultListener implements Listener {

	@EventHandler
	private void OnDeath(EpicDeathEvent e) {
		Player killer = e.getPlayerKiller();
		if(killer == null)
			return;
		
		LivingEntity victim = e.getVictim();
		String name = victim.getName();
		
		int xp = 0;
		if(victim instanceof Player) {
			RpgPlayer victimRpg = PlayerManager.getInstance().getRpgPlayer((Player) victim);
			xp = victimRpg.getInfo().getLevel() * 3;
		} else {
			xp = EpicRPGMobManager.getInstance().getRandomMobExp(name);
		}
		
		if(xp <= 0)
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(killer);
		ExpSystem.getInstance().addMobExp(rpg, xp);
		StygiaSystem.getInstance().addMobStygia(rpg, xp);
		
		if(!(victim instanceof Player)) {
			CoinsSystem.getInstance().addMobCoins(rpg, name);
			MoneySystem.getInstance().addMobMoney(rpg, name);
		} else {
			
		}
	}
	
}
