package me.Vark123.EpicRPG.RuneSystem.SummonSystem.Commands;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.ASummonCommand;
import me.Vark123.EpicRPG.Utils.Utils;

public class BreakThroughCommand extends ASummonCommand {

	private final double radius = 30;
	private final double angle = 30;
	
	public BreakThroughCommand() {
		super("break_through", new SummonCommandItem(Material.NETHERITE_AXE, "§3Przelam", Arrays.asList(
				"§7Nakazuje przywolancom atakowac cel,",
				"§7na ktory patrzy wlasciciel,",
				"§7o ile jakikolwiek istnieje")));
	}

	@Override
	public void apply(Player owner, ActiveMob summon) {
		Location loc1 = owner.getLocation().clone().add(0,1.1,0);
		Location loc2 = BukkitAdapter.adapt(summon.getEntity().getLocation()).clone().add(0, 0.65, 0);
		
		Location loc = owner.getLocation().clone();
		owner.getWorld().getNearbyEntities(loc, radius, radius, radius, entity -> {
			if(entity.getLocation().distanceSquared(loc) > radius*radius)
				return false;
			
			if(!(entity instanceof LivingEntity le))
				return false;
			
			if(Utils.getAngle(owner, entity) > angle)
				return false;
			
			return !Utils.isEntityAlly(owner, le);
		}).stream().map(entity -> (LivingEntity) entity).min((e1, e2) -> Double.compare(
		        score(e1, owner, loc, radius, angle),
		        score(e2, owner, loc, radius, angle)
		)).ifPresent(e -> {
			Location targetLoc = e.getLocation().clone().add(0,1,0);
			Location eyeLoc = e.getEyeLocation().clone();
			
			loc1.getWorld().playSound(loc1, Sound.ENTITY_WITHER_SHOOT, 0.8f, 0.8f);
			Utils.drawLine(Particle.ANGRY_VILLAGER, loc1, targetLoc, 0.3, 2, 0.05f, 0.05f, 0.05f, 0.02f);
			Utils.drawLine(Particle.HAPPY_VILLAGER, loc2, targetLoc, 0.2, 2, 0.05f, 0.05f, 0.05f, 0.02f);
			eyeLoc.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, eyeLoc, 25, 0.4f, 0.4f, 0.4f, 0.05f);
			
			summon.setTarget(BukkitAdapter.adapt(e));
		});
	}
	
	private double score(LivingEntity e, Player owner, Location loc, double radius, double angle) {
	    double distNorm = e.getLocation().distanceSquared(loc) / (radius * radius);
	    double angleNorm = Utils.getAngle(owner, e) / angle;
	    return distNorm * 0.25 + angleNorm * 0.75;
	}
}
