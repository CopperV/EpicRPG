package me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;

public class NonPvPRuneHitCondition implements IRuneHitCondition {

	@Override
	public boolean check(Player caster, LivingEntity hit) {
		if(hit instanceof Player)
			return false;
		
		if(!MythicBukkit.inst().getMobManager().isMythicMob(hit))
			return false;
		
		ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(hit);
		if(aMob.isDead() || aMob.getType().getIsInvincible()
				|| (aMob.hasFaction() && aMob.getFaction().equals("ALLY")))
			return false;
		
		if(!aMob.getEntity().isDamageable())
			return false;
		
		return true;
	}

}
