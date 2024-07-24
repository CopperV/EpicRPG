package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Random;

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
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class PaktKrwi_H extends ARune {

	private static final double red = 138./255.;
	private static final double green = 3./255.;
	private static final double blue = 3./255.;
	private static final DustOptions dust = new DustOptions(Color.fromRGB(128, 3, 3), 1.2f);
	private static final Random rand = new Random();
	
	private static final double HP_TO_TAKE = 200.;

	public PaktKrwi_H(ItemStackRune dr, Player p) {
		super(dr, p);
		this.modifier1 = true;
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1, 0.75f);
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		modifiers.setPaktKrwi_h(true);
		modifiers.setPaktKrwiMeasure_h(true);
		modifiers.setPaktKrwiHp_h(0);
		modifiers.setModifier1_lock(true);
		p.sendMessage("§7[§6EpicRPG§7] §aUzyles runy "+dr.getName());
		
		new BukkitRunnable() {
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar infoBar = Bukkit.createBossBar(dr.getName()+"§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
				infoBar.setVisible(true);
				infoBar.addPlayer(p);
				infoBar.setProgress(timer/time);
			}
			BossBar hpBar = Bukkit.createBossBar("§4KRWAWA ZAPLATA", BarColor.RED, BarStyle.SOLID);{
				hpBar.setVisible(true);
				hpBar.addPlayer(p);
				hpBar.setProgress(0);
			}
			@Override
			public void run() {
				if(isCancelled() || !casterInCastWorld()) {
					infoBar.removeAll();
					infoBar.setVisible(false);
					hpBar.removeAll();
					hpBar.setVisible(false);
				}
				if(timer <= 0) {
					infoBar.removeAll();
					infoBar.setVisible(false);
					hpBar.removeAll();
					hpBar.setVisible(false);
					p.sendMessage("§7[§6EpicRPG§7] §c§lTWOJA DUSZA NALEZY DO MNIE!");
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 0.6f);
					modifiers.setPaktKrwi_h(false);
					modifiers.setPaktKrwiMeasure_h(false);
					modifiers.setPaktKrwiHp_h(0);
					modifiers.setModifier1_lock(false);
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
						p.damage(1);
						p.setHealth(0);
					}, 10);
					
					cancel();
					return;
				}
				if(modifiers.getPaktKrwiHp() > HP_TO_TAKE) {
					infoBar.removeAll();
					infoBar.setVisible(false);
					hpBar.removeAll();
					hpBar.setVisible(false);
					modifier1 = false;
					modifiers.setModifier1_lock(false);
					hpEffect();
					cancel();
					return;
				}
				
				infoBar.setTitle(dr.getName()+"§f: "+(int)timer+" sekund");
				infoBar.setProgress(timer/time);
				
				double hpPercent = modifiers.getPaktKrwiHp() / HP_TO_TAKE;
				if(hpPercent > 1) hpPercent = 1;
				if(hpPercent < 0) hpPercent = 0;
				
				hpBar.setProgress(hpPercent);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(!modifiers.hasPaktKrwi()) {
					this.cancel();
					return;
				}
				
				Location loc = p.getLocation().clone().add(0,1,0);
				if(modifiers.hasPaktKrwiMeasure_h()) {
					for(int i = 0; i < 6; ++i) {
						double x = rand.nextDouble(1.2) - 0.6;
						double y = rand.nextDouble(1.2) - 0.6;
						double z = rand.nextDouble(1.2) - 0.6;
						Location tmp = loc.clone().add(x,y,z);
						p.getWorld().spawnParticle(Particle.SPELL_MOB, tmp, 0, red, green, blue, 1);
					}
					p.getWorld().spawnParticle(Particle.REDSTONE, loc, 6, .6f, .6f, .6f, .1f, dust);
				} else {
					p.getWorld().spawnParticle(Particle.REDSTONE, loc, 6, .6f, .6f, .6f, .1f, dust);
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}
	
	private void hpEffect() {
		p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 1, 0.6f);
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		modifiers.setPaktKrwi_h(true);
		modifiers.setPaktKrwiMeasure_h(false);
		modifiers.setPaktKrwiHp_h(0);

		p.sendMessage("§7[§6EpicRPG§7] §c§lPAKT DOKONANY");
		
		new BukkitRunnable() {
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar(dr.getName()+"§f: "+(int)timer+" sekund", BarColor.RED, BarStyle.SEGMENTED_12);{
				bar.setVisible(true);
				bar.addPlayer(p);
				bar.setProgress(timer/time);
			}
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					bar.removeAll();
					bar.setVisible(false);
					modifiers.setPaktKrwi_h(false);
					modifiers.setPaktKrwiMeasure_h(false);
					modifiers.setPaktKrwiHp_h(0);
					p.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1.2f);
					this.cancel();
					return;
				}
				
				bar.setTitle(dr.getName()+"§f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}

}
