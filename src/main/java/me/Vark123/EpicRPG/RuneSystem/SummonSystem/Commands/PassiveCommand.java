package me.Vark123.EpicRPG.RuneSystem.SummonSystem.Commands;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.ASummonCommand;

public class PassiveCommand extends ASummonCommand {

	
	public PassiveCommand() {
		super("passive", new SummonCommandItem(Material.SHIELD, "§3Pasywny", Arrays.asList(
				"§7Przywolancy przechodza w tryb pasywny,",
				"§7w ktorym nic nie robia",
				"§7oraz nie pobieraja many")));
	}

	@Override
	public void apply(Player owner, ActiveMob summon) {
		Random rand = new Random();
		Location loc = BukkitAdapter.adapt(summon.getEntity().getEyeLocation());
		
		loc.getWorld().playSound(loc, Sound.BLOCK_TRIAL_SPAWNER_DETECT_PLAYER, 1.25f, 0.8f);

		double minRadius = 0.25f;
		double maxRadius = 0.75f;
		for(int i = 0; i < 25; ++i) {
			double angle1 = rand.nextDouble(Math.PI*2);
			double angle2 = rand.nextDouble(Math.PI*2);
			double radius = rand.nextDouble(minRadius, maxRadius);
			float force = rand.nextFloat(0.2f, 1.5f);
			
			double x = Math.cos(angle1) * Math.sin(angle2) * radius;
			double y = Math.sin(angle1) * radius;
			double z = Math.cos(angle1) * Math.cos(angle2) * radius;
			
			Location point = loc.clone().add(x,y,z);
			point.getWorld().spawnParticle(Particle.ENCHANTED_HIT, point, 0, 0f, -1f, 0f, force);
		}
		
		summon.setStance("default");
		summon.signalMob(BukkitAdapter.adapt(owner), "STOP_AI");
		summon.setTarget(null);
	}
}
