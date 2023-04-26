package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;

public class BlogoslawionaZiemia extends ARune {

	public BlogoslawionaZiemia(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location loc = p.getLocation().clone().add(0,0.2d,0);
		loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1.5f, 1.2f);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime();
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
				Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar());
				for(Entity e : entities) {
					if(!(e instanceof Player)) continue;
					Player tmp = (Player) e;
					
					if(!tmp.equals(p)) {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(tmp.getLocation()));
						if(set.queryValue(null, Flags.PVP).equals(StateFlag.State.ALLOW)) {
							continue;
						}
					}
					
					RpgPlayer rpg = Main.getListaRPG().get(tmp.getUniqueId().toString());
					int amount = (int) (rpg.getFinalmana()*0.07);
					
					rpg.addPresentManaSmart(amount);

					tmp.getWorld().spawnParticle(Particle.END_ROD, tmp.getLocation().add(0,1,0),5,0.3F,0.3F,0.3F,0.1F);
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*2;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
				
				for(double t = 1; t <= dr.getObszar(); t+=0.5) {
					for(double theta = 0; theta < (Math.PI*2); theta += ((Math.PI*2)/(3*t))) {
						double x = t*Math.sin(theta);
						double z = t*Math.cos(theta);
						Location tmp = new Location(loc.getWorld(), loc.getX()+x, loc.getY(), loc.getZ()+z);
						p.getWorld().spawnParticle(Particle.END_ROD, tmp, 2, 0.05F, 0.05F, 0.05F, 0.02F);
					}
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 10);

	}

}
