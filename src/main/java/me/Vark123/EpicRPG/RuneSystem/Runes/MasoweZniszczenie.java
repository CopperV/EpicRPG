package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.RuneDamage;

public class MasoweZniszczenie extends ARune {

	private List<Entity> shooted;
	
	public MasoweZniszczenie(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		DustOptions dust = new DustOptions(Color.PURPLE, 3);
		shooted = new ArrayList<>();
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EVOKER_PREPARE_SUMMON, 2, 0.1f);
		new BukkitRunnable() {
			
			Location loc = p.getLocation();
			double t = 0;
			
			@Override
			public void run() {
				
				if(t>dr.getObszar() || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				t+= 0.25;
				int points = (int) (t*2 + 1);
				List<Location> pointLocs = new ArrayList<>();
				for(int i = 0; i < points; ++i) {
					double x = loc.getX()+Math.random()*2*t-t;
					double z = loc.getZ()+Math.random()*2*t-t;
					double y = loc.getY()+Math.random()*4-2;
					pointLocs.add(new Location(loc.getWorld(), x, y, z));
				}
				for(Location l : pointLocs) {
					p.getWorld().spawnParticle(Particle.REDSTONE, l, 2, 0.2f, 0.2f, 0.2f, 0.1f, dust);
				}
				for(Entity entity : loc.getWorld().getNearbyEntities(loc, t, t, t)) {
					if(!entity.equals(p) && entity instanceof LivingEntity && !shooted.contains(entity)) {
						if(entity instanceof Player || entity.hasMetadata("NPC")) {
							RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
							ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
							if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
								continue;
						}
						RuneDamage.damageNormal(p, (LivingEntity)entity, dr);
						loc.getWorld().playSound(loc, Sound.ENTITY_EVOKER_PREPARE_SUMMON, 0.7f, 0.3f);
						shooted.add(entity);
					}
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
	}

}
