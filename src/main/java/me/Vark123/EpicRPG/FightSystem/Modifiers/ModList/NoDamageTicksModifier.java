package me.Vark123.EpicRPG.FightSystem.Modifiers.ModList;

import org.bukkit.Bukkit;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.Modifiers.DamageModifier;

public class NoDamageTicksModifier implements DamageModifier {

	@Override
	public double modifyDamage(Entity damager, Entity victim, double damage, DamageCause cause) {

//		Bukkit.broadcastMessage("§eTest1 NDT §a");
		if(!cause.equals(DamageCause.PROJECTILE))
			return damage;
//		Bukkit.broadcastMessage("§eTest2 NDT §a");
		if(!(damager instanceof AbstractArrow))
			return damage;
//		Bukkit.broadcastMessage("§eTest3 NDT §a");
		if(!(((AbstractArrow)damager).getShooter() instanceof Player))
			return damage;
//		Bukkit.broadcastMessage("§eTest4 NDT §a");
		if(!(victim instanceof LivingEntity))
			return damage;

		Bukkit.getScheduler().runTaskLater(Main.getInstance(), ()->{
			if(victim.isDead())
				return;
			((LivingEntity)victim).setNoDamageTicks(0);
		}, 2);
	
		
		return damage;
	}

	
	
}
