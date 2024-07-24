package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class Wtopienie_H extends ARune {

	public Wtopienie_H(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.playSound(p.getLocation(), Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 2, 0.8f);
		Location loc = p.getLocation().add(0, 0.1, 0);
		
		int radius = dr.getObszar();
		List<RpgPlayer> affected = loc.getWorld().getNearbyEntities(loc, radius, radius, radius)
				.stream()
				.filter(e -> e.getLocation().distanceSquared(loc) <= radius*radius)
				.filter(e -> e instanceof Player)
				.map(e -> (Player) e)
				.map(PlayerManager.getInstance()::getRpgPlayer)
				.filter(rpg -> !rpg.getModifiers().hasWtopienie())
				.collect(Collectors.toList());
		affected.forEach(this::playerEffect);
	}
	
	private void playerEffect(RpgPlayer rpg) {
		Player p = rpg.getPlayer();
		rpg.getModifiers().setWtopienie_h(true);
		p.playSound(p.getLocation(), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 2, .65f);
		p.getWorld().spawnParticle(Particle.FLASH, p.getLocation().add(0, .5, 0), 1);
		
		new BukkitRunnable() {
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar("§2§lWtopienie I§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
				bar.setVisible(true);
				bar.addPlayer(p);
				bar.setProgress(timer/time);
			}
			@Override
			public void run() {
				if(timer <= 0) {
					bar.removeAll();
					bar.setVisible(false);
					this.cancel();
					return;
				}
				bar.setTitle("§2§lWtopienie I§f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			@Override
			public void run() {
				if(timer <= 0) {
					p.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
					p.playSound(p.getLocation(), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 2, 1.25f);
					rpg.getModifiers().setWtopienie_h(false);
					this.cancel();
					return;
				}
				
				Location loc = p.getLocation().clone().add(0,1,0);
				Material m =Material.DIRT;
				Block block = p.getLocation().clone().subtract(0,1,0).getBlock();
				if(block != null && !block.getType().equals(Material.AIR))
					m = block.getType();
				
				BlockData data = Bukkit.createBlockData(m);
				p.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 12, 0.5f, 1, 0.5f, 0.15f, data);
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}

}
