package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;

public class Gniew extends ARune {

	public Gniew(ItemStackRune dr, Player p) {
		super(dr, p);
		this.modifier1 = true;
	}

	@Override
	public void castSpell() {
		RpgPlayer rpg = Main.getListaRPG().get(p.getUniqueId().toString());
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 1, 1);
		rpg.setGniew(true);
		rpg.setModifier1_lock(true);
		p.sendMessage("�7[�6EpicRPG�7] �aUzyles runy "+dr.getName());
		
		new BukkitRunnable() {
			
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar("�x�8�a�0�3�0�3�oGniew�f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
				bar.setVisible(true);
				bar.addPlayer(p);
				bar.setProgress(timer/time);
			}
			DustOptions dust = new DustOptions(Color.fromRGB(138, 3, 3), 1);
			
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					bar.removeAll();
					bar.setVisible(false);
					p.sendMessage("�7[�6EpicRPG�7] �aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 1, 0.3f);
					rpg.setGniew(false);
					rpg.setModifier1_lock(false);
					this.cancel();
					return;
				}

				Location loc = p.getLocation().add(0,1,0);
				p.getWorld().spawnParticle(Particle.REDSTONE, loc, 4, 0.5f, 0.5f, 0.5f, 0.1f, dust);
				
				bar.setTitle("�x�8�a�0�3�0�3�oGniew�f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}

}
