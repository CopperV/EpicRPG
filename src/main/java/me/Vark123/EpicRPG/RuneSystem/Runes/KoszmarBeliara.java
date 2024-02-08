package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
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

public class KoszmarBeliara extends ARune {

	public KoszmarBeliara(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1.25f, 0.7f);
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation();
			Location loc2 = loc.clone();
			Vector vec = loc.getDirection().normalize().multiply(1.15);
			List<Entity> shooted = new ArrayList<>();
			@Override
			public void run() {
				if(t >= 35 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				t+= 0.5;
				
				double x = vec.getX()*t;
				double y = vec.getY()*t + 1.5;
				double z = vec.getZ()*t;
				loc2.setX(loc.getX()+x);
				loc2.setY(loc.getY()+y);
				loc2.setZ(loc.getZ()+z);
				
				p.getWorld().spawnParticle(Particle.SPELL_WITCH, loc2, 4, 0.1f, 0.1f, 0.1f, 0.08f);
				p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc2, 4, 0.1f, 0.1f, 0.1f, 0.04f);
				
				loc2.getWorld().getNearbyEntities(loc2, 3, 3, 3, e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc2.getX()-0.6, loc2.getY()-0.6, loc2.getZ()-0.6, loc2.getX()+0.6, loc2.getY()+0.6, loc2.getZ()+0.6);
					if(!aabb.c(aabb2))
						return false;
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(shooted.contains(e))
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
					shooted.add(e);
					RuneDamage.damageNormal(p, (LivingEntity)e, dr);
					p.getWorld().spawnParticle(Particle.SPELL_WITCH, e.getLocation(), 15, 0.4f, 0.5f, 0.4f, 0.15f);
				});
				
				if(loc2.getBlock().getType().isSolid() && !loc2.getBlock().isLiquid()) {
					this.cancel();
					return;
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
