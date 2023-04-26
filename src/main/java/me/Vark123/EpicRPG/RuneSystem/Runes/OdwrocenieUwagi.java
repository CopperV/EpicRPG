package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;

public class OdwrocenieUwagi extends ARune {
	
	private static Map<Entity, OdwrocenieUwagi> globalEffected = new ConcurrentHashMap<>();
	private Map<Entity, Vector> localEffected = new ConcurrentHashMap<>();
	private Map<Entity, Boolean> hadAIs = new ConcurrentHashMap<>();
	private OdwrocenieUwagi inst;

	public OdwrocenieUwagi(ItemStackRune dr, Player p) {
		super(dr, p);
		inst = this;
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_MOOSHROOM_CONVERT, 1, 1.25f);
		p.getWorld().spawnParticle(Particle.SMOKE_LARGE, p.getLocation().clone().add(0,1,0), 25, 1, 1, 1, 0.2f);
		
		Location loc = p.getLocation();
		PotionEffect potion = new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 9);
		
		Collection<Entity> targetList = p.getWorld().getNearbyEntities(p.getLocation(), dr.getObszar(), dr.getObszar(), dr.getObszar());
		for(Entity e : targetList) {
			if(!(e instanceof LivingEntity))
				continue;
			if(e instanceof Player)
				continue;
			if(e.getLocation().distance(loc) > dr.getObszar())
				continue;
			
			if(globalEffected.containsKey(e))
				globalEffected.get(e).getLocalEffected().remove(e);
			
			Vector vec = e.getLocation().toVector().subtract(p.getLocation().toVector());
			Location eLoc = e.getLocation().setDirection(vec);
//			Bukkit.broadcastMessage("§e"+vec.toString()+"§r : §a"+eLoc.getYaw()+" "+eLoc.getPitch());
			
			((LivingEntity)e).addPotionEffect(potion);
			e.teleport(eLoc);
			
			globalEffected.put(e, this);
			localEffected.put(e, vec);
			
			AbstractEntity ae = BukkitAdapter.adapt(e);
			hadAIs.put(e, ae.hasAI());
			if(ae.hasAI())
				ae.setAI(false);
			
		}
		new BukkitRunnable() {
			int timer = 20*dr.getDurationTime();
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
//				Bukkit.broadcastMessage("Timer: "+timer+" -> "+localEffected.size());
				for(Entity e : localEffected.keySet()) {
					if(e == null || e.isDead())
						continue;
					if(!globalEffected.containsKey(e)) {
						continue;
					}
					if(!globalEffected.get(e).equals(inst)){
						continue;
					}
					
					LivingEntity le = (LivingEntity) e;
					if(!le.hasPotionEffect(PotionEffectType.SLOW)) 
						continue;
//					Bukkit.broadcastMessage("Test6 "+e.getName());
					
					Location tmp = e.getLocation();
					tmp.setDirection(localEffected.get(e));
					
//					MythicBukkit.inst().getVolatileCodeHandler().lookAt(
//							BukkitAdapter.adapt(e), tmp.getYaw(), tmp.getPitch());
					
					MythicBukkit.inst().getVolatileCodeHandler().getEntityHandler().setLocation(
							BukkitAdapter.adapt(e), tmp.getX(), tmp.getY(), tmp.getZ(), 
							tmp.getYaw(), tmp.getPitch());
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for(Entity e : localEffected.keySet()) {
					if(!globalEffected.containsKey(e))
						continue;
					if(!globalEffected.get(e).equals(inst))
						continue;
					globalEffected.remove(e);
				}
				localEffected.clear();
				
				for(Entity e : hadAIs.keySet()) {
					if(!hadAIs.get(e))
						continue;
					BukkitAdapter.adapt(e).setAI(true);
				}
				hadAIs.clear();
			}
		}.runTaskLaterAsynchronously(Main.getInstance(), 20*dr.getDurationTime());
	}

	public static Map<Entity, OdwrocenieUwagi> getGlobalEffected() {
		return globalEffected;
	}

	public Map<Entity, Vector> getLocalEffected() {
		return localEffected;
	}

}
