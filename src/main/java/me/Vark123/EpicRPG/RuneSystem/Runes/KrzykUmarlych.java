package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractPlayer;
import io.lumine.mythic.api.adapters.AbstractVector;
import io.lumine.mythic.bukkit.BukkitAdapter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.RuneDamage;
import net.minecraft.world.phys.AxisAlignedBB;

public class KrzykUmarlych extends ARune {

	private Entity target = null;
	private double dmg = 0;
	
	public KrzykUmarlych(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		
		RpgPlayer rpg = Main.getListaRPG().get(p.getUniqueId().toString());
		if(rpg.getPresent_mana() < 1)
			return;
		dmg = rpg.getPresent_mana() * 3;
		rpg.removePresentMana(rpg.getPresent_mana());
		
		AbstractPlayer ap = BukkitAdapter.adapt(p);
		
		double maxLength = 0;
		
		Collection<Entity> collection = p.getWorld().getNearbyEntities(p.getLocation(), 35, 35, 35);
		Iterator<Entity> iterator = collection.iterator();
		while(iterator.hasNext()) {
			Entity en = iterator.next();
			if(en.equals(p) || !(en instanceof LivingEntity))
				continue;
			if(en instanceof Player || en.hasMetadata("NPC")) {
				Location loc = en.getLocation();
				RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
				ApplicableRegionSet set = query.getApplicableRegions(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(en.getLocation()));
				if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
					continue;
			}
			AbstractEntity ae = BukkitAdapter.adapt(en);
			AbstractVector entityVector = ap.getEyeLocation().toVector();
			AbstractVector targetVector = ae.getEyeLocation().toVector();
			AbstractVector headDirection = ap.getEyeLocation().getDirection();
			AbstractVector targetDirection = targetVector.subtract(entityVector).normalize();
			double targetAngle = Math.toDegrees((double)targetDirection.angle(headDirection));
			if(targetAngle > 30)
				continue;
			if(target == null) {
				target = en;
				maxLength = en.getLocation().distance(p.getLocation());
			} else {
				double lenght = en.getLocation().distance(p.getLocation());
				if(lenght < maxLength) {
					maxLength = lenght;
					target = en;
				}
			}
		}
		
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
					for(Entity e : loc.getWorld().getNearbyEntities(loc, 3, 3, 3)) {
						AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
						AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.8, loc.getY()-1.5, loc.getZ()-0.8, loc.getX()+0.8, loc.getY()+1.5, loc.getZ()+0.8);
						if(aabb.c(aabb2)) {
							if(!e.equals(p) && e instanceof LivingEntity) {
								if(e instanceof Player || e.hasMetadata("NPC")) {
									RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
									ApplicableRegionSet set = query.getApplicableRegions(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(e.getLocation()));
									if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
										continue;
								}
								spellEffect(loc, dmg);
								this.cancel();
								return;
							}
						}
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
					for(Entity e : loc.getWorld().getNearbyEntities(loc, 3, 3, 3)) {
						AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
						AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.8, loc.getY()-1.5, loc.getZ()-0.8, loc.getX()+0.8, loc.getY()+1.5, loc.getZ()+0.8);
						if(aabb.c(aabb2)) {
							if(!e.equals(p) && e instanceof LivingEntity) {
								if(e instanceof Player || e.hasMetadata("NPC")) {
									RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
									ApplicableRegionSet set = query.getApplicableRegions(com.sk89q.worldedit.bukkit.BukkitAdapter.adapt(e.getLocation()));
									if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
										continue;
								}
								spellEffect(loc, dmg);
								this.cancel();
								return;
							}
						}
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
			RuneDamage.damageNormal(p, (LivingEntity) e, dr, dmg);
		}
	}

}
