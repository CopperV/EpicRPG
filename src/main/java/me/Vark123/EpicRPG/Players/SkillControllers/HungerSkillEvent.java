package me.Vark123.EpicRPG.Players.SkillControllers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

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
	
}
