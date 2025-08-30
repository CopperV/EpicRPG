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

public class ResetTargetCommand extends ASummonCommand {

	public ResetTargetCommand() {
		super("reset_target", new SummonCommandItem(Material.ENDER_EYE, "§3Resetuj cel", Arrays.asList(
				"§7Resetuje obecny cel przywolancow po to",
				"§7by mogli na nowo wybrac cel",
				"§7wedlug ich obecnego stanu")));
	}

	@Override
	public void apply(Player owner, ActiveMob summon) {
		Location loc = BukkitAdapter.adapt(summon.getEntity().getLocation()).clone().add(0, 0.1, 0);
		Location loc2 = BukkitAdapter.adapt(summon.getEntity().getEyeLocation()).clone();
		loc.getWorld().playSound(loc, Sound.BLOCK_TRIAL_SPAWNER_BREAK, 1.5f, 0.7f);
		
		Random rand = new Random();
		double radius = 1.5;
		int points = 16;
		
		for(int i = 0; i < points; ++i) {
			double angle = Math.PI * 2 * (double) i / (double) points;
			
			double x = Math.sin(angle) * radius;
			double z = Math.cos(angle) * radius;
			
			Location p1 = loc.clone().add(x, 0, z);
			
			p1.getWorld().spawnParticle(Particle.SMOKE, p1, 1, 0, 0, 0, 0);
		}
		for(int i = 0; i < 25; ++i) {
			double angle = rand.nextDouble(Math.PI*2);
			
			double x = Math.sin(angle) * radius;
			double z = Math.cos(angle) * radius;
			
			Location p1 = loc.clone().add(x, 0, z);
			
			p1.getWorld().spawnParticle(Particle.CRIT, p1, 0, 0, 1, 0, rand.nextFloat(0.1f, 0.6f));
		}
		
		loc2.getWorld().spawnParticle(Particle.ASH, loc2, 40, 0.5f, 0.5f, 0.5f, 0.03f);
		
		summon.setTarget(null);
	}
}
