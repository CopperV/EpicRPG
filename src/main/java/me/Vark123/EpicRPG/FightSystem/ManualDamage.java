package me.Vark123.EpicRPG.FightSystem;

import org.bukkit.EntityEffect;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R2.event.CraftEventFactory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.player.EntityHuman;

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
	
	@SuppressWarnings({ "incomplete-switch", "unused" })
	private static DamageSource getDamageSource(org.bukkit.entity.Entity damager, LivingEntity victim, DamageCause cause) {
		DamageSource src = DamageSource.n;
		final Entity source = ((CraftEntity) damager).getHandle();
		
		switch(cause) {
			case CONTACT: {
				src = DamageSource.j;
				break;
			}
			case ENTITY_ATTACK: {
				if(source instanceof EntityHuman) {
					final EntityHuman player = (EntityHuman) source;
					src = DamageSource.a(player);
					break;
				}
				EntityLiving entityLiving = (EntityLiving) source;
				src = DamageSource.c(entityLiving);
				break;
			}
			case ENTITY_SWEEP_ATTACK: {
				if (src != DamageSource.n) {
					src = src.sweep();
					break;
				}
				break;
			}
			case PROJECTILE: {
				Entity handle = null;
				final CraftEntity bukkitEntity = source.getBukkitEntity();
				Label_0293: {
					if(bukkitEntity instanceof Projectile) {
						final Projectile projectile = (Projectile) bukkitEntity;
						if(projectile.getShooter() instanceof org.bukkit.entity.Entity) {
							handle = ((CraftEntity) projectile.getShooter()).getHandle();
							break Label_0293;
						}
					}
					handle = null;
				}
				src = DamageSource.b(source, handle);
				break;
			}
			case SUFFOCATION: {
				src = DamageSource.f;
				break;
			}
			case FALL: {
				src = DamageSource.k;
				break;
			}
			case FIRE: {
				src = DamageSource.a;
				break;
			}
			case FIRE_TICK: {
				src = DamageSource.c;
				break;
			}
			case MELTING: {
				src = CraftEventFactory.MELTING;
				break;
			}
			case LAVA: {
				src = DamageSource.d;
				break;
			}
			case DROWNING: {
				src = DamageSource.h;
				break;
			}
			case BLOCK_EXPLOSION: {
				EntityLiving entityLiving2 = null;
				Label_0394: {
					if(source instanceof TNTPrimed) {
						final TNTPrimed tntPrimed = (TNTPrimed) source;
						if(tntPrimed.getSource() instanceof EntityLiving) {
							entityLiving2 = (EntityLiving) tntPrimed.getSource();
							break Label_0394;
						}
					}
					entityLiving2 = null;
				}
				src = DamageSource.d(entityLiving2);
				break;
			}
			case ENTITY_EXPLOSION: {
				EntityLiving entityLiving3;
				if (source instanceof EntityLiving) {
					entityLiving3 = (EntityLiving) source;
				} else {
					entityLiving3 = null;
				}
				src = DamageSource.d(entityLiving3);
				break;
			}
			case VOID: {
				src = DamageSource.m;
				break;
			}
			case LIGHTNING: {
				src = DamageSource.b;
				break;
			}
			case STARVATION: {
				src = DamageSource.i;
				break;
			}
			case POISON: {
				src = CraftEventFactory.POISON;
				break;
			}
			case MAGIC: {
				src = DamageSource.o;
				break;
			}
			case WITHER: {
				src = DamageSource.p;
				break;
			}
			case FALLING_BLOCK: {
				src = DamageSource.r;
				break;
			}
			case THORNS: {
				src = DamageSource.a(source);
				break;
			}
			case DRAGON_BREATH: {
				src = DamageSource.s;
				break;
			}
			case FLY_INTO_WALL: {
				src = DamageSource.l;
				break;
			}
			case HOT_FLOOR: {
				src = DamageSource.e;
				break;
			}
			case CRAMMING: {
				src = DamageSource.g;
				break;
			}
			case DRYOUT: {
				src = DamageSource.t;
				break;
			}
		}
		return src;
	}
	
}
