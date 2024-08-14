package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.Random;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;
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
import me.Vark123.EpicOptions.Main;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class MagicznaSfera extends ARune {
	
	private static final Random rand = new Random();
	private static final DustOptions dust = new DustOptions(Color.fromRGB(157, 0, 255), 2.5f);

	public MagicznaSfera(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location loc = p.getLocation().clone().add(0,1.5,0);
		p.getLocation().getWorld().playSound(loc, Sound.ENTITY_EVOKER_PREPARE_ATTACK, 1.1f, 1.2f);
		p.getWorld().spawnParticle(Particle.REVERSE_PORTAL, loc, 100, 0.5, 0.5F, 0.5F, 3);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			MutableDouble radius = new MutableDouble(dr.getObszar());
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(timer <= 0 || radius.getValue() < 1.5 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
				
				loc.getWorld().getNearbyEntities(loc, radius.getValue(), radius.getValue(), radius.getValue(), e -> {
					if(e.equals(p) || !(e instanceof LivingEntity))
						return false;
					if(e.getLocation().distanceSquared(loc) > radius.getValue()*radius.getValue())
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
					if(radius.getValue() < 1.5)
						return;
					RuneDamage.damageNormal(p, (LivingEntity) e, dr);
					p.getWorld().spawnParticle(Particle.SPELL_WITCH, e.getLocation().clone().add(0, 1, 0), 8, 0.3, 0.8, 0.3, 0.07);
					p.getWorld().playSound(loc, Sound.ENTITY_EVOKER_CAST_SPELL, 0.5f, 1.3f);
					radius.subtract(.15);
				});
				
				if(radius.getValue() < 1.5) {
					this.cancel();
					return;
				}
				
				double tmpRadius = radius.getValue();
				for(int i = 0; i <= Math.ceil(tmpRadius*tmpRadius)*3; ++i) {
					double angle1 = rand.nextDouble(2*Math.PI);
					double angle2 = rand.nextDouble(2*Math.PI);
					
					double x = tmpRadius * Math.cos(angle1)*Math.cos(angle2);
					double y = tmpRadius * Math.sin(angle2);
					double z = tmpRadius * Math.sin(angle1)*Math.cos(angle2);
					
					Location tmpLoc = loc.clone().add(x,y,z);
					p.getWorld().spawnParticle(Particle.REDSTONE, tmpLoc, 1, .05, .05, .05, .02, dust);
				}
				
			}
		}.runTaskTimer(Main.getInst(), 0, 5);
	}

}
