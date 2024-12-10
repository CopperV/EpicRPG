package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.Sound;
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
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import net.minecraft.world.phys.AxisAlignedBB;

public class ZewSmierci extends ARune {

	private static final DustOptions dust = new DustOptions(Color.RED, 0.8f);
	private static final double red = 138./255.;
	private static final double green = 3./255.;
	private static final double blue = 3./255.;
	private static final Random rand = new Random();
	
	public ZewSmierci(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.7f, 0.4f);
		
		Location pLoc = p.getLocation().clone().add(0,1.15,0);
		double radius = dr.getObszar();
		
		List<Location> locations = new LinkedList<>();
		for(int i = 0; i < 30; ++i) {
			double angle = rand.nextDouble(Math.PI*2);
			double x = Math.sin(angle) * radius;
			double z = Math.cos(angle) * radius;
			
			locations.add(pLoc.clone().add(x,0,z));
		}

		p.getWorld().spawnParticle(Particle.REDSTONE, pLoc, 16, 0.45f, 0.8f, 0.45f, 0.15f, dust);
		locations.forEach(loc -> castProjectile(loc, pLoc));
	}
	
	private void castProjectile(Location from, Location to) {
		new BukkitRunnable() {
			Location loc = from.clone();
			double distance = dr.getObszar();
			Vector dir = new Vector(
					to.getX() - from.getX(),
					to.getY() - from.getY(),
					to.getZ() - from.getZ()
					).normalize().multiply(0.1);
			@Override
			public void run() {
				if(from.distanceSquared(loc) > (distance*distance) 
						|| !casterInCastWorld()) {
					cancel();
					return;
				}
				if(isCancelled())
					return;
				
				for(int i = 0; i < 6; ++i) {
					double x = rand.nextDouble(0.4) - 0.2;
					double y = rand.nextDouble(0.4) - 0.2;
					double z = rand.nextDouble(0.4) - 0.2;
					Location tmp = loc.clone().add(x,y,z);
					p.getWorld().spawnParticle(Particle.SPELL_MOB, tmp, 0, red, green, blue, 1);
				}
				
				loc.getWorld().getNearbyEntities(loc, 3, 3, 3, e -> {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.5, loc.getY()-0.75, loc.getZ()-0.5, loc.getX()+0.5, loc.getY()+0.75, loc.getZ()+0.5);
					if(!aabb.c(aabb2))
						return false;
					if(!(e instanceof Player))
						return false;
					if(e.equals(p))
						return true;
					RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
					ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
					State flag = set.queryValue(null, Flags.PVP);
					if(flag != null && flag.equals(State.ALLOW)
							&& !(e.getWorld().getName().toLowerCase().contains("dungeon") || e.getWorld().getName().toLowerCase().contains("raid")))
						return false;
					return true;
				}).stream().min((e1, e2) -> {
					double dist1 = e1.getLocation().distanceSquared(loc);
					double dist2 = e2.getLocation().distanceSquared(loc);
					if(dist1 == dist2)
						return 0;
					return dist1 < dist2 ? -1 : 1;
				}).ifPresent(e -> {
					Player _p = (Player) e;
					_p.getWorld().playSound(_p.getLocation(), Sound.ENTITY_CAT_HISS, 0.9f, 1.25f);
					_p.getWorld().spawnParticle(Particle.HEART, _p.getLocation().add(0,1,0), 12, 0.45f, 0.8f, 0.45f, 0.1f);

					RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(_p);
					double amount = _p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*0.04;

					RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, amount);
					Bukkit.getPluginManager().callEvent(event);
					
					cancel();
				});
				if(isCancelled())
					return;
				
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
								&& !(e.getWorld().getName().toLowerCase().contains("dungeon") || e.getWorld().getName().toLowerCase().contains("raid")))
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
					p.getWorld().playSound(e.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 1.2f);
					RuneDamage.damageNormal(p, (LivingEntity)e, dr);
					cancel();
				});
				
				loc.add(dir);
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
