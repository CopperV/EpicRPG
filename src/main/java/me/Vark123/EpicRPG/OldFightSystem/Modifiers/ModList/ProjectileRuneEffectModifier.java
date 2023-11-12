package me.Vark123.EpicRPG.OldFightSystem.Modifiers.ModList;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.OldFightSystem.Modifiers.DamageModifier;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.RuneSystem.RuneTimeEffect;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;

public class ProjectileRuneEffectModifier implements DamageModifier {

	@Override
	public double modifyDamage(Entity damager, Entity victim, double damage, DamageCause cause) {
		if(!cause.equals(DamageCause.PROJECTILE))
			return damage;
		
		Entity shooter = (Entity) ((Projectile) damager).getShooter();
		if(!(shooter instanceof Player))
			return damage;
		
		Player p = (Player) shooter;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		
		if(modifiers.hasOgnistaStrzala()) {
			double dmg = damage*0.075;
			if(RuneDamage.directDamageEffect(p, (LivingEntity) victim, 
					Optional.of(dmg), RuneUtils.getCustomTimeEffect(dmg, RuneTimeEffect.FIRE))) {
				Location loc2 = victim.getLocation().add(0, 1, 0);
				loc2.getWorld().spawnParticle(Particle.FLAME, loc2, 15, 0.2, 0.2, 0.2, 0.05);
			}
		}
		if(modifiers.hasZatrutaStrzala()) {
			double dmg = damage*0.03;
			if(RuneDamage.directDamageEffect(p, (LivingEntity) victim, 
					Optional.of(dmg), RuneUtils.getCustomTimeEffect(dmg, RuneTimeEffect.POISON))) {
				Location loc2 = victim.getLocation().add(0, 1, 0);
				loc2.getWorld().spawnParticle(Particle.SLIME, loc2, 15, 0.2, 0.2, 0.2, 0.05);
			}
		}
		if(modifiers.hasKrwawaStrzala()) {
			double dmg = damage * 0.04;
			RuneDamage.directDamageEffect(p, (LivingEntity) victim, 
					Optional.of(dmg), RuneUtils.getCustomTimeEffect(dmg, RuneTimeEffect.BLOOD));
		}
		
		return damage;
	}

}
