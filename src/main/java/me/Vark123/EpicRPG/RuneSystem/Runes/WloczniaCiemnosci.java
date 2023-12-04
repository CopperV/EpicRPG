package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
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
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import net.minecraft.world.phys.AxisAlignedBB;

public class WloczniaCiemnosci extends ARune {

	private static final DustOptions dust = new DustOptions(Color.PURPLE, 0.85f);
	
	private LinkedList<Location> stos;
	private List<Entity> shooted;
	
	public WloczniaCiemnosci(ItemStackRune dr, Player p) {
		super(dr, p);
		stos = new LinkedList<>();
		shooted = new ArrayList<>();
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_STRAY_HURT, 1.2f, 0.2f);
		new BukkitRunnable() {
			int t = 0;
			Location loc = p.getLocation().add(0, 1, 0);
			Vector vec = loc.getDirection().normalize();
			@Override
			public void run() {
				if(t>=45 || !casterInCastWorld() || shooted.size() >= 4) {
					this.cancel();
					return;
				}
				++t;
				
				for(int i = 0; i < 2; ++i) {
					loc.add(vec);
					stos.addLast(loc.clone());
					while(stos.size() > 5)
						stos.removeFirst();
					
					for(Location point : stos) {
						p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, point, 3, 0, 0, 0, 0.05F);
						p.getWorld().spawnParticle(Particle.REDSTONE, point, 2, 0.15F, 0.15F, 0.15F, 0.1F,dust);
					}

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
						RuneDamage.damageNormal(p, (LivingEntity)e, dr);
						p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, e.getLocation().add(0,1,0), 13, 0.2f, 0.2f, 0.2f, 0.1f);
						p.getWorld().spawnParticle(Particle.REDSTONE, e.getLocation().add(0,1,0), 15, 0.7F, 0.7F, 0.7F, 0.1F,dust);
						p.getWorld().playSound(e.getLocation(), Sound.ENTITY_SKELETON_HORSE_DEATH, 1, 1.4f);
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
