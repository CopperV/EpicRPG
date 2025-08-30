package me.Vark123.EpicRPG.MMExtension.Conditions;

import java.util.UUID;

import org.bukkit.Bukkit;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityComparisonCondition;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.mobs.MobExecutor;
import io.lumine.mythic.core.skills.SkillCondition;

public class IsTargetingAlliedSummonCondition extends SkillCondition implements IEntityComparisonCondition {
	
	private MobExecutor mobManager = MythicBukkit.inst().getMobManager();
	
	public IsTargetingAlliedSummonCondition(final String line, final MythicLineConfig mlc) {
		super(line);
	}

	@Override
	public boolean check(AbstractEntity caster, AbstractEntity targeted) {
		if(!mobManager.isActiveMob(caster) || !mobManager.isActiveMob(targeted))
			return false;
		
		ActiveMob casterMob = mobManager.getMythicMobInstance(caster);
		if(!casterMob.getOwnerUUID().isPresent() || Bukkit.getPlayer(casterMob.getOwnerUUID().get()) == null)
			return false;
		
		AbstractEntity target = targeted.getTarget();
		
		if(target == null || target.isDead() || !MythicBukkit.inst().getMobManager().isActiveMob(target))
			return false;
		
		UUID ownerUUID = casterMob.getOwnerUUID().get();
		ActiveMob targetMob = mobManager.getMythicMobInstance(target);
		if(!targetMob.getOwnerUUID().isPresent() || !targetMob.getOwnerUUID().get().equals(ownerUUID)
				|| targetMob.getVariables().has("tauntable"))
			return false;
		
		return true;
	}
}
