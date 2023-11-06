package me.Vark123.EpicRPG.MMExtension;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
import io.lumine.mythic.core.skills.SkillCondition;
import me.Vark123.EpicRPG.MMExtension.Conditions.GeneralMobsInRadiusCondition;
import me.Vark123.EpicRPG.MMExtension.Conditions.HealthCheckpointCondition;
import me.Vark123.EpicRPG.MMExtension.Conditions.SzansaCondition;

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
		}
	}
	
}
