package me.Vark123.EpicRPG.RuneSystem.Listeners.Runes;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import io.papermc.paper.event.entity.EntityMoveEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class LodowyBlokEvent implements Listener {

	@EventHandler
	private void onMove(EntityMoveEvent e) {
		if(e.isCancelled())
			return;
		if(e.getFrom().getBlock().getLocation().equals(e.getTo().getBlock().getLocation()))
			return;
		
		handleMovement(e.getEntity());
	}
	
	@EventHandler 
	private void onMove(PlayerMoveEvent e) {
		if(e.isCancelled())
			return;
		if(e.getFrom().getBlock().getLocation().equals(e.getTo().getBlock().getLocation()))
			return;
		
		handleMovement(e.getPlayer());
	}
	
	private void handleMovement(LivingEntity entity) {
		if(!Utils.hasEntityBuff(entity, EpicModifierTypes.LODOWY_BLOK))
			return;
		
		Utils.unsetEntityBuff(entity, EpicModifierTypes.LODOWY_BLOK);
	}
	
}
