package me.Vark123.EpicRPG.Players.Components.Markers;

import java.io.Serializable;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import lombok.Getter;
import me.Vark123.EpicOptions.OptionsAPI;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Options.Serializables.MarkerSerializable;
import me.Vark123.EpicRPG.Players.RpgPlayer;

@Getter
public class EpicMarker implements Serializable {

	private static final long serialVersionUID = 5475693641779253405L;

	private static final int DISTANCE = 16;
	
	private static final Ognik ognik = new Ognik();
	
	private RpgPlayer rpg;
	private MarkerSerializable marker;
	
	private BukkitTask ognikTask;
	
	public EpicMarker(RpgPlayer rpg) {
		this.rpg = rpg;
		
		Player p = rpg.getPlayer();
		OptionsAPI.get().getPlayerManager().getPlayerOptions(p)
			.ifPresent(op -> {
				op.getPlayerOptionByID("epicrpg_markers").ifPresent(option -> {
					this.marker = (MarkerSerializable) option.getValue();
				});
			});
	}
	
	public void hideMarker() {
		if(!marker.isCreated() || !marker.isShowed())
			return;
		
		if(ognikTask != null && !ognikTask.isCancelled())
			ognikTask.cancel();
	}
	
	public void showMarker() {
		if(!marker.isCreated() || marker.isShowed())
			return;

		if(ognikTask != null && !ognikTask.isCancelled())
			ognikTask.cancel();
		
		ognikTask = new BukkitRunnable() {
			Player p = rpg.getPlayer();
			@Override
			public void run() {
				if(!p.getWorld().getName().equalsIgnoreCase(marker.getWorld()))
					return;
				Location ognikLocation = p.getLocation().clone().add(0, 1.25, 0);
				Location targetLocation = new Location(ognikLocation.getWorld(), marker.getX(), ognikLocation.getY(), marker.getZ());
				boolean isClose = false;
				if(ognikLocation.distance(targetLocation) > DISTANCE) {
					Vector vec = new Vector(
							targetLocation.getX() - ognikLocation.getX(),
							0,
							targetLocation.getZ() - ognikLocation.getZ())
							.normalize()
							.multiply(DISTANCE);
					targetLocation = ognikLocation.clone().add(vec);
				} else {
					isClose = true;
				}
				spawnOgnik(targetLocation, isClose);
			}
		}.runTaskTimerAsynchronously(Main.getInstance(), 0, 4);
	}
	
	private void spawnOgnik(Location loc, boolean isClose) {
		Player p = rpg.getPlayer();
		Location spawnLoc = loc.clone().add(0, ognik.getY(), 0);
		for(double[] coords : ognik.getCoordinates()) {
			Vector vec = new Vector(coords[0], coords[1], coords[2]);
			Location tmpLoc = spawnLoc.clone().add(vec);
			p.spawnParticle(ognik.getDefaultPartice(), tmpLoc, 1, 0, 0, 0, 0);
			
			Particle addPartcile = isClose ? ognik.getFoundParticle() : ognik.getSearchingParticle();
			if(new Random().nextInt(20) != 0)
				continue;
			p.spawnParticle(addPartcile, tmpLoc, 1, 0, 0, 0, 0.02);
		}
	}
	
	@Getter
	private static class Ognik {
		private static final int INTERVAL = 4;
		private static final double AMPLITUDE = 0.4;
		private static final double MODIFIER = 0.075;
		
		private double y = 0;
		private BukkitTask task;
		
		private Particle defaultPartice;
		private Particle searchingParticle;
		private Particle foundParticle;
		
		private double[][] coordinates;
		
		public Ognik() {
			defaultPartice = Particle.ELECTRIC_SPARK;
			searchingParticle = Particle.SOUL_FIRE_FLAME;
			foundParticle = Particle.FLAME;
			
			int tmp = 0;
			coordinates = new double[9 * 8 * 2][];
			for(double i = 0; i <= Math.PI; i += Math.PI / 8) {
				double radius = Math.sin(i) * 0.35;
				double y = Math.cos(i) * 0.35;
				for(double a = 0; a < Math.PI * 2; a += Math.PI / 8) {
					double x = Math.cos(a) * radius;
					double z = Math.sin(a) * radius;
					coordinates[tmp++] = new double[] {x, y, z};
				}
			}
			
			task = new BukkitRunnable() {
				int direction = 1;
				@Override
				public void run() {
					if(isCancelled())
						return;
					
					y += MODIFIER * direction;
					if(Math.abs(y) >= AMPLITUDE)
						direction *= -1;
				}
			}.runTaskTimerAsynchronously(Main.getInstance(), 0, INTERVAL);
		}
		
	}
	
}
