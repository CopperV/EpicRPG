package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.ArmorStand;
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

import io.lumine.mythic.bukkit.utils.lib.lang3.mutable.MutableBoolean;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import net.minecraft.world.phys.AxisAlignedBB;

public class UderzenieWiatru extends ARune {

	private List<Entity> shooted;
	
	public UderzenieWiatru(ItemStackRune dr, Player p) {
		super(dr, p);
		shooted = new ArrayList<Entity>();
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation();
			Vector vec = loc.getDirection().normalize();
			public void run() {
				for(int i = 0; i < 4; ++i) {
					t+=0.75;
					double x = vec.getX()*t;
					double y = vec.getY()*t+1.5;
					double z = vec.getZ()*t;
					loc.add(x,y,z);
					p.getWorld().spawnParticle(Particle.CLOUD, loc,1,0,0,0,0);
					p.getWorld().spawnParticle(Particle.CLOUD, loc,10,0.33F,0.33F,0.33F,0.15F);
					if(odrzutaZakoncz(loc, vec)) {
						this.cancel();
						return;
					}
					if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
						this.cancel();
						return;
					}
					loc.subtract(x, y, z);
					if(t>40 || !casterInCastWorld()) {
						this.cancel();
						return;
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

	private boolean odrzutaZakoncz(Location loc, Vector vec) {
		MutableBoolean toReturn = new MutableBoolean(false);
		
		loc.getWorld().getNearbyEntities(loc, 5, 5, 5, e -> {
			AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
			AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-2.25, loc.getY()-2.25, loc.getZ()-2.25, loc.getX()+2.25, loc.getY()+2.25, loc.getZ()+2.25);
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
		}).forEach(e -> {
			shooted.add(e);
			RuneDamage.damageNormal(p, (LivingEntity)e, dr);
			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 1);
			if(!(e instanceof ArmorStand 
					|| (e instanceof AbstractHorse 
							&& ((AbstractHorse)e).getOwner() instanceof Player)))
				e.setVelocity(new Vector(vec.getX()*3,2,vec.getZ()*3));
			if(!toReturn.booleanValue())
				toReturn.setValue(true);
		});
		
		return toReturn.booleanValue();
	}
	
}
