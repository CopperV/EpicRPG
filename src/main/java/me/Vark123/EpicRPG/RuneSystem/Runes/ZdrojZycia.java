package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class ZdrojZycia extends ARune {

	public ZdrojZycia(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location loc = p.getLocation().clone().add(0,0.2d,0);
		loc.getWorld().playSound(loc, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime();
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				
				Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar());
				entities.stream().filter(e -> {
					if(!(e instanceof Player))
						return false;
					RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
					ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
					State flag = set.queryValue(null, Flags.PVP);
					if(flag != null && flag.equals(State.ALLOW)
							&& !e.getWorld().getName().toLowerCase().contains("dungeon"))
						return false;
					return true;
				}).forEach(e -> {
					Player tmp = (Player) e;
					RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(tmp);
					double amount = tmp.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*0.02;		
					RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, amount);
					Bukkit.getPluginManager().callEvent(event);
					tmp.getWorld().spawnParticle(Particle.HEART, tmp.getLocation().add(0,1,0),5,0.3F,0.3F,0.3F,0.1F);
				});
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
								
				for(double theta = 0; theta <= (Math.PI*2); theta = theta + (Math.PI/(dr.getObszar()*2))) {
					double x = dr.getObszar()*Math.sin(theta);
					double z = dr.getObszar()*Math.cos(theta);
					Location tmp = new Location(loc.getWorld(), loc.getX()+x, loc.getY(), loc.getZ()+z);
					p.getWorld().spawnParticle(Particle.HEART, tmp, 2, 0.15F, 0.15F, 0.15F, 0.1F);
				}
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
		
	}

}
