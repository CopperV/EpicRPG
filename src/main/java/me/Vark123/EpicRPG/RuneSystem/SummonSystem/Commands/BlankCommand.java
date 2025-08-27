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

public class BlankCommand extends ASummonCommand {

	public BlankCommand() {
		super("blank", new SummonCommandItem(Material.SPAWNER, "§7Pusta Komenda", Arrays.asList("Nic nie robi", "To tylko testowy rozkaz")));
	}

	@Override
	public void apply(Player owner, ActiveMob summon) {
		Location loc1 = owner.getLocation().clone().add(0,1.3,0);
		Location loc2 = BukkitAdapter.adapt(summon.getEntity().getEyeLocation());
		
		loc2.getWorld().playSound(loc2, Sound.ENTITY_WOLF_HOWL, 2f, 0.8f);
		Utils.drawLine(Particle.SMOKE, loc1, loc2, 0.2, 2, 0.05f, 0.05f, 0.05f, 0.02f);
	}

}
