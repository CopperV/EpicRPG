package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class LodowyBlokEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onDefense(EpicDefenseEvent e) {
		if(e.isCancelled())
			return;
		
		LivingEntity victim = e.getVictim();
		if(Utils.hasEntityBuff(victim, EpicModifierTypes.LODOWY_BLOK)) {
			victim.getWorld().playSound(victim, Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1, 2);
			e.setCancelled(true);
			return;
		}
		
		LivingEntity damager = e.getDamager();
		if(damager != null && Utils.hasEntityBuff(damager, EpicModifierTypes.LODOWY_BLOK)) {
			Utils.unsetEntityBuff(damager, EpicModifierTypes.LODOWY_BLOK);
		}
	}

}
