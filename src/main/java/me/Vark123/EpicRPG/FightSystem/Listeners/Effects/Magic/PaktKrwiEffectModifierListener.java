package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Magic;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;

public class PaktKrwiEffectModifierListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		if(!(victim instanceof Player))
			return;
		
		Player p = (Player) victim;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();

		if(modifiers.hasPaktKrwiMeasure())
			modifiers.setPaktKrwiHp(modifiers.getPaktKrwiHp() + e.getFinalDamage());
		if(modifiers.hasPaktKrwiMeasure_h())
			modifiers.setPaktKrwiHp_h(modifiers.getPaktKrwiHp_h() + e.getFinalDamage());
	}

}
