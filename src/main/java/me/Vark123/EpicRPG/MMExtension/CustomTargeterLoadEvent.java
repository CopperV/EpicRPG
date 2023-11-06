package me.Vark123.EpicRPG.MMExtension;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.lumine.mythic.api.skills.targeters.ISkillTargeter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicTargeterLoadEvent;
import me.Vark123.EpicRPG.MMExtension.Targeters.AngleTargeter;
import me.Vark123.EpicRPG.MMExtension.Targeters.CircleSegmentTargeter;
import me.Vark123.EpicRPG.MMExtension.Targeters.ConditionalTarget;
import me.Vark123.EpicRPG.MMExtension.Targeters.ForwardLevelTargeter;
import me.Vark123.EpicRPG.MMExtension.Targeters.WorldLimitTargeter;

public class CustomTargeterLoadEvent implements Listener {

	@EventHandler
	public void onTargeterLoad(MythicTargeterLoadEvent e) {
		String targeter = e.getTargeterName().toLowerCase();
		ISkillTargeter target;
		switch(targeter) {
			case "conditional":
				target = new ConditionalTarget(MythicBukkit.inst().getSkillManager(), e.getConfig());
				e.register(target);
				break;
			case "forwardlevel":
				target = new ForwardLevelTargeter(MythicBukkit.inst().getSkillManager(), e.getConfig());
				e.register(target);
				break;
			case "segmentcircle":
				target = new CircleSegmentTargeter(MythicBukkit.inst().getSkillManager(), e.getConfig());
				e.register(target);
				break;
			case "angletarget":
				target = new AngleTargeter(MythicBukkit.inst().getSkillManager(), e.getConfig());
				e.register(target);
				break;
			case "worldlimit":
				target = new WorldLimitTargeter(MythicBukkit.inst().getSkillManager(), e.getConfig());
				e.register(target);
				break;
		}
	}
	
}
