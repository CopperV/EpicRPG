package me.Vark123.EpicRPG.RuneSystem.SummonSystem.Commands;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.ASummonCommand;
import me.Vark123.EpicRPG.Utils.Utils;

public class PassiveCommand extends ASummonCommand {

	public PassiveCommand() {
		super("passive", new SummonCommandItem(Material.SHIELD, "§3Pasywny", Arrays.asList(
				"§7Przywolancy przechodza w tryb pasywny,",
				"§7w ktorym nic nie robia",
				"§7oraz nie pobieraja many")));
	}

	@Override
	public void apply(Player owner, ActiveMob summon) {
		Location loc1 = owner.getLocation().clone().add(0,1.3,0);
		Location loc2 = BukkitAdapter.adapt(summon.getEntity().getEyeLocation());
		
		loc2.getWorld().playSound(loc2, Sound.ENTITY_WOLF_HOWL, 2f, 0.8f);
		Utils.drawLine(Particle.SMOKE, loc1, loc2, 0.2, 2, 0.05f, 0.05f, 0.05f, 0.02f);
		
		summon.setStance("default");
		summon.signalMob(BukkitAdapter.adapt(owner), "STOP_AI");
		summon.setTarget(null);
	}
}
