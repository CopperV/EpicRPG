package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;

public class AuraCzystosci extends ARune {

	public AuraCzystosci(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
//		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		p.getWorld().playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
		double amount = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*0.5;
		p.setAbsorptionAmount(p.getAbsorptionAmount()+amount);
		double startAmount = p.getAbsorptionAmount();
		
		new BukkitRunnable() {
			
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar("§3Aura czystosci§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
				bar.setVisible(true);
				bar.addPlayer(p);
				bar.setProgress(timer/time);
			}
			
			@Override
			public void run() {

//				Bukkit.broadcastMessage("Test2: "+timer+" "+effected.contains(p));
				
				if(timer <= 0 || !casterInCastWorld() || startAmount - p.getAbsorptionAmount() >= amount) {
					bar.removeAll();
					bar.setVisible(false);
					
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 1, 1f);
					p.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, p.getLocation().add(0,1,0), 15, 0.75f, 1f, 0.75f, 0.1f);
					p.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
					
					if(p.getAbsorptionAmount() < amount)
						p.setAbsorptionAmount(0);
					else
						p.setAbsorptionAmount(p.getAbsorptionAmount() - amount);
					
					this.cancel();
					return;
				}
				
				bar.setTitle("§3Aura czystosci§f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld() || startAmount - p.getAbsorptionAmount() >= amount) {
					this.cancel();
					return;
				}
				Location loc = p.getLocation().add(0,1,0);
				p.getWorld().spawnParticle(Particle.END_ROD, loc, 12, 0.75f, 1f, 0.75f, 0.1f);
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}

}
