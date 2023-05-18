package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class GrupoweLeczenie extends ARune {
	
	List<Player> players;

	public GrupoweLeczenie(ItemStackRune dr, Player p) {
		super(dr, p);
		players = new ArrayList<Player>();
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
		List<Entity> entities = p.getNearbyEntities(dr.getObszar(), dr.getObszar(), dr.getObszar());
		entities.add(p);
		Location loc = p.getLocation();
		loc.add(0,0.1,0);
		for(double t = 0;t<dr.getObszar();t+=0.25) {
			for(double theta = 0; theta <= 2*Math.PI; theta = theta + (Math.PI/32)) {
				double x = t*Math.sin(theta);
				double z = t*Math.cos(theta);
				loc.add(x, 0, z);
				p.getWorld().spawnParticle(Particle.HEART, loc, 1, 0, 0, 0, 0);
				loc.subtract(x, 0, z);
			}
		}
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		
		entities.parallelStream().filter(e -> {
			return e instanceof Player;
		}).forEach(e -> {
			Player pl = (Player) e;
			RpgPlayer rpg2 = PlayerManager.getInstance().getRpgPlayer(pl);
			double amount = pl.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*0.1*rpg.getStats().getKrag();
			
			if(pl.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() > (amount+pl.getHealth())) {
				pl.sendMessage("§7[§6EpicRPG§7] §6Uleczyles sie w "+(10*rpg.getStats().getKrag())+"%");
			}else {
				pl.sendMessage("§7[§6EpicRPG§7] §6Uleczyles sie w pelni");
			}
			
			RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg2, amount);
			Bukkit.getPluginManager().callEvent(event);
			
			new BukkitRunnable() {
				double t = 0;
				Location loc = p.getLocation();
				@Override
				public void run() {
					t+=0.2;
					for(double theta = 0;theta <= 2*Math.PI; theta=theta+(Math.PI/8)) {
						double x = t*Math.sin(theta);
						double y = t;
						double z = t*Math.cos(theta);
						loc.add(x, y, z);
						p.getWorld().spawnParticle(Particle.HEART, loc, 1, 0, 0, 0, 0);
						loc.subtract(x,y,z);
					}
					if(t>=1) {
						this.cancel();
						return;
					}
				}
			}.runTaskTimer(Main.getInstance(), 0, 1);
		});
	}

}
