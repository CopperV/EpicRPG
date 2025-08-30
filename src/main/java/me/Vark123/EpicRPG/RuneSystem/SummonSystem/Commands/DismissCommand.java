package me.Vark123.EpicRPG.RuneSystem.SummonSystem.Commands;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.ASummonCommand;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.SummonManager;
import me.Vark123.EpicRPG.Utils.Utils;

public class DismissCommand extends ASummonCommand {

	public DismissCommand() {
		super("dismiss", new SummonCommandItem(Material.WIND_CHARGE, "§3Odwolaj", Arrays.asList(
				"§7Odwoluje wszystkie poslusznych",
				"§7Tobie przywolancow")));
	}

	@Override
	public void apply(Player owner, ActiveMob summon) {
		Location loc = BukkitAdapter.adapt(summon.getEntity().getLocation()).clone().add(0, 0.1, 0);
		loc.getWorld().playSound(loc, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.1f, 0.9f);
		
		Random rand = new Random();
		double radius = 1.5;
		int points = 16;
		
		for(int i = 0; i < points; ++i) {
			double angle = Math.PI * 2 * (double) i / (double) points;
			
			double x = Math.sin(angle) * radius;
			double z = Math.cos(angle) * radius;
			
			Location p1 = loc.clone().add(x, 0, z);
			Location p2 = p1.clone().add(0, 4, 0);
			
			p1.getWorld().spawnParticle(Particle.SMOKE, p1, 1, 0, 0, 0, 0);
			p2.getWorld().spawnParticle(Particle.SMOKE, p2, 1, 0, 0, 0, 0);
		}
		for(int i = 0; i < 25; ++i) {
			double angle = rand.nextDouble(Math.PI*2);
			
			double x = Math.sin(angle) * radius;
			double z = Math.cos(angle) * radius;
			
			Location p1 = loc.clone().add(x, 0, z);
			
			p1.getWorld().spawnParticle(Particle.SMOKE, p1, 0, 0, 1, 0, rand.nextFloat(0.03f, 0.15f));
		}
		
		Location pentagramLoc = loc.clone().add(0,4,0);
		Utils.drawPentagram(
				Particle.SMALL_FLAME,
				pentagramLoc,
				new Vector(0,1,0),
				5,
				radius,
				0.05,
				2,
				rand.nextDouble(Math.PI * 2));
		
		SummonManager.get().dismissSummon(summon);
	}
}
