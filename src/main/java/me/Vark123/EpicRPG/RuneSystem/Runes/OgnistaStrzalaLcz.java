package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
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

public class OgnistaStrzalaLcz extends ARune {

	public OgnistaStrzalaLcz(ItemStackRune dr, Player p) {
		super(dr, p);
		this.modifier1 = true;
	}

	@Override
	public void castSpell() {
		RpgPlayer rpg = Main.getListaRPG().get(p.getUniqueId().toString());
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
		rpg.setOgnistaStrzala(true);
		rpg.setModifier1_lock(true);
		p.sendMessage("§7[§6EpicRPG§7] §aUzyles runy "+dr.getName());
		
		new BukkitRunnable() {
			
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar("§a§lOgnista strzala§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
				bar.setVisible(true);
				bar.addPlayer(p);
				bar.setProgress(timer/time);
			}
			
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					bar.removeAll();
					bar.setVisible(false);
					p.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
					rpg.setOgnistaStrzala(false);
					rpg.setModifier1_lock(false);
					Location loc = p.getLocation();
					for(double y = 0.1;y<=2.2;y+=0.1) {
						for(double t = 0;t<=2*Math.PI;t+=(Math.PI/16)) {
							double x = Math.sin(t);
							double z = Math.cos(t);
							loc.add(x,y,z);
							p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 1, 0, 0,0,0);
							loc.subtract(x, y, z);
						}
					}
					this.cancel();
					return;
				}
				
				bar.setTitle("§a§lOgnista strzala§f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
//		new BukkitRunnable(){
//
//			@Override
//			public void run() {
//				p.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
//				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
//				rpg.setOgnistaStrzala(false);
//				rpg.setModifier1_lock(false);
//				Location loc = p.getLocation();
//				for(double y = 0.1;y<=2.2;y+=0.1) {
//					for(double t = 0;t<=2*Math.PI;t+=(Math.PI/16)) {
//						double x = Math.sin(t);
//						double z = Math.cos(t);
//						loc.add(x,y,z);
//						p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 1, 0, 0,0,0);
//						loc.subtract(x, y, z);
//					}
//				}
//			}
//			
//		}.runTaskLater(Main.getInstance(), 20*dr.getDurationTime());
	}

}
