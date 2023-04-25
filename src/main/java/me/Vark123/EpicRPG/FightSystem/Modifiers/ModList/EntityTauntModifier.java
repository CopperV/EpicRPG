package me.Vark123.EpicRPG.FightSystem.Modifiers.ModList;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.FightSystem.Modifiers.DamageModifier;

public class EntityTauntModifier implements DamageModifier {

	@Override
	public double modifyDamage(Entity damager, Entity victim, double damage, DamageCause cause) {

		//TODO
//		if(!Prowokacja.getTargets().containsKey(victim))
//			return damage;
//		
//		ActiveMob mob = MythicBukkit.inst().getAPIHelper().getMythicMobInstance(victim);
//		if(mob == null) 
//			return damage;
//		
//		if(mob.hasThreatTable())
//			mob.getThreatTable().Taunt(BukkitAdapter.adapt(Prowokacja.getTargets().get(victim)));
//		else
//			mob.setTarget(BukkitAdapter.adapt(Prowokacja.getTargets().get(victim)));
		
		return damage;
	}

}
