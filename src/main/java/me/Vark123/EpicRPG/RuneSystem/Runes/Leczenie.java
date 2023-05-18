package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
//import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class Leczenie extends ARune{

	public Leczenie(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		double amount = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*0.1*rpg.getStats().getKrag();
		if(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() > (amount+p.getHealth())) {
//			p.setHealth((int)(p.getHealth()+p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*0.1*rpg.getKrag()));
			p.sendMessage("§7[§6EpicRPG§7] §6Uleczyles sie w "+(10*rpg.getStats().getKrag())+"%");
		}else {
//			p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			p.sendMessage("§7[§6EpicRPG§7] §6Uleczyles sie w pelni");
		}
		
		RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, amount);
		Bukkit.getPluginManager().callEvent(event);
		
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation();
//			Vector vec = loc.getDirection().normalize();
			@Override
			public void run() {
				t+=0.1;
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
		
	}

}
