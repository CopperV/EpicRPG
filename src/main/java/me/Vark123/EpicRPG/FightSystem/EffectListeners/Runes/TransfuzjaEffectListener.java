package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicDamageEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class TransfuzjaEffectListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EpicDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		LivingEntity victim = e.getVictim();
		if(!(victim instanceof Player player))
			return;
		
		double damage = e.getFinalDamage();
		if(damage < victim.getHealth() || !Utils.hasEntityBuff(player, EpicModifierTypes.TRANSFUZJA))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		rpg.getStats().createRegenHpTask(5, player.getAttribute(Attribute.MAX_HEALTH).getValue()*0.05);
		
		Utils.unsetEntityBuff(player, EpicModifierTypes.TRANSFUZJA);
		e.setCancelled(true);
	}
	
}
