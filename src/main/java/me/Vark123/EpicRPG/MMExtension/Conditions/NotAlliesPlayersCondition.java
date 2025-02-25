package me.Vark123.EpicRPG.MMExtension.Conditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.skills.SkillCondition;

public class NotAlliesPlayersCondition extends SkillCondition implements IEntityCondition {
	
	private static final List<String> playerAlliesFactions = new ArrayList<>(Arrays.asList(
			"DEFENDERS",
			"SUMMONS",
			"NEUTRAL",
			"NEUTRAL_ANIMALS"
	));
	
	public NotAlliesPlayersCondition(final String line, final MythicLineConfig mlc) {
		super(line);
	}

	@Override
	public boolean check(AbstractEntity aEntity) {
		if(aEntity.getBukkitEntity() instanceof Player)
			return false;
		
		if(!MythicBukkit.inst().getMobManager().isActiveMob(aEntity))
			return false;
		
		ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(aEntity);
		if(!aMob.hasFaction())
			return true;
		
    	return !playerAlliesFactions.contains(aMob.getFaction().toUpperCase());
	}
	
	
}
