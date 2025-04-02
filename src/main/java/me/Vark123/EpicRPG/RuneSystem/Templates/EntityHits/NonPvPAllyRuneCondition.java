package me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits;

import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;

public class NonPvPAllyRuneCondition implements IRuneHitCondition {

	@Override
	public boolean check(Player caster, LivingEntity hit) {
		if(hit.equals(caster))
			return true;
		
		if(hit instanceof Player) {
			Player _hit = (Player) hit;
			if(_hit.getGameMode().equals(GameMode.SPECTATOR)
					|| _hit.getGameMode().equals(GameMode.CREATIVE))
				return false;
			return true;
		}
		
		if(!MythicBukkit.inst().getMobManager().isMythicMob(hit))
			return false;
		
		ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(hit);
		if(aMob.isDead())
			return false;
		
		return aMob.hasFaction() && aMob.getFaction().equals("SUMMONS");
	}

}
