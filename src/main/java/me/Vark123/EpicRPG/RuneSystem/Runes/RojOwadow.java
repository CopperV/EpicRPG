package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
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

import io.lumine.mythic.api.adapters.AbstractEntity;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import net.minecraft.world.phys.AxisAlignedBB;

public class RojOwadow extends ARune {
	
	private LivingEntity le;

	public RojOwadow(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BEE_LOOP_AGGRESSIVE, 0.85f, 1.8f);
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation().add(0,1.25,0);
			Vector vec = loc.getDirection().normalize().multiply(1.15);
			@Override
			public void run() {
				if(t >= 35 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				++t;
				loc.add(vec);
				p.getWorld().spawnParticle(Particle.TOWN_AURA, loc, 20, 0.2f, 0.2f, 0.2f, 0.15f);

				loc.getWorld().getNearbyEntities(loc, 3, 4, 3, e -> {
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
						le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 1));
						new BukkitRunnable() {
							AbstractEntity mob = io.lumine.mythic.bukkit.BukkitAdapter.adapt(le);
							int timer = dr.getDurationTime();
							@Override
							public void run() {
								if(timer <= 0 || !casterInCastWorld() || !entityInCastWorld(e)) {
									this.cancel();
									return;
								}
								--timer;
								boolean end = RuneDamage.damageTiming(p, le, dr, dr.getDamage());
//								Bukkit.broadcastMessage("Test: "+end+" "+timer);
								if(!end) {
									this.cancel();
									return;
								}
								p.getWorld().spawnParticle(Particle.TOWN_AURA, io.lumine.mythic.bukkit.BukkitAdapter.adapt(mob.getEyeLocation()), 10, 0.3f, 0.3f, 0.3f, 0.07f);
							}
						}.runTaskTimer(Main.getInstance(), 0, 20);
					});
					p.getWorld().spawnParticle(Particle.TOWN_AURA, loc, 20, 0.4f, 0.4f, 0.4f, 0.3f);
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BEE_LOOP_AGGRESSIVE, 0.85f, 1.5f);
					cancel();
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
