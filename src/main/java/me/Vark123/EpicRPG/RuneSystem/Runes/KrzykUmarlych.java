package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractPlayer;
import io.lumine.mythic.api.adapters.AbstractVector;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import net.minecraft.world.phys.AxisAlignedBB;

public class KrzykUmarlych extends ARune {

	private Entity target = null;
	private double dmg = 0;
	
	public KrzykUmarlych(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgStats stats = rpg.getStats();
		if(stats.getPresentMana() < 1)
			return;
		dmg = stats.getPresentMana() * 3;
		stats.removePresentMana(stats.getPresentMana());
		
		AbstractPlayer ap = BukkitAdapter.adapt(p);
		
		p.getWorld().getNearbyEntities(p.getLocation(), 35, 35, 35, e -> {
			if(e.equals(p) || !(e instanceof LivingEntity))
				return false;
			if(e instanceof Player) {
				RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
				ApplicableRegionSet set = query.getApplicableRegions(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(e.getLocation()));
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
			AbstractEntity ae = BukkitAdapter.adapt(e);
			AbstractVector entityVector = ap.getEyeLocation().toVector();
			AbstractVector targetVector = ae.getEyeLocation().toVector();
			AbstractVector headDirection = ap.getEyeLocation().getDirection();
			AbstractVector targetDirection = targetVector.subtract(entityVector).normalize();
			double targetAngle = Math.toDegrees((double)targetDirection.angle(headDirection));
			if(targetAngle > 30)
				return false;
			return true;
		}).stream().min((e1, e2) -> {
			double dist1 = e1.getLocation().distanceSquared(p.getLocation());
			double dist2 = e2.getLocation().distanceSquared(p.getLocation());
			if(dist1 == dist2)
				return 0;
			return dist1 < dist2 ? -1 : 1;
		}).ifPresent(e -> {
			target = e;
		});
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SKELETON_HORSE_HURT, 2.5f, 0.4f);

		if(target != null) {
			Location targetLoc = target.getLocation();
			new BukkitRunnable() {
				
				DustOptions dust = new DustOptions(Color.fromRGB(0, 85, 0), 1.25f);
				Location loc = p.getLocation().add(0, 1.6, 0);
				Vector dir = new Vector(targetLoc.getX() - loc.getX(),
						targetLoc.getY() - loc.getY(),
						targetLoc.getZ() - loc.getZ()).normalize().multiply(0.4);
				double t = 0;
				
				@Override
				public void run() {
					if(target == null || target.isDead()) {
						if(t >= 25) {
							this.cancel();
							return;
						}
						t += 0.6;
					} else {
						Location tmp = target.getLocation();
						dir = new Vector(tmp.getX() - loc.getX(),
								tmp.getY() - loc.getY(),
								tmp.getZ() - loc.getZ()).normalize().multiply(0.4);
					}
					loc.add(dir);
					p.getWorld().spawnParticle(Particle.REDSTONE, loc, 8, 0.3f, 0.3f, 0.3f, 0, dust);
					
					loc.getWorld().getNearbyEntities(loc, 3, 3, 3, e -> {
						AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
						AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.8, loc.getY()-1.5, loc.getZ()-0.8, loc.getX()+0.8, loc.getY()+1.5, loc.getZ()+0.8);
						if(!aabb.c(aabb2))
							return false;
						if(e.equals(p) || !(e instanceof LivingEntity))
							return false;
						if(e instanceof Player) {
							RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
							ApplicableRegionSet set = query.getApplicableRegions(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(e.getLocation()));
							State flag = set.queryValue(null, Flags.PVP);
							if(flag != null && flag.equals(State.ALLOW)
									&& !e.getWorld().getName().toLowerCase().contains("dungeon"))
								return false;
						}
						if(!MythicBukkit.inst().getMobManager().isMythicMob(e)
								&& e.getType().equals(EntityType.ARMOR_STAND))
							return false;
						if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
							return false;
						return true;
					}).stream().min((e1, e2) -> {
						double dist1 = e1.getLocation().distanceSquared(loc);
						double dist2 = e2.getLocation().distanceSquared(loc);
						if(dist1 == dist2)
							return 0;
						return dist1 < dist2 ? -1 : 1;
					}).ifPresent(e -> {
						spellEffect(loc, dmg);
						cancel();
					});
					
					if(this.isCancelled()) {
						return;
					}
					
					if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid() || !casterInCastWorld()) {
						spellEffect(loc, dmg);
						this.cancel();
						return;
					}
				}
			}.runTaskTimer(Main.getInstance(), 0, 1);
		} else {
			new BukkitRunnable() {
				
				DustOptions dust = new DustOptions(Color.fromRGB(0, 85, 0), 1.25f);
				Location loc = p.getLocation().add(0, 1.6, 0);
				Vector dir = p.getLocation().getDirection().normalize().multiply(0.4);
				double t = 0;
				@Override
				public void run() {
					if(t >= 35) {
						this.cancel();
						return;
					}
					t += 0.6;
					loc.add(dir);
					p.getWorld().spawnParticle(Particle.REDSTONE, loc, 8, 0.3f, 0.3f, 0.3f, 0, dust);

					loc.getWorld().getNearbyEntities(loc, 3, 3, 3, e -> {
						AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
						AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.8, loc.getY()-1.5, loc.getZ()-0.8, loc.getX()+0.8, loc.getY()+1.5, loc.getZ()+0.8);
						if(!aabb.c(aabb2))
							return false;
						if(e.equals(p) || !(e instanceof LivingEntity))
							return false;
						if(e instanceof Player) {
							RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
							ApplicableRegionSet set = query.getApplicableRegions(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(e.getLocation()));
							State flag = set.queryValue(null, Flags.PVP);
							if(flag != null && flag.equals(State.ALLOW)
									&& !e.getWorld().getName().toLowerCase().contains("dungeon"))
								return false;
						}
						if(!MythicBukkit.inst().getMobManager().isMythicMob(e)
								&& e.getType().equals(EntityType.ARMOR_STAND))
							return false;
						if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
							return false;
						return true;
					}).stream().min((e1, e2) -> {
						double dist1 = e1.getLocation().distanceSquared(loc);
						double dist2 = e2.getLocation().distanceSquared(loc);
						if(dist1 == dist2)
							return 0;
						return dist1 < dist2 ? -1 : 1;
					}).ifPresent(e -> {
						spellEffect(loc, dmg);
						cancel();
					});
					
					if(this.isCancelled()) {
						return;
					}
					
					if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid() || !casterInCastWorld()) {
						spellEffect(loc, dmg);
						this.cancel();
						return;
					}
				}
			}.runTaskTimer(Main.getInstance(), 0, 1);
		}
		
		
	}
	
	private void spellEffect(Location loc, double dmg) {
		DustOptions dust = new DustOptions(Color.fromRGB(0, 85, 0), 1.25f);
		p.getWorld().spawnParticle(Particle.REDSTONE, loc, dr.getObszar()*30, dr.getObszar()/2.0f, dr.getObszar()/4.0f, dr.getObszar()/2.0f, 0.2f, dust);
		p.getWorld().playSound(loc, Sound.ENTITY_PHANTOM_HURT, 2.5f, 0.2f);
		for(Entity e : loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar())) {
			if(e.getLocation().distance(loc) > dr.getObszar())
				continue;
			if(e.equals(p) || !(e instanceof LivingEntity))
				continue;
			if(e instanceof Player || e.hasMetadata("NPC")) {
				RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
				ApplicableRegionSet set = query.getApplicableRegions(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(e.getLocation()));
				if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
					continue;
			}
			if(!MythicBukkit.inst().getMobManager().isMythicMob(e)
					&& e.getType().equals(EntityType.ARMOR_STAND))
				continue;
			RuneDamage.damageNormal(p, (LivingEntity) e, dr, dmg);
		}
	}

}
