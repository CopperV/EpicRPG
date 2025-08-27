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
		Location loc1 = owner.getLocation().clone().add(0,1.3,0);
		Location loc2 = BukkitAdapter.adapt(summon.getEntity().getEyeLocation());
		
		loc2.getWorld().playSound(loc2, Sound.ENTITY_WOLF_HOWL, 2f, 0.8f);
		Utils.drawLine(Particle.SMOKE, loc1, loc2, 0.2, 2, 0.05f, 0.05f, 0.05f, 0.02f);
		
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
			summon.setTarget(BukkitAdapter.adapt(e));
		});
	}
	
	private double score(LivingEntity e, Player owner, Location loc, double radius, double angle) {
	    double distNorm = e.getLocation().distanceSquared(loc) / (radius * radius);
	    double angleNorm = Utils.getAngle(owner, e) / angle;
	    return distNorm * 0.25 + angleNorm * 0.75;
	}
}
