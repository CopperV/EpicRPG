package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.EntityType;
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

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import net.minecraft.world.phys.AxisAlignedBB;

public class DuzaKulaOgnia extends ARune {

	public DuzaKulaOgnia(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation();
			Vector vec = loc.getDirection().normalize();
			LivingEntity le;
//			double dmg = dr.getDamage();
//			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
			public void run() {
				t++;
				double x = vec.getX()*t;
				double y = vec.getY()*t+1.5;
				double z = vec.getZ()*t;
				loc.add(x,y,z);
				p.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc,10,0.5,0.5,0.5,0.05);
				p.getWorld().spawnParticle(Particle.FLAME, loc,40,2,2,2,0.25);
				
				loc.getWorld().getNearbyEntities(loc, 5, 5, 5, e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-2, loc.getY()-2, loc.getZ()-2, loc.getX()+2, loc.getY()+2, loc.getZ()+2);
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
					le = (LivingEntity) e;
					RuneDamage.damageNormal(p, le, dr, (p,le,dr)->{
						new BukkitRunnable() {
							int timer = 7;
							double dmg = dr.getDamage()/50.0;
							@Override
							public void run() {
								if(timer <= 0 || !casterInCastWorld() || !entityInCastWorld(e)) {
									this.cancel();
									return;
								}
								--timer;
								boolean end = RuneDamage.damageTiming(p, le, dr, dmg);
								if(!end) {
									this.cancel();
									return;
								}
								Location loc = le.getLocation().add(0,1,0);
								p.getWorld().spawnParticle(Particle.FLAME, loc,10,0.2,0.2,0.2,0.05);
								p.getWorld().playSound(loc, Sound.ENTITY_GENERIC_BURN, 1, 1);
							}
						}.runTaskTimer(Main.getInstance(), 0, 20);
					});
					e.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
					this.cancel();
				});
				
				if(this.isCancelled())
					return;
				
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
//				if(!loc.getBlock().getType().equals(Material.AIR)) {
					this.cancel();
					return;
				}
				loc.subtract(x, y, z);
				if(t>50 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
