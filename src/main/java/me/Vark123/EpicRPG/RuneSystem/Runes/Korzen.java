package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
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

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import net.minecraft.world.phys.AxisAlignedBB;

public class Korzen extends ARune{

	public Korzen(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, 3, 0.35f);
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation();
			Vector vec = loc.getDirection().normalize();
			public void run() {
				if(t>40 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				t+=0.35;
				double x = vec.getX()*t;
				double y = vec.getY()*t+1;
				double z = vec.getZ()*t;
				loc.add(x,y,z);
				p.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc,40,0.4F,0.4F,0.4F,0.02F, Bukkit.createBlockData(Material.DIRT));
						
				loc.getWorld().getNearbyEntities(loc, 5, 5, 5, e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.75, loc.getY()-0.75, loc.getZ()-0.75, loc.getX()+0.75, loc.getY()+0.75, loc.getZ()+0.75);
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
					zadajDamage(e);
					cancel();
				});
				
				if(this.isCancelled()) {
					return;
				}

				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
					this.cancel();
					return;
				}
				if(t>40) {
					this.cancel();
					return;
				}
				loc.subtract(x, y, z);
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
	private void zadajDamage(Entity e) {
		((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 10));
		RuneDamage.damageNormal(p, (LivingEntity)e, dr);
		new BukkitRunnable() {
			double t = 0;
			@Override
			public void run() {
				if(t>=dr.getDurationTime() || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				if(!e.isDead()) {
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, e.getLocation().add(0,0.25,0),40,0.3F,0.2F,0.3F,0.05f,Bukkit.createBlockData(Material.DIRT));
				}
				t+=0.5;
			}
		}.runTaskTimer(Main.getInstance(), 0, 10);
	}

}
