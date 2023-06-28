package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
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

public class ZatrutaKrew extends ARune {

	public ZatrutaKrew(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1f, 0.4f);
		new BukkitRunnable() {
			LivingEntity le;
			double t = 0;
			Location loc = p.getLocation().add(0,1.25,0);
			Vector vec = loc.getDirection().normalize().multiply(1.2);
			DustOptions dust = new DustOptions(Color.fromRGB(170, 83, 3), 1.6f);
			@Override
			public void run() {
				if(t >= 40 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				++t;
				loc.add(vec);
				p.getWorld().spawnParticle(Particle.REDSTONE, loc, 8, 0.2f, 0.2f, 0.2f, 0.15f, dust);

				loc.getWorld().getNearbyEntities(loc, 3, 3, 3, e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-1, loc.getY()-1.5, loc.getZ()-1, loc.getX()+1, loc.getY()+1.5, loc.getZ()+1);
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
				}).stream().min((e1, e2) -> {
					double dist1 = e1.getLocation().distanceSquared(loc);
					double dist2 = e2.getLocation().distanceSquared(loc);
					if(dist1 == dist2)
						return 0;
					return dist1 < dist2 ? -1 : 1;
				}).ifPresent(e -> {
					le = (LivingEntity) e;
					RuneDamage.damageNormal(p, le, dr, (p, le, dr) ->{
						new BukkitRunnable() {
							int timer = dr.getDurationTime();
							@Override
							public void run() {
								if(timer <= 0 || !casterInCastWorld() || !entityInCastWorld(e)) {
									this.cancel();
									return;
								}
								--timer;
								boolean end = RuneDamage.damageTiming(p, le, dr, dr.getDamage());
								if(!end) {
									this.cancel();
									return;
								}
								p.getWorld().spawnParticle(Particle.REDSTONE, le.getLocation().add(0, 1, 0), 7, 0.3f, 0.3f, 0.3f, 0.07f, dust);
							}
						}.runTaskTimer(Main.getInstance(), 0, 20);
					});
					p.getWorld().spawnParticle(Particle.REDSTONE, loc, 15, 0.4f, 0.4f, 0.4f, 0.3f, dust);
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_BIG_FALL, 1.1f, 0.6f);
					this.cancel();
				});
				
				if(this.isCancelled()) {
					return;
				}
				
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
