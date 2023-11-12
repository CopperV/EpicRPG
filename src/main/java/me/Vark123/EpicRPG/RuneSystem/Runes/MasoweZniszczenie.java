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
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;

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
				
				loc.getWorld().getNearbyEntities(loc, t, t, t).stream().filter(e -> {
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
					RuneDamage.damageNormal(p, (LivingEntity)e, dr);
					loc.getWorld().playSound(e.getLocation(), Sound.ENTITY_EVOKER_PREPARE_SUMMON, 0.7f, 0.3f);
					shooted.add(e);
				});
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
	}

}
