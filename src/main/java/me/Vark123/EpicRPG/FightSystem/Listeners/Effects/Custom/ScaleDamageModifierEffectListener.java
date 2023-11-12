package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Custom;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class ScaleDamageModifierEffectListener implements Listener {
	
	private static final double MAG_SCALE = 0.9;
	private static final double WOJ_SCALE = 0.85;
	private static final double MYS_SCALE = 0.7;
	private static final double OBY_SCALE = 1.2;
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;

		Entity damager = e.getDamager();
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();
		if(!(damager instanceof Player))
			return;

		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgPlayerInfo info = rpg.getInfo();
		double modifier = 1;
		
		switch(info.getShortProf().toLowerCase()) {
			case "woj":
				modifier = WOJ_SCALE;
				break;
			case "mys":
				modifier = MYS_SCALE;
				break;
			case "mag":
				modifier = MAG_SCALE;
				break;
			case "oby":
				modifier = OBY_SCALE;
				break;
		}
		
		e.setDmg(e.getDmg() * modifier);
		
	}

}
