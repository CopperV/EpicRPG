package me.Vark123.EpicRPG.MMExtension.Conditions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityComparisonCondition;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.mobs.MobExecutor;
import io.lumine.mythic.core.skills.SkillCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class AlliesPlayersCondition extends SkillCondition implements IEntityComparisonCondition {
	
	private final MobExecutor manager = MythicBukkit.inst().getMobManager();
	
	public AlliesPlayersCondition(final String line, final MythicLineConfig mlc) {
		super(line);
	}

	@Override
	public boolean check(AbstractEntity aCaster, AbstractEntity aEntity) {
		Entity caster = BukkitAdapter.adapt(aCaster);
		Entity entity = BukkitAdapter.adapt(aEntity);
		if(!(caster instanceof LivingEntity || entity instanceof LivingEntity))
			return false;
		
		LivingEntity leCaster = (LivingEntity) caster;
		LivingEntity leEntity = (LivingEntity) entity;
		
		if(caster instanceof Player)
			return Utils.isEntityAlly(leCaster, leEntity);
		
		if(manager.isActiveMob(aCaster)) {
			ActiveMob mob = manager.getMythicMobInstance(aCaster);
			if(mob.getOwnerUUID().isPresent() && Bukkit.getPlayer(mob.getOwnerUUID().get()) != null)
				return Utils.isEntityAlly(leCaster, leEntity);
		}
		
		return entity instanceof Player || Utils.isEntityMythicMobAlly(aEntity);
	}
	
}
