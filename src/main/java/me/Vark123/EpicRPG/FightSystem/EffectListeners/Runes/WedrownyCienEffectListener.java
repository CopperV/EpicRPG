package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob.ThreatTable;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.Utils.Utils;

public class WedrownyCienEffectListener implements Listener {
	
	@EventHandler
	public void onMod(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		LivingEntity damager = e.getDamager();
		if(damager == null)
			return;
		
		if(!Utils.hasEntityBuff(damager, EpicModifierTypes.WEDROWNY_CIEN))
			return;
		
		Utils.unsetEntityBuff(damager, EpicModifierTypes.WEDROWNY_CIEN);
	}

	@EventHandler
	public void onTarget(EntityTargetEvent e) {
		if(e.isCancelled())
			return;
		
		Entity target = e.getTarget();
		if(target == null || !(target instanceof LivingEntity))
			return;
		
		if(!Utils.hasEntityBuff((LivingEntity) target, EpicModifierTypes.WEDROWNY_CIEN))
			return;
		
		e.setCancelled(true);
		MythicBukkit.inst().getMobManager().getActiveMob(e.getEntity().getUniqueId()).ifPresent(aMob -> {
			if(aMob.hasThreatTable()) {
				ThreatTable threatTable = aMob.getThreatTable();
				if(threatTable.getTopThreatHolder().getBukkitEntity().equals(target)) {
					threatTable.clearTarget();
					threatTable.targetHighestThreat();
				} else {
					threatTable.threatSet(BukkitAdapter.adapt(target), 0);
				}
			}
		});
	}

}
