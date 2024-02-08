package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.Runes.Events.AllyRangedRuneUseEvent;

public class ZadzaKrwi extends ARune {

	private static List<Player> effected = new ArrayList<>();

	public ZadzaKrwi(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		PotionEffect effect = new PotionEffect(PotionEffectType.SPEED, dr.getDurationTime()*20, 1);
		
		if(!effected.contains(p)) {
			p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 5, 0.1f);
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
			effected.add(p);
			p.addPotionEffect(effect);
			p.sendMessage("§7[§6EpicRPG§7] §cKrew Ciebie zalewa - Twoi przeciwnicy zaczynaja drzec ze strachu...");
			rpg.getModifiers().setZadzaKrwi(true);
			
			new BukkitRunnable() {
				
				double time = dr.getDurationTime();
				double timer = dr.getDurationTime();
				BossBar bar = Bukkit.createBossBar("§9§lZadza krwi§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
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
					
					bar.setTitle("§9§lZadza krwi§f: "+(int)timer+" sekund");
					bar.setProgress(timer/time);
					
					--timer;
				}
			}.runTaskTimer(Main.getInstance(), 0, 20);
			
			new BukkitRunnable() {
				int timer = dr.getDurationTime()*4;
				DustOptions dust = new DustOptions(Color.RED, 1);
				@Override
				public void run() {
					if(timer <= 0 || !casterInCastWorld()) {
						p.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
						p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 5, 1f);
						rpg.getModifiers().setZadzaKrwi(false);
						this.cancel();
						return;
					}
					
					Location loc = p.getLocation().add(0,1,0);
					p.getWorld().spawnParticle(Particle.REDSTONE, loc, 3, 0.5f, 0.5f, 0.5f, 0.1f,dust);
					
					--timer;
				}
			}.runTaskTimer(Main.getInstance(), 0, 5);
			
			
			
			new BukkitRunnable() {
				DustOptions dust = new DustOptions(Color.YELLOW, 1);
				@Override
				public void run() {
					p.sendMessage("§7[§6EpicRPG§7] §aDebuff runy "+dr.getName()+" skonczyl sie");
					p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 5, 1.5f);
					p.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation(), 10, 0.5f, 0.5f, 0.5f, 0.1f,dust);
					effected.remove(p);
				}
			}.runTaskLater(Main.getInstance(), 60*10*20);
		}
		
		Collection<Player> affected = new HashSet<>();
		affected.addAll(p.getNearbyEntities(dr.getObszar(), dr.getObszar(), dr.getObszar()).stream()
				.filter(e -> {
					if(!(e instanceof Player))
						return false;
					if(e.getLocation().distanceSquared(p.getLocation()) > dr.getObszar() * dr.getObszar())
						return false;
					Player tmp = (Player) e;
					if(tmp.equals(p))
						return false;
					if(effected.contains(tmp))
						return false;
					return true;
				}).map(e -> (Player) e)
				.collect(Collectors.toSet()));
		AllyRangedRuneUseEvent event = new AllyRangedRuneUseEvent(p, this, affected);
		Bukkit.getPluginManager().callEvent(event);
		
		event.getAffectedPlayers().forEach(tmp -> {
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(tmp);
			tmp.playSound(tmp.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 5, 0.1f);
			effected.add(tmp);
			tmp.addPotionEffect(effect);
			tmp.sendMessage("§7[§6EpicRPG§7] §cKrew Ciebie zalewa - Twoi przeciwnicy zaczynaja drzec ze strachu...");
			rpg.getModifiers().setZadzaKrwi(true);
			new BukkitRunnable() {
				int timer = dr.getDurationTime()*4;
				DustOptions dust = new DustOptions(Color.RED, 1);
				@Override
				public void run() {
					if(timer <= 0 || !entityInCastWorld(tmp)) {
						tmp.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
						tmp.playSound(tmp.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 5, 1f);
						rpg.getModifiers().setZadzaKrwi(false);
						this.cancel();
						return;
					}
					
					Location loc = tmp.getLocation().add(0,1,0);
					tmp.getWorld().spawnParticle(Particle.REDSTONE, loc, 3, 0.5f, 0.5f, 0.5f, 0.1f,dust);
					
					--timer;
				}
			}.runTaskTimer(Main.getInstance(), 0, 5);
			new BukkitRunnable() {
				DustOptions dust = new DustOptions(Color.YELLOW, 1);
				@Override
				public void run() {
					tmp.sendMessage("§7[§6EpicRPG§7] §aDebuff runy "+dr.getName()+" skonczyl sie");
					tmp.playSound(tmp.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 5, 1.5f);
					tmp.getWorld().spawnParticle(Particle.REDSTONE, p.getLocation(), 10, 0.5f, 0.5f, 0.5f, 0.1f,dust);
					effected.remove(tmp);
				}
			}.runTaskLater(Main.getInstance(), 60*10*20);
		});
		
	}
	
}
