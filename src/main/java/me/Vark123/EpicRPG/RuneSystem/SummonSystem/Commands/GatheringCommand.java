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

public class GatheringCommand extends ASummonCommand {

	public GatheringCommand() {
		super("gathering", new SummonCommandItem(Material.TOTEM_OF_UNDYING, "§3Zbiorka", Arrays.asList(
				"§7Teleportuje wszystkie przywolance",
				"§7do wlasciciela jednoczesnie resetujac",
				"§7ich obecny cel")));
	}

	@Override
	public void apply(Player owner, ActiveMob summon) {
		Location loc1 = owner.getLocation().clone();
		Location loc2 = BukkitAdapter.adapt(summon.getEntity().getLocation()).clone().add(0, 0.5, 0);
		
		loc2.getWorld().playSound(loc2, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1.2f, 0.75f);
		loc2.getWorld().spawnParticle(Particle.FLASH, loc2, 1);
		
		summon.getEntity().teleport(BukkitAdapter.adapt(loc1));
		summon.setTarget(null);

		loc2 = BukkitAdapter.adapt(summon.getEntity().getLocation()).clone().add(0, 0.5, 0);
		loc2.getWorld().playSound(loc2, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1.2f, 0.75f);
		loc2.getWorld().spawnParticle(Particle.FLASH, loc2, 1);
	}
}
