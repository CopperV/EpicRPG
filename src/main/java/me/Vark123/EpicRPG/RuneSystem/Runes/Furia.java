package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
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
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import net.minecraft.world.phys.AxisAlignedBB;

public class Furia extends ARune {

	private static final double red = 138./255.;
	private static final double green = 3./255.;
	private static final double blue = 3./255.;
	private static final Random rand = new Random();
	
	public Furia(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		new BukkitRunnable() {
			int counter = 3;
			@Override
			public void run() {
				if(counter <= 0 || !casterInCastWorld()) {
					cancel();
					return;
				}
				if(isCancelled())
					return;
				
				Location loc = p.getLocation().add(0, 1, 0);
				castProjectile(loc);
				--counter;
			}
		}.runTaskTimer(Main.getInstance(), 0, 10);
	}
	
	private void castProjectile(Location startLoc) {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1.3f);

		new BukkitRunnable() {
			double distance = 30;
			Location loc = startLoc.clone();
			Vector vec = loc.getDirection().normalize().multiply(0.75);
			@Override
			public void run() {
				if(startLoc.distanceSquared(loc) > (distance*distance) 
						|| !casterInCastWorld()) {
					cancel();
					return;
				}
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
					this.cancel();
					return;
				}
				if(isCancelled())
					return;
				
				for(int i = 0; i < 12; ++i) {
					double x = rand.nextDouble(0.8) - 0.4;
					double y = rand.nextDouble(0.8) - 0.4;
					double z = rand.nextDouble(0.8) - 0.4;
					Location tmp = loc.clone().add(x,y,z);
					p.getWorld().spawnParticle(Particle.SPELL_MOB, tmp, 0, red, green, blue, 1);
				}
				
				loc.getWorld().getNearbyEntities(loc, 3, 3, 3, e -> {
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
					p.getWorld().playSound(e.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 0.66f);
					RuneDamage.damageNormal(p, (LivingEntity)e, dr);
					cancel();
				});
				
				loc.add(vec);
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
