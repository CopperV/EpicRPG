package me.Vark123.EpicRPG.Players.SkillControllers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class HungerSkillEvent implements Listener {

	@EventHandler
	public void onSkill(FoodLevelChangeEvent e) {
		if(e.isCancelled())
			return;
		if(!(e.getEntity() instanceof Player))
			return;
		
		Player p = (Player) e.getEntity();
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		if(!rpg.getSkills().hasHungerless())
			return;
		
		e.setCancelled(true);
		e.setFoodLevel(18);
	}
	
	@EventHandler
	private void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		if(!rpg.getSkills().hasHungerless())
			return;
		
		new BukkitRunnable() {
			@Override
			public void run() {
				p.setFoodLevel(18);
			}
		}.runTaskLater(Main.getInstance(), 1);
	}
	
}
