package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.RuneDamage;

public class Czystka extends ARune {

	public Czystka(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		List<Entity> list = new LinkedList<>();
		
		for(Entity e : p.getWorld().getNearbyEntities(p.getLocation(), dr.getObszar(), dr.getObszar(), dr.getObszar())) {
			if(e.equals(p) || !(e instanceof LivingEntity))
				continue;
			if(e instanceof Player || e.hasMetadata("NPC")) {
				Location loc = e.getLocation();
				RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
				ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
				if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
					continue;
			}
			list.add(e);
		}
		
		if(list.size() == 0)
			return;

		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, .4f);
		double dmg = (double)dr.getDamage()/(double)list.size();
		for(Entity e : list) {
			e.getWorld().spawnParticle(Particle.SNEEZE, e.getLocation().add(0, 1, 0), 12, .4f, .4f, .4f, .15f);
			RuneDamage.damageNormal(p, (LivingEntity)e, dr, dmg);
		}
		
	}

}
