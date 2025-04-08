package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob.ThreatTable;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.RuneEffectType;
import me.Vark123.EpicRPG.Utils.Utils;

public class ProwokacjaEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		LivingEntity damager = e.getDamager();
		if(damager == null)
			return;
		
		if(!Utils.hasEntityBuff(damager, EpicModifierTypes.PROWOKACJA))
			return;
		
		e.setDamage(e.getDamage() * 0.01);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onDefense(EpicDefenseEvent e) {
		if(e.isCancelled())
			return;
		
		LivingEntity victim = e.getVictim();
		if(victim == null)
			return;
		
		if(!Utils.hasEntityBuff(victim, EpicModifierTypes.PROWOKACJA))
			return;
		
		e.setDamage(Math.ceil(e.getDamage() * 0.1));
	}
	
	@EventHandler
	public void onTarget(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		LivingEntity victim = e.getVictim();
		LivingEntity damager = e.getDamager();
		if(damager == null)
			return;
		
		if(!Utils.hasEntityEffect(victim, RuneEffectType.PROWOKACJA) || Utils.hasEntityEffect(damager, victim, RuneEffectType.PROWOKACJA))
			return;
		
		AbstractEntity aVictim = BukkitAdapter.adapt(victim);
		LivingEntity target = (LivingEntity) aVictim.getMetadata(RuneEffectType.PROWOKACJA.name()).get();
		
		MythicBukkit.inst().getMobManager().getActiveMob(victim.getUniqueId()).ifPresent(mob -> {
			if(mob.hasThreatTable())
				mob.getThreatTable().Taunt(BukkitAdapter.adapt(target));
			else {
				mob.setTarget(BukkitAdapter.adapt(target));
			}
		});
	}

	@EventHandler
	public void onTarget(EntityTargetEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getEntity();
		Entity target = e.getTarget();
		if(target == null || !(target instanceof LivingEntity)
				|| !(victim instanceof LivingEntity))
			return;
		
		LivingEntity lVictim = (LivingEntity) victim;
		LivingEntity lTarget = (LivingEntity) target;
		if(!Utils.hasEntityEffect(lVictim, RuneEffectType.PROWOKACJA) || Utils.hasEntityEffect(lTarget, lVictim, RuneEffectType.PROWOKACJA))
			return;
		
		e.setCancelled(true);
		
		MythicBukkit.inst().getMobManager().getActiveMob(lVictim.getUniqueId()).ifPresent(aMob -> {
			if(aMob.hasThreatTable()) {
				AbstractEntity aVictim = BukkitAdapter.adapt(lVictim);
				LivingEntity taunt = (LivingEntity) aVictim.getMetadata(RuneEffectType.PROWOKACJA.name()).get();

				ThreatTable threatTable = aMob.getThreatTable();
				threatTable.Taunt(BukkitAdapter.adapt(taunt));
			} else {
				aMob.setTarget(BukkitAdapter.adapt(target));
			}
		});
	}

}
