package me.Vark123.EpicRPG.OldRuneSystem.Runes;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.OldRuneSystem.ARune;
import me.Vark123.EpicRPG.OldRuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class LodowaTarcza extends ARune {

	public LodowaTarcza(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		List<Player> effected = new LinkedList<>();
		
		p.getWorld().getNearbyEntities(p.getLocation(), dr.getObszar(), dr.getObszar(), dr.getObszar()).stream().filter(e -> {
			if(!(e instanceof Player))
				return false;
			if(e.equals(p))
				return true;
			RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
			ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
			State flag = set.queryValue(null, Flags.PVP);
			if(flag != null && flag.equals(State.ALLOW)
					&& !(e.getWorld().getName().toLowerCase().contains("dungeon") || e.getWorld().getName().toLowerCase().contains("raid")))
				return false;
			return true;
		}).forEach(e -> {
			Player tmp = (Player) e;
			effected.add(tmp);
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(tmp);
			rpg.getModifiers().setLodowaTarcza(true);
		});
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 1.5f, .2f);
		BossBar bar = Bukkit.createBossBar(dr.getName()+"§f: "+dr.getDurationTime()+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);
		bar.setProgress(1);
		bar.setVisible(true);
		
		effected.stream().forEach(tmp -> {
			bar.addPlayer(tmp);
		});
		
		new BukkitRunnable() {
			
			double timer = dr.getDurationTime();
			double time = dr.getDurationTime();
			
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					bar.removeAll();
					bar.setVisible(false);
					this.cancel();
					return;
				}
				
				bar.setTitle(dr.getName()+"§f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			double r = 1.5;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					effected.stream().forEach(tmp -> {
						if(!tmp.isOnline())
							return;
						tmp.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
						RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(tmp);
						rpg.getModifiers().setLodowaTarcza(false);
					});
					this.cancel();
					return;
				}

				for(double theta = 0; theta <= (Math.PI*2); theta += (Math.PI/24)) {
					for(Player tmp : effected) {
						if(!tmp.isOnline())
							continue;
						Location loc = tmp.getLocation().add(0,1,0);
						double x1 = r*Math.sin(theta);
						double z1 = r*Math.cos(theta);
						double y = 1.25*Math.sin(theta*4);
						p.getWorld().spawnParticle(Particle.ITEM_SNOWBALL, loc.clone().add(x1, 1, z1), 1, 0, 0, 0, 0);
						p.getWorld().spawnParticle(Particle.ITEM_SNOWBALL, loc.clone().add(x1, -1, z1), 1, 0, 0, 0, 0);
						p.getWorld().spawnParticle(Particle.ITEM_SNOWBALL, loc.clone().add(x1, y, z1), 1, 0, 0, 0, 0);
						p.getWorld().spawnParticle(Particle.ITEM_SNOWBALL, loc.clone().add(x1, -y, z1), 1, 0, 0, 0, 0);
					}
				}
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
		
	}

}
