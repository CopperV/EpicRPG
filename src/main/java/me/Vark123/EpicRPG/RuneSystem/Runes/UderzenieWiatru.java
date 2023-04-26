package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.RuneDamage;
import net.minecraft.world.phys.AxisAlignedBB;

public class UderzenieWiatru extends ARune {

	private List<Entity> shooted;
	
	public UderzenieWiatru(ItemStackRune dr, Player p) {
		super(dr, p);
		shooted = new ArrayList<Entity>();
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation();
			Vector vec = loc.getDirection().normalize();
			public void run() {
				t+=0.75;
				double x = vec.getX()*t;
				double y = vec.getY()*t+1.5;
				double z = vec.getZ()*t;
				loc.add(x,y,z);
				p.getWorld().spawnParticle(Particle.CLOUD, loc,1,0,0,0,0);
				p.getWorld().spawnParticle(Particle.CLOUD, loc,10,0.33F,0.33F,0.33F,0.15F);
				if(odrzutaZakoncz(loc, vec)) {
					this.cancel();
					return;
				}
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
					this.cancel();
					return;
				}
				loc.subtract(x, y, z);
				if(t>40 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				t+=0.75;
				x = vec.getX()*t;
				y = vec.getY()*t+1.5;
				z = vec.getZ()*t;
				loc.add(x,y,z);
				p.getWorld().spawnParticle(Particle.CLOUD, loc,1,0,0,0,0);
				p.getWorld().spawnParticle(Particle.CLOUD, loc,10,0.33F,0.33F,0.33F,0.15F);
//				loc.getWorld().playSound(loc, Sound.FIRE_IGNITE, 1, 1);
				if(odrzutaZakoncz(loc, vec)) {
					this.cancel();
					return;
				}
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
					this.cancel();
					return;
				}
				loc.subtract(x, y, z);
				if(t>40 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				t+=0.75;
				x = vec.getX()*t;
				y = vec.getY()*t+1.5;
				z = vec.getZ()*t;
				loc.add(x,y,z);
				p.getWorld().spawnParticle(Particle.CLOUD, loc,1,0,0,0,0);
				p.getWorld().spawnParticle(Particle.CLOUD, loc,10,0.33F,0.33F,0.33F,0.15F);
//				loc.getWorld().playSound(loc, Sound.FIRE_IGNITE, 1, 1);
				if(odrzutaZakoncz(loc, vec)) {
					this.cancel();
					return;
				}
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
					this.cancel();
					return;
				}
				loc.subtract(x, y, z);
				if(t>40 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				t+=0.75;
				x = vec.getX()*t;
				y = vec.getY()*t+1.5;
				z = vec.getZ()*t;
				loc.add(x,y,z);
				p.getWorld().spawnParticle(Particle.CLOUD, loc,1,0,0,0,0);
				p.getWorld().spawnParticle(Particle.CLOUD, loc,10,0.33F,0.33F,0.33F,0.15F);
//				loc.getWorld().playSound(loc, Sound.FIRE_IGNITE, 1, 1);
				if(odrzutaZakoncz(loc, vec)) {
					this.cancel();
					return;
				}
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
					this.cancel();
					return;
				}
				loc.subtract(x, y, z);
				if(t>40 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

	private boolean odrzutaZakoncz(Location loc, Vector vec) {
		boolean toReturn = false;
		
		for(Entity e:loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
			AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
			AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-2.25, loc.getY()-2.25, loc.getZ()-2.25, loc.getX()+2.25, loc.getY()+2.25, loc.getZ()+2.25);
			if(aabb.c(aabb2)) {
				if(!e.equals(p) && e instanceof LivingEntity && !shooted.contains(e)) {
					shooted.add(e);
					if(e instanceof Player || e.hasMetadata("NPC")) {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
						if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
							continue;
					}
					RuneDamage.damageNormal(p, (LivingEntity)e, dr);
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
					if(!(e instanceof ArmorStand 
							|| (e instanceof AbstractHorse 
									&& ((AbstractHorse)e).getOwner() instanceof Player)))
						e.setVelocity(new Vector(vec.getX()*3,2,vec.getZ()*3));
					toReturn=true;
				}
			}
		}
		
//		for(Entity e:loc.getChunk().getEntities()) {
//			if(e.getLocation().distance(loc) < 2.25 || e.getLocation().add(0, 1, 0).distance(loc) < 2.25 || e.getLocation().add(0, 1, 0).distance(loc) < 2.25) {
//				if(!e.equals(p) && e instanceof LivingEntity && !shooted.contains(e)) {
//					shooted.add(e);
//					if(e instanceof Player || e.hasMetadata("NPC")) {
//						ApplicableRegionSet set = WorldGuardPlugin.inst().getRegionManager(e.getWorld()).getApplicableRegions(e.getLocation());
//						if(set.queryState(null, DefaultFlag.PVP) == State.DENY) 
//							continue;
//					}
//					((LivingEntity)e).setLastDamageCause(new EntityDamageEvent(p, DamageCause.CUSTOM, dr.getDamage()));
//					((LivingEntity)e).damage(dr.getDamage(), p);
//					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 1, 1);
//					e.setVelocity(new Vector(vec.getX()*3,1,vec.getZ()*3));
//					toReturn = true;
//				}
//			}
//		}
		return toReturn;
	}
	
}
