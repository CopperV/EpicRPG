package me.Vark123.EpicRPG.OldFightSystem.Modifiers.ModList;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.OldFightSystem.Modifiers.DamageModifier;
import me.Vark123.EpicRPG.RuneSystem.Runes.Prowokacja;

public class EntityTauntModifier implements DamageModifier {

	@Override
	public double modifyDamage(Entity damager, Entity victim, double damage, DamageCause cause) {

		if(!Prowokacja.getTargets().containsKey(victim))
			return damage;
		
		ActiveMob mob = MythicBukkit.inst().getAPIHelper().getMythicMobInstance(victim);
		if(mob == null) 
			return damage;
		
		if(mob.hasThreatTable())
			mob.getThreatTable().Taunt(BukkitAdapter.adapt(Prowokacja.getTargets().get(victim)));
		else
			mob.setTarget(BukkitAdapter.adapt(Prowokacja.getTargets().get(victim)));
		
		return damage;
	}

}
