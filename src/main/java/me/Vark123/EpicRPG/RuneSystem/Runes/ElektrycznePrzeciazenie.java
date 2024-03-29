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

public class ElektrycznePrzeciazenie extends ARune {
	
	private LivingEntity le;

	public ElektrycznePrzeciazenie(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BEE_LOOP_AGGRESSIVE, 1.1f, 2f);
		PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 2);
		p.addPotionEffect(effect);
		
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation().add(0,1.25,0);
			Vector vec = loc.getDirection().normalize().multiply(1.1);
			@Override
			public void run() {
				if(t >= 50 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				++t;
				loc.add(vec);
				p.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 7, 0.15f, 0.15f, 0.15f, 0.05f);
				
				loc.getWorld().getNearbyEntities(loc, 3, 3, 3, e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.9, loc.getY()-1.5, loc.getZ()-0.9, loc.getX()+0.9, loc.getY()+1.5, loc.getZ()+0.9);
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
					RuneDamage.damageNormal(p, le, dr, (p, le, dr) ->{
						le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 9));
						double dmg = dr.getDamage()/5;
						new BukkitRunnable() {
							int timer = dr.getDurationTime();
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
								le.getWorld().playSound(le.getLocation(), Sound.ENTITY_BEE_HURT, 1f, 2f);
								p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, le.getLocation(), 15, 0.3f, 0.4f, 0.3f, 0.05f);
							}
						}.runTaskTimer(Main.getInstance(), 0, 20);
					});
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BEE_HURT, 1f, 2f);
					this.cancel();
				});
				
				if(this.isCancelled())
					return;
				
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
					this.cancel();
					return;
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
