package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.Collection;
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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class RytualKrwi extends ARune {

	public RytualKrwi(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 0.8f);
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		rpg.getModifiers().setRytualKrwi(true);
		p.sendMessage("§7[§6EpicRPG§7] §aUzyles runy "+dr.getName());
		
		new BukkitRunnable() {
			
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar("§d§a§0§3§0§3§lRytual krwi§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
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
				
				bar.setTitle("§d§a§0§3§0§3§lRytual krwi§f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					Location loc = p.getLocation().add(0,1,0);
					DustOptions dust = new DustOptions(Color.fromRGB(128, 0, 0), 2);
					p.getWorld().spawnParticle(Particle.REDSTONE, loc, 15, 0.5f, 0.5f, 0.5f, 0.2f, dust);
					p.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1, 1.7f);
					rpg.getModifiers().setRytualKrwi(false);
					this.cancel();
					return;
				}
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);

	}
	
	public void castEffect() {
		Location loc = p.getLocation();
		loc.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1.4F);
		List<Vector> directions = new ArrayList<>();
		
		double r = 2;
		double x,y,z,theta;
		for(int i = 0; i < 10; ++i) {
			theta = Math.random()*Math.PI*2;
			x = r * Math.sin(theta);
			y = Math.random()*2.5+0.1;
			z = r * Math.cos(theta);
			directions.add(new Vector(x, y, z));
		}
		for(Vector v : directions) {
			Vector tmp = v.normalize().multiply(3);
			loc.getWorld().spawnParticle(Particle.SPELL_MOB, loc.clone().add(tmp), 0,
					218./255., 3./255., 3./255., 1);
		}
		
		Collection<Entity> tmpList = loc.getWorld().getNearbyEntities(loc,dr.getObszar(), dr.getObszar(), dr.getObszar());
		
		tmpList.stream().filter(e -> {
			if(e.equals(p) || !(e instanceof LivingEntity))
				return false;
			if(e instanceof Player) {
				RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
				ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
				State flag = set.queryValue(null, Flags.PVP);
				if(flag != null && flag.equals(State.ALLOW)
						&& !e.getWorld().getName().toLowerCase().contains("dungeon"))
					return true;
				return false;
			}
			if(!MythicBukkit.inst().getMobManager().isMythicMob(e)
					&& e.getType().equals(EntityType.ARMOR_STAND))
				return false;
			if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
				return false;
			return true;
		}).forEach(e -> {
			RuneDamage.damageNormal(p, (LivingEntity) e, dr);
		});
		
	}

}
