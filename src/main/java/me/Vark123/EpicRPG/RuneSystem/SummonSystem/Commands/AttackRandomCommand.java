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

public class AttackRandomCommand extends ASummonCommand {

	public AttackRandomCommand() {
		super("attack_random", new SummonCommandItem(Material.GOLDEN_SHOVEL, "§3Atakuj §7[§3Losowy§7]", Arrays.asList(
				"§7Nakazuje przywolancowi atakowac",
				"§7losowego przeciwnika w swoim zasiegu")));
	}

	@Override
	public void apply(Player owner, ActiveMob summon) {
		Location loc = BukkitAdapter.adapt(summon.getEntity().getLocation()).clone().add(0, 0.1, 0);
		loc.getWorld().playSound(loc, Sound.ENTITY_EVOKER_CAST_SPELL, 1f, 0.9f);
		
		Random rand = new Random();
		double radius = 1.5;
		int points = 16;
		
		for(int i = 0; i < points; ++i) {
			double angle = Math.PI * 2 * (double) i / (double) points;
			
			double x = Math.sin(angle) * radius;
			double z = Math.cos(angle) * radius;
			
			Location p1 = loc.clone().add(x, 0, z);
			
			p1.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, p1, 1, 0, 0, 0, 0);
		}
		for(int i = 0; i < 25; ++i) {
			double angle = rand.nextDouble(Math.PI*2);
			
			double x = Math.sin(angle) * radius;
			double z = Math.cos(angle) * radius;
			
			Location p1 = loc.clone().add(x, 0, z);
			
			p1.getWorld().spawnParticle(Particle.FIREWORK, p1, 0, 0, 1, 0, rand.nextFloat(0.03f, 0.15f));
		}
		
		summon.setStance("attack_random");
		summon.signalMob(BukkitAdapter.adapt(owner), "CLEAR_AI");
		summon.setTarget(null);
	}
}
