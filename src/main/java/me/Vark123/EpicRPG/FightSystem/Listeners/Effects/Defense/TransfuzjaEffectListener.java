package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Defense;

import org.bukkit.EntityEffect;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.RuneSystem.Runes.Transfuzja;

public class TransfuzjaEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		if(!(victim instanceof Player))
			return;
		
		Player p = (Player) victim;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		if(!modifiers.hasTransfuzja())
			return;
		
		if(!Transfuzja.getEffected().contains(p))
			return;
		
		if(p.getHealth() - e.getFinalDamage() >= 1.0D)
			return;
		
		rpg.getStats().createRegenHpTask(3, p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*0.05);
		p.playEffect(EntityEffect.TOTEM_RESURRECT);
		p.playEffect(EntityEffect.HURT);
		Transfuzja.getEffected().remove(p);
		e.setCancelled(true);
	}

}
