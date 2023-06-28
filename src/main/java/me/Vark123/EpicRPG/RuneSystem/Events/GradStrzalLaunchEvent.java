package me.Vark123.EpicRPG.RuneSystem.Events;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class GradStrzalLaunchEvent implements Listener {

	private static List<UUID> reservedUUID = new ArrayList<>();
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onLaunch(EntityShootBowEvent e) {
		if(e.isCancelled())
			return;
		if(reservedUUID.contains(e.getProjectile().getUniqueId())) {
			reservedUUID.remove(e.getProjectile().getUniqueId());
			return;
		}
		if(!(e.getProjectile() instanceof AbstractArrow))
			return;
		if(!(e.getEntity() instanceof Player))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer((Player) e.getEntity());
		if(!rpg.getModifiers().hasGradStrzal())
			return;
		
		AbstractArrow arrow = (AbstractArrow) e.getProjectile();
		Vector velocity = e.getProjectile().getVelocity();
		double wsplY = velocity.getY()/velocity.length();
		if(wsplY < 0.75)
			return;
		
		if(!arrow.hasMetadata("rpg_bow"))
			return;
		ItemStack bow = (ItemStack) arrow.getMetadata("rpg_bow").get(0).value();
		
		double power = e.getForce();
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				if(arrow == null || arrow.isDead())
					return;
				
				Random rand = new Random();
				Location loc = arrow.getLocation();
				arrow.remove();
				
				new BukkitRunnable() {
					int timer = 0;
					@Override
					public void run() {
						if(timer >= 4) {
							this.cancel();
							return;
						}
						++timer;
						for(int i = 0; i < 25; ++i) {
							double x = rand.nextDouble()*30-15;
							double z = rand.nextDouble()*30-15;
							Location tmp = loc.clone().add(x,0,z);
							Arrow tmpArrow = tmp.getWorld().spawnArrow(tmp, new Vector(0, -1, 0), (float) power, 0);
							reservedUUID.add(tmpArrow.getUniqueId());
							tmpArrow.setShooter(rpg.getPlayer());
							EntityShootBowEvent event = new EntityShootBowEvent(rpg.getPlayer(), bow, null, tmpArrow, EquipmentSlot.HAND, (float) power, false);
							Bukkit.getPluginManager().callEvent(event);
						}
					}
				}.runTaskTimer(Main.getInstance(), 0, 10);
				
			}
		}.runTaskLater(Main.getInstance(), 20*2);
	}
	
}
