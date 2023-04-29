package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

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
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class Transfuzja extends ARune {
	
	private static List<Player> effected = new ArrayList<>();

	public Transfuzja(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		if(effected.contains(p))
			effected.remove(p);
		effected.add(p);
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_MAGMA_CUBE_JUMP, 1, 0.6f);
		rpg.getModifiers().setTransfuzja(true);
		p.sendMessage("§7[§6EpicRPG§7] §aUzyles runy "+dr.getName());
		
		new BukkitRunnable() {
			
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar("§x§e§e§0§5§0§5§lTransfuzja§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
				bar.setVisible(true);
				bar.addPlayer(p);
				bar.setProgress(timer/time);
			}

			DustOptions dust = new DustOptions(Color.fromRGB(238, 5, 5), 2.5f);
			
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld() || !effected.contains(p)) {
					bar.removeAll();
					bar.setVisible(false);
					
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_MAGMA_CUBE_HURT, 1, 0.7f);
					p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation().add(0,1,0), 20, 0.5f, 0.5f, 0.5f, 0.1f, dust);
					rpg.getModifiers().setTransfuzja(false);
					effected.remove(p);
					p.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
					
					this.cancel();
					return;
				}
				
				bar.setTitle("§x§e§e§0§5§0§5§lTransfuzja§f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			
			double _45deg = Math.PI/4.0;
			double r = 0.75;
			double theta = 0;
			
			DustOptions dust = new DustOptions(Color.fromRGB(238, 5, 5), 0.6f);
			
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld() || !effected.contains(p)) {
					this.cancel();
					return;
				}
				
				Location loc1 = p.getLocation().clone().add(0,1,0);
				Location loc2 = p.getLocation().clone().add(0,1,0);
				
				double x = r*Math.cos(theta)*Math.cos(_45deg);
				double z = r*Math.cos(theta)*Math.sin(_45deg);
				double y = r*Math.sin(theta);
				loc1.add(x,y,z);
				p.getWorld().spawnParticle(Particle.REDSTONE, loc1, 1,0,0,0,0,dust);
				
				x = r*Math.cos(theta)*Math.cos(-_45deg);
				z = r*Math.cos(theta)*Math.sin(-_45deg);
				loc2.add(x,y,z);
				p.getWorld().spawnParticle(Particle.REDSTONE, loc1, 1,0,0,0,0,dust);
				
				theta += Math.PI/16.;
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}

	public static List<Player> getEffected() {
		return effected;
	}

}
