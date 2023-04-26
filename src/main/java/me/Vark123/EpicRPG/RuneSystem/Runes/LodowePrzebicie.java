package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
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

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.RuneDamage;
import net.minecraft.world.phys.AxisAlignedBB;

public class LodowePrzebicie extends ARune {

	public LodowePrzebicie(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location loc = p.getLocation();
		p.getWorld().playSound(loc, Sound.BLOCK_AMETHYST_CLUSTER_BREAK, 1, 0.8f);
		p.getWorld().spawnParticle(Particle.SNOWFLAKE, loc.clone().add(0,1,0), 15, 0.4,0.4,0.4,0.03);
		
		List<Entity> targets = new LinkedList<>();
		Collection<Entity> tmpList = loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar());
		
		for(Entity e : tmpList) {
			if(e.getLocation().distance(loc) > dr.getObszar())
				continue;
			if(!(e instanceof LivingEntity))
				continue;
			if(e.hasMetadata("NPC")) 
				continue;
			
			if(e.equals(p))
				continue;
			
			if(e instanceof Player) {
				RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
				ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(loc));
				if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
					continue;
				targets.add(e);
				continue;
			}
			
			ActiveMob mob = MythicBukkit.inst().getAPIHelper().getMythicMobInstance(e);
			if(mob == null) 
				continue;
			
			targets.add(e);
		}
		
		for(Entity e : targets)
			spellEffect(e);
	}
	
	private void spellEffect(Entity e) {
		new BukkitRunnable() {
			Location startLoc = e.getLocation().add(0, 25, 0);
			Location endLoc = e.getLocation();
			Vector v = new Vector(0, endLoc.getY() - startLoc.getY(), 0).normalize().multiply(0.1);
			Location loc = startLoc.clone();
			@Override
			public void run() {
//				Bukkit.broadcastMessage("Dystans: "+loc.distance(startLoc)+" = "+loc.getY()+" "+startLoc.getY());
				if(loc.distance(startLoc) > 40 || !casterInCastWorld()) {
					this.cancel();
					return;
				}

				p.getWorld().spawnParticle(Particle.SNOWBALL, loc, 6, 0.04f, 0.04f, 0.04f, 0.02f);
				
				for(Entity e:loc.getWorld().getNearbyEntities(loc, 3, 3, 3)) {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.55, loc.getY()-0.55, loc.getZ()-0.55, loc.getX()+0.55, loc.getY()+0.55, loc.getZ()+0.55);
					if(aabb.c(aabb2)) {
						if(!e.equals(p) && e instanceof LivingEntity) {
							if(e instanceof Player || e.hasMetadata("NPC")) {
								RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
								ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
								if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
									continue;
							}
							e.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_FREEZE, 1, 0.65f);
							RuneDamage.damageNormal(p, (LivingEntity)e, dr);
							this.cancel();
							return;
						}
					}
				}
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
					this.cancel();
					return;
				}
				
				loc.add(v);
				v.multiply(1.05);
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
