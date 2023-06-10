package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

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

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.api.party.PartyAPI;
import de.simonsator.partyandfriends.api.party.PlayerParty;
import de.simonsator.partyandfriends.clan.api.Clan;
import de.simonsator.partyandfriends.clan.api.ClansManager;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;

public class PoteznaRunaDomisia extends ARune {
	
	public PoteznaRunaDomisia(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		PotionEffect effect1 = new PotionEffect(PotionEffectType.SPEED, dr.getDurationTime()*20, 1);
		PotionEffect effect2 = new PotionEffect(PotionEffectType.NIGHT_VISION, dr.getDurationTime()*20, 0);
		PotionEffect effect3 = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, dr.getDurationTime()*20, 0);
		PotionEffect effect4 = new PotionEffect(PotionEffectType.REGENERATION, dr.getDurationTime()*20, 3);
		PotionEffect effect5 = new PotionEffect(PotionEffectType.SLOW_FALLING, dr.getDurationTime()*20, 0);
		PotionEffect effect6 = new PotionEffect(PotionEffectType.CONFUSION, dr.getDurationTime()*20, 0);
		PotionEffect effect7 = new PotionEffect(PotionEffectType.CONDUIT_POWER, dr.getDurationTime()*20, 0);
	
		Random rand = new Random();
		int i1, i2;
		i1 = rand.nextInt(100);
		i2 = rand.nextInt(100);
		boolean conf = i1 == i2;
		
		p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 5, 0.1f);
		p.addPotionEffect(effect1);
		p.addPotionEffect(effect2);
		p.addPotionEffect(effect3);
		p.addPotionEffect(effect4);
		p.addPotionEffect(effect5);
		p.addPotionEffect(effect7);
		if(conf)
			p.addPotionEffect(effect6);
		
		new BukkitRunnable() {
			
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar("§5§lPotezna Runa Domisia§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
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
				
				bar.setTitle("§5§lPotezna Runa Domisia§f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			DustOptions dust = new DustOptions(Color.fromRGB(128, 0, 128), 1);
			@Override
			public void run() {
				if(timer <= 0) {
					p.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
					p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 5, 1f);
					this.cancel();
					return;
				}
				
				Location loc = p.getLocation().add(0,1,0);
				p.getWorld().spawnParticle(Particle.REDSTONE, loc, 3, 0.5f, 0.5f, 0.5f, 0.1f,dust);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
		
		p.getNearbyEntities(dr.getObszar(), dr.getObszar(), dr.getObszar()).stream().filter(e -> {
			if(!(e instanceof Player))
				return false;
			RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
			ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
			State flag = set.queryValue(null, Flags.PVP);
			if(flag != null && flag.equals(State.ALLOW)
					&& !e.getWorld().getName().toLowerCase().contains("dungeon"))
				return false;
			return false;
		}).forEach(e -> {
			Player tmp = (Player) e;
			tmp.playSound(tmp.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 5, 0.1f);
			tmp.addPotionEffect(effect1);
			tmp.addPotionEffect(effect2);
			tmp.addPotionEffect(effect3);
			tmp.addPotionEffect(effect4);
			tmp.addPotionEffect(effect5);
			tmp.addPotionEffect(effect7);
			if(conf)
				tmp.addPotionEffect(effect6);
			
			new BukkitRunnable() {
				
				double time = dr.getDurationTime();
				double timer = dr.getDurationTime();
				BossBar bar = Bukkit.createBossBar("§5§lPotezna Runa Domisia§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
					bar.setVisible(true);
					bar.addPlayer(tmp);
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
					
					bar.setTitle("§5§lPotezna Runa Domisia§f: "+(int)timer+" sekund");
					bar.setProgress(timer/time);
					
					--timer;
				}
			}.runTaskTimer(Main.getInstance(), 0, 20);
			
			new BukkitRunnable() {
				int timer = dr.getDurationTime()*4;
				DustOptions dust = new DustOptions(Color.fromRGB(128, 0, 128), 1);
				@Override
				public void run() {
					if(timer <= 0) {
						tmp.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
						tmp.playSound(tmp.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 5, 1f);
						this.cancel();
						return;
					}
					
					Location loc = tmp.getLocation().add(0,1,0);
					tmp.getWorld().spawnParticle(Particle.REDSTONE, loc, 3, 0.5f, 0.5f, 0.5f, 0.1f,dust);
					
					--timer;
				}
			}.runTaskTimer(Main.getInstance(), 0, 5);
		});
		
		OnlinePAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(p);
		PlayerParty party = PartyAPI.getParty(pafPlayer);
		Clan klan = ClansManager.getInstance().getClan(pafPlayer);
		Set<OnlinePAFPlayer> set = new LinkedHashSet<>();
		
		if(party != null) {
			set.addAll(party.getAllPlayers());
		}
		if(klan != null) {
			set.addAll(klan.getAllOnlineClanPlayers());
		}
		
		set.stream().filter(paf -> {
			if(paf.equals(pafPlayer))
				return false;
			return true;
		}).forEach(paf -> {
			Player tmp = paf.getPlayer();
			tmp.playSound(tmp.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 5, 0.1f);
			tmp.addPotionEffect(effect1);
			tmp.addPotionEffect(effect2);
			tmp.addPotionEffect(effect3);
			tmp.addPotionEffect(effect4);
			tmp.addPotionEffect(effect5);
			tmp.addPotionEffect(effect7);
			if(conf)
				tmp.addPotionEffect(effect6);
			
			new BukkitRunnable() {
				
				double time = dr.getDurationTime();
				double timer = dr.getDurationTime();
				BossBar bar = Bukkit.createBossBar("§5§lPotezna Runa Domisia§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
					bar.setVisible(true);
					bar.addPlayer(tmp);
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
					
					bar.setTitle("§5§lPotezna Runa Domisia§f: "+(int)timer+" sekund");
					bar.setProgress(timer/time);
					
					--timer;
				}
			}.runTaskTimer(Main.getInstance(), 0, 20);
			
			new BukkitRunnable() {
				int timer = dr.getDurationTime()*4;
				DustOptions dust = new DustOptions(Color.fromRGB(128, 0, 128), 1);
				@Override
				public void run() {
					if(timer <= 0) {
						tmp.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
						tmp.playSound(tmp.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 5, 1f);
						this.cancel();
						return;
					}
					
					Location loc = tmp.getLocation().add(0,1,0);
					tmp.getWorld().spawnParticle(Particle.REDSTONE, loc, 3, 0.5f, 0.5f, 0.5f, 0.1f,dust);
					
					--timer;
				}
			}.runTaskTimer(Main.getInstance(), 0, 5);
		});
		
	}

}
