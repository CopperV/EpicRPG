package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
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

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;

public class EksplozjaLodu extends ARune {

	public EksplozjaLodu(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location loc = p.getLocation().add(0,1,0);
		loc.getWorld().playSound(loc, Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1.5f, 1.5F);

		Collection<Entity> tmpList = loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar());
		
		tmpList.stream().filter(e -> {
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
		}).forEach(e -> {
			loc.getWorld().playSound(e.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 0.8f, 0.5F);
			RuneDamage.damageNormal(p, (LivingEntity)e, dr);
		});
		
		List<Vector> directions = new LinkedList<>();
		double r = 2;
		double x,y,z,theta;
		for(int i = 0; i < 20; ++i) {
			theta = Math.random()*Math.PI*2;
			x = r * Math.sin(theta);
			y = Math.random()*2.5+0.1;
			z = r * Math.cos(theta);
			directions.add(new Vector(x, y, z));
		}
		
		for(Vector vec : directions)
			spellEffect(loc.clone(), vec);
	}

	private void spellEffect(Location loc, Vector vec) {
		new BukkitRunnable() {
			int timer = 0;
			@Override
			public void run() {
				if(timer >= 20) {
					this.cancel();
					return;
				}
				p.getWorld().spawnParticle(Particle.END_ROD, loc, 4, 0.06f, 0.06f, 0.06f, 0.01f);
				p.getWorld().spawnParticle(Particle.SNOWBALL, loc, 3, 0.06f, 0.06f, 0.06f, 0.01f);
				loc.add(vec);
				++timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
}
