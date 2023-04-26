package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

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

public class LodowyBlok extends ARune {
	
	private static List<Player> effected = new ArrayList<>();

	public LodowyBlok(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		if(effected.contains(p))
			effected.remove(p);
		effected.add(p);
//		Bukkit.broadcastMessage("Test1 - contains: "+effected.contains(p));
		RpgPlayer rpg = Main.getListaRPG().get(p.getUniqueId().toString());
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1, 1.25f);
		rpg.setLodowyBlok(true);
		p.sendMessage("§7[§6EpicRPG§7] §aUzyles runy "+dr.getName());
		
		new BukkitRunnable() {
			
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar("§b§oLodowy blok§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
				bar.setVisible(true);
				bar.addPlayer(p);
				bar.setProgress(timer/time);
			}
			
			@Override
			public void run() {

//				Bukkit.broadcastMessage("Test2: "+timer+" "+effected.contains(p));
				
				if(timer <= 0 || !casterInCastWorld() || !effected.contains(p)) {
					bar.removeAll();
					bar.setVisible(false);
					
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1, 1f);
					p.getWorld().spawnParticle(Particle.SNOWFLAKE, p.getLocation().add(0,1,0), 30, 0.5f, 1f, 0.5f, 0.1f);
					rpg.setLodowyBlok(false);
					effected.remove(p);
					p.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
					
					this.cancel();
					return;
				}
				
				bar.setTitle("§b§oLodowy blok§f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld() || !effected.contains(p)) {
					this.cancel();
					return;
				}
				Location loc = p.getLocation().add(0,1,0);
				p.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 10, 0.5f, 1f, 0.5f, 0.15f);
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
		
	}

	public static List<Player> getEffected() {
		return effected;
	}

}
