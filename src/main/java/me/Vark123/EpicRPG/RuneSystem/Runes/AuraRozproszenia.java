package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;

public class AuraRozproszenia extends ARune {

	public AuraRozproszenia(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		List<Player> effected = new LinkedList<>();
		effected.add(p);
		for(Entity e : p.getWorld().getNearbyEntities(p.getLocation(), dr.getObszar(), dr.getObszar(), dr.getObszar())) {
			if(!(e instanceof Player))
				continue;
			if(e.equals(p))
				continue;
			Player tmp = (Player) e;
			RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
			ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
			if(set.queryValue(null, Flags.PVP).equals(StateFlag.State.ALLOW))
				continue;
			effected.add(tmp);
			RpgPlayer rpg = Main.getListaRPG().get(tmp.getUniqueId().toString());
			rpg.setAuraRozproszenia(true);
		}
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1.5f, .2f);
		BossBar bar = Bukkit.createBossBar("§c§oAura rozproszenia§f: "+dr.getDurationTime()+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);
		bar.setProgress(1);
		bar.setVisible(true);
		for(Player tmp : effected)
			bar.addPlayer(tmp);
		
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
				
				bar.setTitle("§c§oAura rozproszenia§f: "+(int)timer+" sekund");
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
					for(Player tmp : effected) {
						if(!tmp.isOnline())
							continue;
						tmp.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
						RpgPlayer rpg = Main.getListaRPG().get(tmp.getUniqueId().toString());
						rpg.setAuraRozproszenia(false);
					}
					this.cancel();
					return;
				}

				for(double theta = 0; theta <= (Math.PI*2); theta += (Math.PI/32)) {
					for(Player tmp : effected) {
						if(!tmp.isOnline())
							continue;
						Location loc = tmp.getLocation().add(0,1,0);
						double x1 = r*Math.sin(theta);
						double z1 = r*Math.cos(theta);
						double y = 1*Math.sin(theta*4);
						p.getWorld().spawnParticle(Particle.FLAME, loc.clone().add(x1, 1, z1), 1, 0, 0, 0, 0);
						p.getWorld().spawnParticle(Particle.FLAME, loc.clone().add(x1, -1, z1), 1, 0, 0, 0, 0);
						p.getWorld().spawnParticle(Particle.FLAME, loc.clone().add(x1, y, z1), 1, 0, 0, 0, 0);
						p.getWorld().spawnParticle(Particle.FLAME, loc.clone().add(x1, -y, z1), 1, 0, 0, 0, 0);
					}
				}
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
		
	}

}
