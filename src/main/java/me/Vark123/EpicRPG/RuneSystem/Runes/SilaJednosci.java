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
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;

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
					for(Player p : localEffected) {
						if(!p.isOnline()) 
							continue;
						
						RpgPlayer rpg = Main.getListaRPG().get(p.getUniqueId().toString());
						if(rpg.hasSilaJednosci()) rpg.setSilaJednosci(false);
						
						if(!globalEffected.containsKey(p) || !globalEffected.get(p).equals(inst))
							continue;
						globalEffected.remove(p);
					}
					localEffected.clear();
					
					loc.getWorld().playSound(loc, Sound.ENTITY_ITEM_BREAK, 1, 1);
					
					this.cancel();
					return;
				}
				
				List<Player> tmp = new LinkedList<>(localEffected);
				for(Player tmpP : tmp) {
					if(!tmpP.isOnline())
						continue;
					
					if(!globalEffected.containsKey(tmpP) || !globalEffected.get(tmpP).equals(inst)) {
						localEffected.remove(tmpP);
//						RpgPlayer rpg = Main.getListaRPG().get(p.getUniqueId().toString());
//						if(rpg.hasSilaJednosci()) rpg.setSilaJednosci(false);
						continue;
					}
					
					if(tmpP.getLocation().distance(loc) <= dr.getObszar())
						continue;
					
					localEffected.remove(tmpP);
					RpgPlayer rpg = Main.getListaRPG().get(p.getUniqueId().toString());
					if(rpg.hasSilaJednosci()) rpg.setSilaJednosci(false);
				}
				
				Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar());
				for(Entity e : entities) {
					if(!(e instanceof Player))
						continue;
					
					Player tmpP = (Player) e;
					if(tmpP.equals(p))
						continue;
					
					if(localEffected.contains(p)) {
						if(!globalEffected.containsKey(tmpP) || !globalEffected.get(tmpP).equals(inst)) {
							localEffected.remove(tmpP);
						}
						continue;
					}
					
					localEffected.add(tmpP);
					globalEffected.put(tmpP, inst);
					RpgPlayer rpg = Main.getListaRPG().get(tmpP.getUniqueId().toString());
					rpg.setSilaJednosci(true);
					
				}
				
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
