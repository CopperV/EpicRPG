package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
//import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class OgnistaSfera extends ARune{

	public OgnistaSfera(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		rpg.getModifiers().setSfera(true);
		new BukkitRunnable() {
			int licznik = 0;
			double theta = 0;
			@Override
			public void run() {
				Location loc = p.getLocation();
				Location loc2 = p.getLocation();
				theta = 0;
				licznik++;
				for(double y = 0;y<= 2.5; y+=0.1) {
					theta = theta+(Math.PI/8);
					double x = Math.cos(theta);
					double z = Math.sin(theta);
					double x2 = Math.sin(theta);
					double z2 = Math.cos(theta);
					loc.add(x, y, z);
					loc2.add(x2, y, z2);
					p.getWorld().spawnParticle(Particle.FLAME, loc, 1, 0, 0, 0,0);
					p.getWorld().spawnParticle(Particle.FLAME, loc2, 1, 0, 0, 0, 0);
					loc2.subtract(x2, y, z2);
					loc.subtract(x,y,z);
				}
				if(licznik>=20*dr.getDurationTime() || !casterInCastWorld()) {
					rpg.getModifiers().setSfera(false);
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
