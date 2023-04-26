package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Random;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.RuneDamage;
import net.minecraft.world.phys.AxisAlignedBB;

public class MalaBlyskawica extends ARune {

	public MalaBlyskawica(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
//		Block b = p.getTargetBlock((HashSet<Byte>)null, 25);
//		p.getWorld().strikeLightningEffect((p.getTargetBlock((HashSet<Byte>)null, 20)).getLocation());
		Block b = p.getTargetBlock((Set<Material>)null, 25);
		Random rand = new Random();
		Location bLoc = b.getLocation();
		Location sLoc = b.getLocation().add(rand.nextInt(20)-10, 40, rand.nextInt(20)-10);
		Vector vec = (new Vector(bLoc.getX()-sLoc.getX(), bLoc.getY()-sLoc.getY(), bLoc.getZ()-sLoc.getZ())).normalize();
		double temp = 0;
		for(double k = sLoc.getBlockY(); k>= bLoc.getBlockY(); k-=0.5) {
			double x = vec.getX()*temp;
			double y = vec.getY()*temp;
			double z = vec.getZ()*temp;
			sLoc.add(x, y, z);
//			Bukkit.broadcastMessage("Particle: "+sLoc.getX()+":"+sLoc.getY()+":"+sLoc.getZ());
			p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, sLoc, 10,0.1F,0.1F,0.1F,0.05F);
			sLoc.subtract(x, y, z);
			temp+=0.5;
		}
		bLoc.getWorld().playSound(bLoc, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1, 1);
		
		for(Entity e:bLoc.getWorld().getNearbyEntities(bLoc, 10, 10, 10)) {
			AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
			AxisAlignedBB aabb2 = new AxisAlignedBB(bLoc.getX()-2, bLoc.getY()-2, bLoc.getZ()-2, bLoc.getX()+2, bLoc.getY()+10, bLoc.getZ()+2);
			if(aabb.c(aabb2)) {
				if(!e.equals(p) && e instanceof LivingEntity) {
					if(e instanceof Player || e.hasMetadata("NPC")) {
						Location loc = e.getLocation();
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
						if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
							continue;
					}
					RuneDamage.damageNormal(p, (LivingEntity)e, dr);
					e.getWorld().playSound(b.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 1, 1);
					break;
				}
			}
		}
	}

}
