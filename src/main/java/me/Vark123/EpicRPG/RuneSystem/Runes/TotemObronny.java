package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class TotemObronny extends ARune {
	
	List<Player> defense;

	public TotemObronny(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		defense = new ArrayList<>();
		Location loc = p.getLocation().clone().add(0,0.2d,0);
		loc.getWorld().playSound(loc, Sound.ITEM_SHIELD_BLOCK, 1, 0.3f);
		loc.getWorld().playSound(loc, Sound.ITEM_SHIELD_BLOCK, 1, 0.6f);
		loc.getWorld().playSound(loc, Sound.ITEM_SHIELD_BLOCK, 1, 0.9f);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					defense.parallelStream().filter(tmp -> {
						return tmp.isOnline();
					}).forEach(tmp -> {
						RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(tmp);
						if(rpg.getModifiers().hasTotemObronny()) 
							rpg.getModifiers().setTotemObronny(false);
					});
					defense.clear();
					this.cancel();
					return;
				}

				List<Player> tmp = new ArrayList<>(defense);
				tmp.parallelStream().filter(temp -> {
					if(!temp.isOnline())
						return false;
					if(temp.getLocation().distance(loc) <= dr.getObszar())
						return false;
					return true;
				}).forEach(temp -> {
					defense.remove(temp);
					RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(temp);
					if(rpg.getModifiers().hasTotemObronny()) 
						rpg.getModifiers().setTotemObronny(false);
				});
				
				Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar());
				entities.parallelStream().filter(e -> {
					if(!(e instanceof Player))
						return false;
					Player temp = (Player) e;
					if(defense.contains(temp))
						return false;
					if(!temp.equals(p)) {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
						State flag = set.queryValue(null, Flags.PVP);
						if(flag != null && flag.equals(State.ALLOW)
								&& !e.getWorld().getName().toLowerCase().contains("dungeon"))
							return true;
						return false;
					}
					return true;
				}).forEach(e -> {
					Player temp = (Player) e;
					defense.add(temp);
					RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(temp);
					rpg.getModifiers().setTotemObronny(true);
				});
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
								
				for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/(dr.getObszar()*2))) {
					double x = dr.getObszar()*Math.sin(theta);
					double z = dr.getObszar()*Math.cos(theta);
					Location tmp = new Location(loc.getWorld(), loc.getX()+x, loc.getY(), loc.getZ()+z);
					p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, tmp, 2, 0.15F, 0.15F, 0.15F, 0.1F);
				}
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
		
	}

}
