package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.api.party.PartyAPI;
import de.simonsator.partyandfriends.api.party.PlayerParty;
import de.simonsator.partyandfriends.clan.api.Clan;
import de.simonsator.partyandfriends.clan.api.ClansManager;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;

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
			RpgPlayer rpg = Main.getListaRPG().get(p.getUniqueId().toString());
			effected.add(p);
			p.addPotionEffect(effect);
			p.sendMessage("§7[§6EpicRPG§7] §cKrew Ciebie zalewa - Twoi przeciwnicy zaczynaja drzec ze strachu...");
			rpg.setZadzaKrwi(true);
			
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
						rpg.setZadzaKrwi(false);
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
		
		OnlinePAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(p);
		PlayerParty party = PartyAPI.getParty(pafPlayer);
		Clan klan = ClansManager.getInstance().getClan(pafPlayer);
//		List<OnlinePAFPlayer> list1 = party.getAllPlayers();
//		List<OnlinePAFPlayer> list2 = klan.getAllOnlineClanPlayers();
		List<OnlinePAFPlayer> list1 = new LinkedList<>();
		List<OnlinePAFPlayer> list2 = new LinkedList<>();
		if(party != null) list1 = party.getAllPlayers();
		if(klan != null) list2 = klan.getAllOnlineClanPlayers();
		
		Collection<Entity> entities = p.getWorld().getNearbyEntities(p.getLocation(), dr.getObszar(), dr.getObszar(), dr.getObszar());
		for(Entity e : entities) {
			if(!(e instanceof Player))
				continue;
			Player tmp = (Player) e;
			if(effected.contains(tmp))
				continue;
			
			OnlinePAFPlayer tmpPAF = PAFPlayerManager.getInstance().getPlayer(tmp);
			if(!(list1.contains(tmpPAF) || list2.contains(tmpPAF)))
				continue;
			
			RpgPlayer rpg = Main.getListaRPG().get(tmp.getUniqueId().toString());
			tmp.playSound(tmp.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 5, 0.1f);
			effected.add(tmp);
			tmp.addPotionEffect(effect);
			tmp.sendMessage("§7[§6EpicRPG§7] §cKrew Ciebie zalewa - Twoi przeciwnicy zaczynaja drzec ze strachu...");
			rpg.setZadzaKrwi(true);
			new BukkitRunnable() {
				int timer = dr.getDurationTime()*4;
				DustOptions dust = new DustOptions(Color.RED, 1);
				@Override
				public void run() {
					if(timer <= 0 || !entityInCastWorld(tmp)) {
						tmp.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
						tmp.playSound(tmp.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 5, 1f);
						rpg.setZadzaKrwi(false);
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
			
		}
		
	}
	
}
