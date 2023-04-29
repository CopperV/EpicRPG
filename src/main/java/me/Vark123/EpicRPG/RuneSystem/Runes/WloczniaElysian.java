package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import net.minecraft.world.phys.AxisAlignedBB;

public class WloczniaElysian extends ARune {

	private LinkedList<Location> stos;
	private List<Entity> shooted;
	
	public WloczniaElysian(ItemStackRune dr, Player p) {
		super(dr, p);
		stos = new LinkedList<>();
		shooted = new ArrayList<>();
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ITEM_TRIDENT_THROW, 1.2f, 0.2f);
		new BukkitRunnable() {
			int t = 0;
			Location loc = p.getLocation().add(0, 1, 0);
			Vector vec = loc.getDirection().normalize();
			@Override
			public void run() {
				if(t>=45 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				++t;
				
				for(int i = 0; i < 2; ++i) {
					loc.add(vec);
					stos.addLast(loc.clone());
					while(stos.size() > 5)
						stos.removeFirst();
					
					for(Location point : stos)
						p.getWorld().spawnParticle(Particle.END_ROD, point, 7, 0.05f, 0.05f, 0.05f, 0.02f);

					loc.getWorld().getNearbyEntities(loc, 3, 3, 3, e -> {
						AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
						AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.7, loc.getY()-0.7, loc.getZ()-0.7, loc.getX()+0.7, loc.getY()+0.7, loc.getZ()+0.7);
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
						shooted.add(e);
						RuneDamage.damageNormal(p, (LivingEntity)e, dr);
						p.getWorld().spawnParticle(Particle.END_ROD, e.getLocation().add(0,1,0), 15, 0.4f, 0.5f, 0.4f, 0.15f);
						p.getWorld().playSound(e.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 1, 0.7f);
					});
					
					if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
						this.cancel();
						return;
					}
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
