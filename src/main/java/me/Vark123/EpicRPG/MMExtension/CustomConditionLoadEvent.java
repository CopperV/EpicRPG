package me.Vark123.EpicRPG.MMExtension;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
import io.lumine.mythic.core.skills.SkillCondition;
import me.Vark123.EpicRPG.MMExtension.Conditions.AlliesPlayersCondition;
import me.Vark123.EpicRPG.MMExtension.Conditions.AnyStanceCondition;
import me.Vark123.EpicRPG.MMExtension.Conditions.BlockBelowCondition;
import me.Vark123.EpicRPG.MMExtension.Conditions.CasterFieldOfViewCondition;
import me.Vark123.EpicRPG.MMExtension.Conditions.DistanceFromSpawnLocationCondition;
import me.Vark123.EpicRPG.MMExtension.Conditions.EnoughManaCondition;
import me.Vark123.EpicRPG.MMExtension.Conditions.GeneralMobsInRadiusCondition;
import me.Vark123.EpicRPG.MMExtension.Conditions.HasValidTargetCondition;
import me.Vark123.EpicRPG.MMExtension.Conditions.HealthCheckpointCondition;
import me.Vark123.EpicRPG.MMExtension.Conditions.NotAlliesPlayersCondition;
import me.Vark123.EpicRPG.MMExtension.Conditions.SneakingCondition;
import me.Vark123.EpicRPG.MMExtension.Conditions.SummonAlliesPlayersCondition;
import me.Vark123.EpicRPG.MMExtension.Conditions.SzansaCondition;
import me.Vark123.EpicRPG.MMExtension.Conditions.VariableStanceCondition;

public class CustomConditionLoadEvent implements Listener {

	@EventHandler
	public void onConditionLoad(MythicConditionLoadEvent e) {
		String conditioner = e.getConditionName().toLowerCase();
		SkillCondition condition;
		switch(conditioner) {
			case "generalmobsinradius":
				condition = new GeneralMobsInRadiusCondition(e.getConfig().getLine(), e.getConfig());
				e.register(condition);
				break;
			case "szansa":
				condition = new SzansaCondition(e.getConfig().getLine(), e.getConfig());
				e.register(condition);
				break;
			case "hpcheck":
				condition = new HealthCheckpointCondition(e.getConfig().getLine(), e.getConfig());
				e.register(condition);
				break;
			case "casterfieldofview":
				condition = new CasterFieldOfViewCondition(e.getConfig().getLine(), e.getConfig());
				e.register(condition);
				break;
			case "vstance":
				condition = new VariableStanceCondition(e.getConfig().getLine(), e.getConfig());
				e.register(condition);
				break;
			case "sneaking":
				condition = new SneakingCondition(e.getConfig().getLine(), e.getConfig());
				e.register(condition);
				break;
			case "blockbelow":
				condition = new BlockBelowCondition(e.getConfig().getLine(), e.getConfig());
				e.register(condition);
				break;
			case "distancefromspawnlocation":
				condition = new DistanceFromSpawnLocationCondition(e.getConfig().getLine(), e.getConfig());
				e.register(condition);
				break;
			case "alliesplayers":
				condition = new AlliesPlayersCondition(e.getConfig().getLine(), e.getConfig());
				e.register(condition);
				break;
			case "summonalliesplayers":
				condition = new SummonAlliesPlayersCondition(e.getConfig().getLine(), e.getConfig());
				e.register(condition);
				break;
			case "notalliesplayers":
				condition = new NotAlliesPlayersCondition(e.getConfig().getLine(), e.getConfig());
				e.register(condition);
				break;
			case "enoughmana":
				condition = new EnoughManaCondition(e.getConfig().getLine(), e.getConfig());
				e.register(condition);
				break;
			case "anystance":
				condition = new AnyStanceCondition(e.getConfig().getLine(), e.getConfig());
				e.register(condition);
				break;
			case "hasvalidtarget":
				condition = new HasValidTargetCondition(e.getConfig().getLine(), e.getConfig());
				e.register(condition);
				break;
		}
	}
	
}
