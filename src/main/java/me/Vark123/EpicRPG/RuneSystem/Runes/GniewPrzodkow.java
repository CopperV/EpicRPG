package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class GniewPrzodkow extends ARune {

	private static final double red = 138./255.;
	private static final double green = 3./255.;
	private static final double blue = 3./255.;
	private static final Random rand = new Random();
	private Collection<Entity> shooted = new HashSet<>();

	public GniewPrzodkow(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location loc = p.getLocation();
		p.getWorld().playSound(loc, Sound.ENTITY_PLAYER_BIG_FALL, 1, 0.6f);
		
		new BukkitRunnable() {
			double maxRadius = dr.getObszar();
			double r = 0.25;
			@Override
			public void run() {
				if(r > maxRadius || !casterInCastWorld()) {
					cancel();
					return;
				}
				for(double theta = 0; theta < Math.PI * 2; theta += Math.PI / (r*2)) {
					double x = r*Math.sin(theta);
					double z = r*Math.cos(theta);
					Location tmp1 = loc.clone().add(x,0,z);
					
					for(int i = 0; i < 6; ++i) {
						x = rand.nextDouble(0.6) - 0.3;
						double y = rand.nextDouble(1.2) - 0.6;
						z = rand.nextDouble(0.6) - 0.3;
						Location tmp2 = tmp1.clone().add(x,y,z);
						p.getWorld().spawnParticle(Particle.SPELL_MOB, tmp2, 0, red, green, blue, 1);
					}
				}
				
				loc.getWorld().getNearbyEntities(loc, r, r, r, e -> {
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(shooted.contains(e))
						return false;
					if(e.getLocation().distance(loc) > r)
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
					if(!MythicBukkit.inst().getMobManager().isMythicMob(e)
							&& e.getType().equals(EntityType.ARMOR_STAND))
						return false;
					if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
						return false;
					return true;
				}).forEach(e -> {
					shooted.add(e);
					p.getWorld().playSound(e.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 0.5f);
					RuneDamage.damageNormal(p, (LivingEntity) e, dr);
				});
				
				r += 0.25;
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
