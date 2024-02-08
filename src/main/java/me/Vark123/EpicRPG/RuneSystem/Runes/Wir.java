package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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

public class Wir extends ARune {

	public Wir(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
		new BukkitRunnable() {
			double t = 1;
			Location loc = p.getLocation();
			Vector vec = loc.getDirection().normalize();
			LivingEntity le;
			@Override
			public void run() {
				for(int i = 0; i < 3; ++i) {
					t+=0.6;
					double x = vec.getX()*t;
					double y = vec.getY()*t+1.5;
					double z = vec.getZ()*t;
					loc.add(x,y,z);
					p.getWorld().spawnParticle(Particle.CLOUD, loc,1,0,0,0,0);
					p.getWorld().spawnParticle(Particle.CLOUD, loc,10,0.3F,0.3F,0.3F,0);

					
					loc.getWorld().getNearbyEntities(loc, 5, 5, 5, e -> {
						AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
						AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1, loc.getY()-1, loc.getZ()-1, loc.getX()+1, loc.getY()+1, loc.getZ()+1);
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
							le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 10));
							new BukkitRunnable() {
								@Override
								public void run() {
									new BukkitRunnable() {
										double t = 0.5;
										@Override
										public void run() {
											if(t>=dr.getDurationTime() || !casterInCastWorld() || !entityInCastWorld(e)) {
												this.cancel();
												return;
											}
											t+=0.5;
											p.getWorld().spawnParticle(Particle.CLOUD, le.getLocation().add(0, 0.7, 0),10,0.7F,0.7F,0.7F,0.15F);
											if(!RuneDamage.damageTiming(p, le, dr, dr.getDamage()/2)) {
												this.cancel();
												return;
											}
										}
									}.runTaskTimer(Main.getInstance(), 0, 10);
									
								}
							}.runTaskLater(Main.getInstance(), 10);
						});
						e.getWorld().playSound(loc, Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
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
					if(t>35 || !casterInCastWorld()) {
						this.cancel();
						return;
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
