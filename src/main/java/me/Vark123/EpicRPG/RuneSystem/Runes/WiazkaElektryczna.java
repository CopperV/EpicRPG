package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import net.minecraft.world.phys.AxisAlignedBB;

public class WiazkaElektryczna extends ARune {

	private List<Entity> shooted;
	
	public WiazkaElektryczna(ItemStackRune dr, Player p) {
		super(dr, p);
		shooted = new ArrayList<Entity>();
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_METAL_STEP, 1, 1);
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation();
			Vector vec = loc.getDirection().normalize();
			LivingEntity le;
			public void run() {
				for(int i = 0; i < 2; ++i) {
					t+=0.75;
					double x = vec.getX()*t;
					double y = vec.getY()*t+1.5;
					double z = vec.getZ()*t;
					loc.add(x,y,z);
					p.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, loc,10,0.1F,0.1F,0.1F,0);

					loc.getWorld().getNearbyEntities(loc, 5, 5, 5, e -> {
						AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
						AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1, loc.getY()-1, loc.getZ()-1, loc.getX()+1, loc.getY()+1, loc.getZ()+1);
						if(!aabb.c(aabb2))
							return false;
						if(e.equals(p) || !(e instanceof LivingEntity))
							return false;
						if(shooted.contains(e))
							return false;
						if(shooted.size() >= 5)
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
					}).stream().min((e1, e2) -> {
						double dist1 = e1.getLocation().distanceSquared(loc);
						double dist2 = e2.getLocation().distanceSquared(loc);
						if(dist1 == dist2)
							return 0;
						return dist1 < dist2 ? -1 : 1;
					}).ifPresent(e -> {
						shooted.add(e);
						le = (LivingEntity) e;
						RuneDamage.damageNormal(p, le, dr, (p,le,dr)->{
							spellEffect(e);
						});
					});
					
					if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
						this.cancel();
						return;
					}
					loc.subtract(x, y, z);
					if(t>50 || !casterInCastWorld()) {
						this.cancel();
						return;
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
	private void spellEffect(Entity e) {
		Location loc = e.getLocation();
		Random rand = new Random();
		Location sLoc = new Location(loc.getWorld(), loc.getX()+rand.nextInt(20)-10, loc.getY()+40, loc.getZ()+rand.nextInt(20)-10);
		Vector vec = (new Vector(loc.getX()-sLoc.getX(), loc.getY()-sLoc.getY(), loc.getZ()-sLoc.getZ())).normalize();
		double temp = 0;
		for(double k = sLoc.getBlockY(); k>= loc.getBlockY(); k-=0.5) {
			double x = vec.getX()*temp;
			double y = vec.getY()*temp;
			double z = vec.getZ()*temp;
			sLoc.add(x, y, z);
			p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, sLoc, 10,0.1F,0.1F,0.1F,0.05F);
			sLoc.subtract(x, y, z);
			temp+=0.5;
		}
		loc.getWorld().playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1, 1);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				new BukkitRunnable() {
					int t = 1;
					@Override
					public void run() {
						if(t>=dr.getDurationTime() || !casterInCastWorld() || !entityInCastWorld(e)) {
							this.cancel();
							return;
						}
						++t;
						p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 20,1F,1F,1F,0.2F);
						if(!RuneDamage.damageTiming(p, (LivingEntity)e, dr)) {
							this.cancel();
							return;
						}
					}
				}.runTaskTimer(Main.getInstance(), 0, 20);
			}
		}.runTaskLater(Main.getInstance(), 20);
		
	}

}
