package me.Vark123.EpicRPG.FightSystem.Listeners.Death;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDeathEvent;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.Utils.Utils;

public class EntityDeathBarbarzynskiSzalRuneListener implements Listener {

	@EventHandler
	private void onDeath(EpicDeathEvent e) {
		LivingEntity damager = e.getKiller();
		if(!Utils.hasEntityBuff(damager, EpicModifierTypes.BARBARZYNSKI_SZAL))
			return;
		
		if(!(damager instanceof Player player))
			return;

		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgStats stats = rpg.getStats();

		double hpToRestore = rpg.getInfo().getLevel() * 0.01 + 0.01*stats.getFinalSila() + 0.025*stats.getFinalWytrzymalosc();
		new BukkitRunnable() {
			int timer = 10;
			@Override
			public void run() {
				if(isCancelled())
					return;
				
				if(timer <= 0 || !player.isOnline() || player.isDead()) {
					cancel();
					return;
				}
				--timer;
				
				RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, hpToRestore);
				Bukkit.getPluginManager().callEvent(event);
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}
	
}
