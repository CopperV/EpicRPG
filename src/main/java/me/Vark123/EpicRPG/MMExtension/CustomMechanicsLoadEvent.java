package me.Vark123.EpicRPG.MMExtension;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.mythic.core.skills.SkillMechanic;
import me.Vark123.EpicRPG.MMExtension.Mechanics.AuraPullMechanic;
import me.Vark123.EpicRPG.MMExtension.Mechanics.ChangeRegionFlagMechanic;
import me.Vark123.EpicRPG.MMExtension.Mechanics.EchoMechanic;
import me.Vark123.EpicRPG.MMExtension.Mechanics.MalevolenceMechanic;
import me.Vark123.EpicRPG.MMExtension.Mechanics.RemoveTargetMechanic;
import me.Vark123.EpicRPG.MMExtension.Mechanics.SetTargetMechanic;

public class CustomMechanicsLoadEvent implements Listener {

	@EventHandler
	public void onMechanicLoad(MythicMechanicLoadEvent e) {
		String mech = e.getMechanicName().toLowerCase();
		SkillMechanic mechanic;
		switch(mech) {
			case "echo":
				mechanic = new EchoMechanic(MythicBukkit.inst().getSkillManager(), e.getContainer().getFile(), e.getConfig().getLine(), e.getConfig());
				e.register(mechanic);
				break;
			case "settarget":
				mechanic = new SetTargetMechanic(MythicBukkit.inst().getSkillManager(), e.getContainer().getFile(), e.getConfig().getLine(), e.getConfig());
				e.register(mechanic);
				break;
			case "aurapull":
				mechanic = new AuraPullMechanic(MythicBukkit.inst().getSkillManager(), e.getContainer().getFile(), e.getConfig().getLine(), e.getConfig());
				e.register(mechanic);
				break;
			case "malevolence":
				mechanic = new MalevolenceMechanic(MythicBukkit.inst().getSkillManager(), e.getContainer().getFile(), e.getConfig().getLine(), e.getConfig());
				e.register(mechanic);
				break;
			case "removetarget":
				mechanic = new RemoveTargetMechanic(MythicBukkit.inst().getSkillManager(), e.getContainer().getFile(), e.getConfig().getLine(), e.getConfig());
				e.register(mechanic);
				break;
			case "setflag":
				mechanic = new ChangeRegionFlagMechanic(MythicBukkit.inst().getSkillManager(), e.getContainer().getFile(), e.getConfig().getLine(), e.getConfig());
				e.register(mechanic);
				break;
		}
	}
	
}
