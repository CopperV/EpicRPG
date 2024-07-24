package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class SwietyMrok extends ARune {
	
	private static final Random rand = new Random();

	public SwietyMrok(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location loc = p.getLocation().add(0, 1, 0);
		p.getWorld().playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 2, .65f);
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgStats stats = rpg.getStats();
		
		double dmg = stats.getFinalSila() * 2 + stats.getFinalWytrzymalosc() * 1.4;
		double r = dr.getObszar();
		loc.getWorld().getNearbyEntities(loc, r, r, r, e -> {
			if(e.getLocation().distanceSquared(loc) > (r*r))
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
			if(!MythicBukkit.inst().getMobManager().isMythicMob(e)
					&& e.getType().equals(EntityType.ARMOR_STAND))
				return false;
			if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
				return false;
			return true;
		}).forEach(e -> {
			RuneDamage.damageNormal(p, (LivingEntity) e, dr, dmg);
		});
		
		List<Location> targets = new LinkedList<>();
		for(int i = 0; i < 100; ++i) {
			double radius = rand.nextDouble(r);
			double angle = rand.nextDouble(Math.PI * 2);
			
			double x = Math.sin(angle) * radius;
			double y = rand.nextDouble(3.5) - 0.5;
			double z = Math.cos(angle) * radius;
			
			targets.add(loc.clone().add(x,y,z));
		}
		targets.forEach(target -> {
			Vector vec = new Vector(rand.nextDouble(2)-1, rand.nextDouble(2)-1, rand.nextDouble(2)-1).normalize();
			Vector vec2 = new Vector(rand.nextDouble(2)-1, rand.nextDouble(2)-1, rand.nextDouble(2)-1).normalize();
			p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, target, 0, vec.getX(), vec.getY(), vec.getZ(), rand.nextDouble(0.2)+0.1);
			p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, target, 0, vec2.getX(), vec2.getY(), vec2.getZ(), rand.nextDouble(0.2)+0.1);
		});
		
	}

}
