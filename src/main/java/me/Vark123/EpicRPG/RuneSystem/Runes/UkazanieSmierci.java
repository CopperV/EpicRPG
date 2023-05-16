package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
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
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import net.minecraft.world.phys.AxisAlignedBB;

public class UkazanieSmierci extends ARune {

	public UkazanieSmierci(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 1, 1);
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation();
			Vector vec = loc.getDirection().normalize();
			public void run() {
				for(int i = 0; i < 2; ++i) {
					t+=0.7;
					double x = vec.getX()*t;
					double y = vec.getY()*t+1.5;
					double z = vec.getZ()*t;
					loc.add(x,y,z);
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc,1,0,0,0,0);
					p.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc,10,0.6F,0.6F,0.6F,0.05F);

					loc.getWorld().getNearbyEntities(loc, 5, 5, 5, e -> {
						AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
						AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1.25, loc.getY()-1.25, loc.getZ()-1.25, loc.getX()+1.25, loc.getY()+1.25, loc.getZ()+1.25);
						if(!aabb.c(aabb2))
							return false;
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
						if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
							return false;
						return true;
					}).parallelStream().min((e1, e2) -> {
						double dist1 = e1.getLocation().distanceSquared(loc);
						double dist2 = e2.getLocation().distanceSquared(loc);
						if(dist1 == dist2)
							return 0;
						return dist1 < dist2 ? -1 : 1;
					}).ifPresent(e -> {
						String name = e.getName().toLowerCase();
						if(name.contains("legendary") || name.contains("legendarny")) {
							RuneDamage.damageNormal(p, (LivingEntity)e, dr);
							this.cancel();
							return;
						}
						spellEffect(e);
						this.cancel();
					});
					
					if(this.isCancelled()) {
						return;
					}
					
					if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
						this.cancel();
						return;
					}
					loc.subtract(x, y, z);
					if(t>60 || !casterInCastWorld()) {
						this.cancel();
						return;
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
	
	private void spellEffect(Entity e) {
		if(isHit()) {
			p.getWorld().spawnParticle(Particle.SMOKE_LARGE, e.getLocation(),20,1F,1F,1F,0.4F);
			e.getLocation().getWorld().playSound(e.getLocation(), Sound.ENTITY_SKELETON_HORSE_DEATH, 10, 1);
			RuneDamage.damageNormal(p, (LivingEntity)e, dr, ((LivingEntity)e).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*3);
		}else {
			p.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, e.getLocation(),20,1F,1F,1F,0.4F);
			e.getLocation().getWorld().playSound(e.getLocation(), Sound.ENTITY_VILLAGER_YES, 10, 1);
			RuneDamage.damageNormal(p, (LivingEntity)e, dr);
		}
	}
	
	private boolean isHit() {
		Random rand = new Random();
		List<Integer> losy = new ArrayList<>();
		int amount = 0;
		int los;
		while(amount < 5) {
			los = rand.nextInt(100);
			if(losy.contains(los)) continue;
			losy.add(los);
			++amount;
		}
		los = rand.nextInt(100);
		if(losy.contains(los)) return true;
		return false;
	}

}
