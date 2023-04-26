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

public class TarczaCienia extends ARune {

	public TarczaCienia(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1, 0.8f);
		RpgPlayer rpg = Main.getListaRPG().get(p.getUniqueId().toString());
		rpg.setTarczaCienia(true);
		
		new BukkitRunnable() {
			
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar("�8�lTarcza cienia�f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
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
				
				bar.setTitle("�8�lTarcza cienia�f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			int licznik = 0;
//			Vector vec = loc.getDirection().normalize();
			@Override
			public void run() {
				Location loc = p.getLocation();
				licznik++;
				loc.add(0, 0.6, 0);
				p.getWorld().spawnParticle(Particle.SQUID_INK, loc, 2, 0.4F, 0.4F, 0.4F, 0);
				loc.subtract(0, 1.5, 0);
				if(licznik>=20*dr.getDurationTime() || !casterInCastWorld()) {
					p.sendMessage("�7[�6EpicRPG�7] �aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1.7f);
					rpg.setTarczaCienia(false);
					this.cancel();
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
