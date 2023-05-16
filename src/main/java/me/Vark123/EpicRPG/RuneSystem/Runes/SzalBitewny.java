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
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class SzalBitewny extends ARune{

	public SzalBitewny(ItemStackRune dr, Player p) {
		super(dr, p);
		this.modifier1 = true;
	}

	@Override
	public void castSpell() {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
		modifiers.setSzalBitewny(true);
		modifiers.setModifier1_lock(true);
		p.sendMessage("�7[�6EpicRPG�7] �aUzyles runy "+dr.getName());
		
		new BukkitRunnable() {

			DustOptions dust = new DustOptions(Color.RED, 1);
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar("�9�lSzal bitewny�f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
				bar.setVisible(true);
				bar.addPlayer(p);
				bar.setProgress(timer/time);
			}
			
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					bar.removeAll();
					bar.setVisible(false);
					p.sendMessage("�7[�6EpicRPG�7] �aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
					modifiers.setSzalBitewny(false);
					modifiers.setModifier1_lock(false);
					Location loc = p.getLocation();
					for(double y = 0.1;y<=2.2;y+=0.1) {
						for(double t = 0;t<=2*Math.PI;t+=(Math.PI/16)) {
							double x = Math.sin(t);
							double z = Math.cos(t);
							loc.add(x,y,z);
							p.getWorld().spawnParticle(Particle.REDSTONE, loc, 1,0,0,0,0, dust);
							loc.subtract(x, y, z);
						}
					}
					this.cancel();
					return;
				}
				
				bar.setTitle("�9�lSzal bitewny�f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
	}
	
}
