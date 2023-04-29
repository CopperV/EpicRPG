package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class SilaJednosci extends ARune {
	
	private static Map<Player, SilaJednosci> globalEffected = new ConcurrentHashMap<>();
	private List<Player> localEffected = new ArrayList<>();
	private Location loc;
	private SilaJednosci inst;

	public SilaJednosci(ItemStackRune dr, Player p) {
		super(dr, p);
		loc = p.getLocation().add(0,0.1,0);
		inst = this;
	}

	@Override
	public void castSpell() {
		loc.getWorld().playSound(loc, Sound.ENTITY_MOOSHROOM_CONVERT, 1, 0.85f);

		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					
					localEffected.parallelStream().forEach(tmp -> {
						if(!tmp.isOnline())
							return;
						RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(tmp);
						if(rpg.getModifiers().hasSilaJednosci()) 
							rpg.getModifiers().setSilaJednosci(false);
						if(!globalEffected.containsKey(tmp) || !globalEffected.get(tmp).equals(inst))
							return;
						globalEffected.remove(tmp);
					});
					localEffected.clear();
					
					loc.getWorld().playSound(loc, Sound.ENTITY_ITEM_BREAK, 1, 1);
					
					this.cancel();
					return;
				}
				
				List<Player> tmp = new LinkedList<>(localEffected);
				tmp.parallelStream().forEach(temp -> {
					if(!temp.isOnline())
						return;
					if(!globalEffected.containsKey(temp) || !globalEffected.get(temp).equals(inst)) {
						localEffected.remove(temp);
						return;
					}
					if(temp.getLocation().distance(loc) <= dr.getObszar())
						return;
					
					localEffected.remove(temp);
					RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(temp);
					if(rpg.getModifiers().hasSilaJednosci()) 
						rpg.getModifiers().setSilaJednosci(false);
				});
				
				Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar());
				
				entities.parallelStream().filter(e -> {
					if(!(e instanceof Player))
						return false;
					if(e.equals(p))
						return false;
					Player temp = (Player) e;
					if(localEffected.contains(temp)) {
						if(!globalEffected.containsKey(temp) || !globalEffected.get(temp).equals(inst)) {
							localEffected.remove(temp);
						}
						return false;
					}
					return true;
				}).forEach(e -> {
					Player temp = (Player) e;
					localEffected.add(temp);
					globalEffected.put(temp, inst);
					RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(temp);
					rpg.getModifiers().setSilaJednosci(true);
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
					p.getWorld().spawnParticle(Particle.SNEEZE, tmp, 2, 0.15F, 0.15F, 0.15F, 0.05F);
				}
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}

	public Location getLoc() {
		return loc;
	}
	
	public Player getCaster() {
		return p;
	}

	public static Map<Player, SilaJednosci> getGlobalEffected() {
		return globalEffected;
	}
	
	public ItemStackRune getRune() {
		return dr;
	}

}
