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
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class SilaRownowagi_M extends ARune {

	public SilaRownowagi_M(ItemStackRune dr, Player p) {
		super(dr, p);
		this.modifier1 = true;
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 2, 0.8f);
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		modifiers.setSilaRownowagi_m(true);
		modifiers.setModifier1_lock(true);
		p.sendMessage("§7[§6EpicRPG§7] §aUzyles runy "+dr.getName());
		
		new BukkitRunnable() {
			
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar("§f§lSila rownowagi II§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
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
				
				bar.setTitle("§f§lSila rownowagi II§f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					p.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 0.65f);
					modifiers.setSilaRownowagi_m(false);
					modifiers.setModifier1_lock(false);
					this.cancel();
					return;
				}
				
				Location loc = p.getLocation().add(0,1,0);
				p.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc, 6, 0.5f, 0.5f, 0.5f, 0.2f);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}

}
