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

public class AttackLowestHpCommand extends ASummonCommand {

	public AttackLowestHpCommand() {
		super("attack_lowest_hp", new SummonCommandItem(Material.LEATHER_CHESTPLATE, "§3Atakuj §7[§3Najmniej HP§7]", Arrays.asList(
				"§7Nakazuje przywolancowi atakowac",
				"§7przeciwnikow, ktorzy maja najmniej zycia")));
	}

	@Override
	public void apply(Player owner, ActiveMob summon) {
		Location loc1 = owner.getLocation().clone().add(0,1.3,0);
		Location loc2 = BukkitAdapter.adapt(summon.getEntity().getEyeLocation());
		
		loc2.getWorld().playSound(loc2, Sound.ENTITY_WOLF_HOWL, 2f, 0.8f);
		Utils.drawLine(Particle.SMOKE, loc1, loc2, 0.2, 2, 0.05f, 0.05f, 0.05f, 0.02f);
		
		summon.setStance("attack_lowest_hp");
		summon.signalMob(BukkitAdapter.adapt(owner), "CLEAR_AI");
		summon.setTarget(null);
	}
}
