package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Random;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
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

		bLoc.getWorld().getNearbyEntities(bLoc, 4, 4, 4, e -> {
			AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
			AxisAlignedBB aabb2 = new AxisAlignedBB(bLoc.getX()-2, bLoc.getY()-2, bLoc.getZ()-2, bLoc.getX()+2, bLoc.getY()+10, bLoc.getZ()+2);
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
			double dist1 = e1.getLocation().distanceSquared(bLoc);
			double dist2 = e2.getLocation().distanceSquared(bLoc);
			if(dist1 == dist2)
				return 0;
			return dist1 < dist2 ? -1 : 1;
		}).ifPresent(e -> {
			RuneDamage.damageNormal(p, (LivingEntity)e, dr);
			e.getWorld().playSound(b.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 1, 1);
		});
	}

}
