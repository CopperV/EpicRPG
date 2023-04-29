package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class WedrownyCien extends ARune {
	
	private static List<Player> effected = new ArrayList<>();

	public WedrownyCien(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		if(effected.contains(p))
			effected.remove(p);
		effected.add(p);
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 0.8f);
		rpg.getModifiers().setWedrownyCien(true);
		p.sendMessage("§7[§6EpicRPG§7] §aUzyles runy "+dr.getName());
		
		Collection<Entity> targetList = p.getWorld().getNearbyEntities(p.getLocation(), 40, 40, 40);
		targetList.parallelStream().filter(e -> {
			if(!(e instanceof Creature))
				return false;
			Creature creature = (Creature) e;
			if(creature.getTarget() == null)
				return false;
			if(!creature.getTarget().equals(p))
				return false;
			return true;
		}).forEach(e -> {
			Creature creature = (Creature) e;
			creature.setTarget(null);
			ActiveMob mob = MythicBukkit.inst().getMobManager().getMythicMobInstance(e);
			if(mob == null)
				return;
			mob.resetTarget();
		});
		
		new BukkitRunnable() {
			
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar("§aWedrowny cien§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
				bar.setVisible(true);
				bar.addPlayer(p);
				bar.setProgress(timer/time);
			}
			
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld() || !effected.contains(p)) {
					bar.removeAll();
					bar.setVisible(false);
					this.cancel();
					return;
				}
				
				bar.setTitle("§aWedrowny cien§f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld() || !effected.contains(p)) {
					p.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1.5f);
					rpg.getModifiers().setWedrownyCien(false);
					this.cancel();
					return;
				}
				
				Location loc = p.getLocation().add(0,1,0);
				p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 5, 0.5f, 0.75f, 0.5f, 0.1f);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}

	public static List<Player> getEffected() {
		return effected;
	}

}
