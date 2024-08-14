package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.RuneManager;

public class BlogoslawienstwoPrzedwiecznych extends ARune {

	private static final Random rand = new Random();
	@Getter
	private static Map<Player, Integer> effected = new LinkedHashMap<>();
	private static Map<Player, BukkitTask> tasks = new LinkedHashMap<>();
	
	private static Map<Player, Map<Player, Integer>> localEffected = new LinkedHashMap<>();

	public BlogoslawienstwoPrzedwiecznych(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		if(rand.nextDouble() < modifiers.getBlogoslawienstwoPrzedwiecznychRenewChance()) {
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 0.6f, 0.8f);
			modifiers.setBlogoslawienstwoPrzedwiecznychRenewChance(modifiers.getBlogoslawienstwoPrzedwiecznychRenewChance()*0.9);
			p.sendMessage("§7[§6EpicRPG§7] §aPrzedwieczni obdarzyli Ciebie swoim blogoslawienstwem!");
			RuneManager.getInstance().getPlayerRuneCd().get(p).remove(dr.getName());
		} else {
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 0.6f, 0.8f);
			modifiers.setBlogoslawienstwoPrzedwiecznychRenewChance(RpgModifiers.getBlogoslawienstwoPrzedwiecznychBaseRenewPercent());
			p.sendMessage("§7[§6EpicRPG§7] §aPrzedwieczni odwrocili sie od Ciebie!");
			Location loc = p.getLocation().add(0,3,0);
			for(int i = 0; i < 15; ++i) {
				double x = rand.nextDouble(1.5) - 0.75;
				double z = rand.nextDouble(1.5) - 0.75;
				float speed = rand.nextFloat(0.23f) + 0.12f;
				Location tmp = loc.clone().add(x,0,z);
				p.getWorld().spawnParticle(Particle.SMOKE_LARGE, tmp, 0, 0, -1, 0, speed);
			}
		}
		
		Map<Player, Integer> tmpMap = localEffected.getOrDefault(p, new LinkedHashMap<>());
		localEffected.put(p, tmpMap);
		p.getWorld().getNearbyEntities(p.getLocation(), dr.getObszar(), dr.getObszar(), dr.getObszar(), e -> {
			if(!(e instanceof Player))
				return false;
			if(e.getLocation().distanceSquared(p.getLocation()) > dr.getObszar()*dr.getObszar())
				return false;
			return true;
		}).stream()
			.map(e -> (Player) e)
			.forEach(player -> {
				p.sendMessage("§7[§6EpicRPG§7] §aLaska Przedwiecznych wylewa sie na Ciebie i Twoich kompanow!");
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.1f, 0.8f);
				effected.put(player, effected.getOrDefault(player, 0) + 1);
				tmpMap.put(player, tmpMap.getOrDefault(player, 0) + 1);
				Location loc = player.getLocation().add(0,3,0);
				for(int i = 0; i < 15; ++i) {
					double x = rand.nextDouble(1.5) - 0.75;
					double z = rand.nextDouble(1.5) - 0.75;
					float speed = rand.nextFloat(0.18f) + 0.12f;
					Location tmp = loc.clone().add(x,0,z);
					player.getWorld().spawnParticle(Particle.DRAGON_BREATH, tmp, 0, 0, -1, 0, speed);
				}
			});
		
		if(tasks.containsKey(p) && !tasks.get(p).isCancelled())
			tasks.get(p).cancel();
		
		BukkitTask task = new BukkitRunnable() {
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar(dr.getName()+"§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
				bar.setVisible(true);
				bar.addPlayer(p);
				bar.setProgress(timer/time);
			}
			@Override
			public void run() {
				if(isCancelled()) {
					bar.removeAll();
					bar.setVisible(false);
					return;
				}
				if(timer <= 0 || !casterInCastWorld()) {
					bar.removeAll();
					bar.setVisible(false);
					localEffected.remove(p);
					tmpMap.forEach((player, level) -> {
						if(!effected.containsKey(player))
							return;

						player.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" §aod §7"+p.getName()+" §askonczyl sie");
						player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, player.getLocation().clone().add(0,1,0), 15, 0.4f, 1, 0.4f, 0.16f);
						if(level < effected.get(player)) {
							effected.put(player, effected.get(player)-level);
							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_HURT, 0.9f, 0.8f);
						} else {
							effected.remove(player);
							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 0.9f, 0.7f);
						}
					});
					this.cancel();
					return;
				}
				timer -= 0.25;
				
				bar.setTitle(dr.getName()+"§f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				
				tmpMap.keySet().forEach(player -> {
					Location loc = player.getLocation().clone().add(0,1,0);
					player.getWorld().spawnParticle(Particle.DRAGON_BREATH, loc, 4, 0.4f, 1, 0.4f, 0.12f);
				});
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
		tasks.put(p, task);
	}
	
	
	
}
