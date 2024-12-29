package me.Vark123.EpicRPG.OldFightSystem;

import org.bukkit.EntityEffect;
import org.bukkit.attribute.Attribute;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

@Deprecated
public class ManualDamage {

	private ManualDamage() {}
	
	public static void doDamage(LivingEntity damager, LivingEntity victim, double damage, EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return;
		damage(damager, victim, damage, e);
	}
	
	public static void doDamage(LivingEntity victim, double damage, EntityDamageEvent e) {
		if(e.isCancelled())
			return;
		damage(victim /*TO PREVENT EXCEPTION*/,
				victim, damage, e);
	}
	
	public static boolean doDamageWithCheck(LivingEntity damager, LivingEntity victim, double damage, EntityDamageByEntityEvent e) {
		if(e.isCancelled())
			return false;
		return damage(damager, victim, damage, e);
	}
	
	public static void doDamageNoCheck(org.bukkit.entity.Entity damager, LivingEntity victim, double damage, EntityDamageByEntityEvent e) {
		damage(damager, victim, damage, e);
	}
	
	public static void doDamageNoCheck(LivingEntity victim, double damage, EntityDamageEvent e) {
		damage(victim /*TO PREVENT EXCEPTION*/,
				victim, damage, e);
	}
	
	private static boolean damage(org.bukkit.entity.Entity damager, LivingEntity victim, double damage, EntityDamageEvent e) {
		if(victim.getHealth() < damage) {
			victim.setLastDamageCause(e);
			victim.setNoDamageTicks(0);
			victim.setHealth(0);
			return true;
		}
		
		victim.setLastDamageCause(e);
		victim.playEffect(EntityEffect.HURT);
		double absorption = victim.getAbsorptionAmount();
		if(absorption > 0) {
			if(absorption > damage) {
				victim.setAbsorptionAmount(absorption - damage);
				return true;
			}
			victim.setAbsorptionAmount(0);
		}
		if(victim.getHealth() > victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
			victim.setHealth(victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		victim.setHealth(victim.getHealth() - damage);
		
//		victim.setLastDamageCause(e);
//		victim.setNoDamageTicks(0);
//		BukkitAdapter.adapt(victim).setNoDamageTicks(0);
//		Bukkit.broadcastMessage("§aTest1 "+victim.getName()+"§r§f "+((int) victim.getHealth())+" "+((int) damage));
//		DamageSource reason = getDamageSource(damager, victim, e.getCause());
//		EntityLiving target = ((CraftLivingEntity) victim).getHandle();
//		target.a(reason, (float) damage);
//		victim.setNoDamageTicks(0);
//		Bukkit.broadcastMessage("§bTest2 "+victim.getName()+"§r§f "+((int) victim.getHealth()));
		return true;
	}
	
	@SuppressWarnings({ "unused" })
	private static DamageSource getDamageSource(org.bukkit.entity.Entity damager, LivingEntity victim, DamageCause cause) {
		DamageSource src = DamageSource.builder(DamageType.GENERIC).build();
		
		return src;
	}
	
}
