package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;

public class Wampiryzm_M extends ARune {

	public Wampiryzm_M(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_CAT_HISS, 1, .1f);
		RpgPlayer rpg = Main.getListaRPG().get(p.getUniqueId().toString());
		rpg.setWampiryzm_m(true);
		
		new BukkitRunnable() {
			
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar("§9§lWampiryzm II§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
				bar.setVisible(true);
				bar.addPlayer(p);
				bar.setProgress(timer/time);
			}
			
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					bar.removeAll();
					bar.setVisible(false);
					this.cancel();
					return;
				}
				
				bar.setTitle("§9§lWampiryzm II§f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			int licznik = 0;
//			Vector vec = loc.getDirection().normalize();
			DustOptions dust = new DustOptions(Color.fromRGB(224, 32, 32), 1);
			@Override
			public void run() {
				Location loc = p.getLocation();
				licznik++;
				loc.add(0, 1.5, 0);
				p.getWorld().spawnParticle(Particle.REDSTONE, loc, 10, 0.5F, 0.5F, 0.5F, 0.1f, dust);
				loc.subtract(0, 1.5, 0);
				if(licznik>=20*dr.getDurationTime() || !casterInCastWorld()) {
					rpg.setWampiryzm_m(false);
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
