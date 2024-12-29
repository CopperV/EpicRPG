package me.Vark123.EpicRPG.OldRuneSystem.Runes;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.OldRuneSystem.ARune;
import me.Vark123.EpicRPG.OldRuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class CukierekAlboPsikus extends ARune {
	
	@Getter
	private static Map<Entity, Player> effected = new ConcurrentHashMap<>();
	private	PotionEffect effect;

	public CukierekAlboPsikus(ItemStackRune dr, Player p) {
		super(dr, p);
		effect = new PotionEffect(PotionEffectType.SLOWNESS, dr.getDurationTime()*20, 1);
	}

	@Override
	public void castSpell() {
		Location loc = p.getLocation().clone();
		loc.getWorld().playSound(loc, Sound.ENTITY_WITCH_CELEBRATE, 1, 0.75f);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime();
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(timer <= 0 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				--timer;
				
				RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
				double amount = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*0.05;		
				RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, amount);
				Bukkit.getPluginManager().callEvent(event);
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_BREATH, 0.2f, 0.8f);
				p.getWorld().spawnParticle(Particle.HEART, p.getLocation().add(0,1,0), 8, 0.6F, 0.9F, 0.6F, 0.2F);
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar(), e -> {
			if(e.equals(p) || !(e instanceof LivingEntity))
				return false;
			if(e instanceof Player) {
				RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
				ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
				State flag = set.queryValue(null, Flags.PVP);
				if(flag != null && flag.equals(State.ALLOW)
						&& !(e.getWorld().getName().toLowerCase().contains("dungeon") || e.getWorld().getName().toLowerCase().contains("raid")))
					return true;
				return false;
			}
			if(!MythicBukkit.inst().getMobManager().isMythicMob(e)
					&& e.getType().equals(EntityType.ARMOR_STAND))
				return false;
			if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
				return false;
			return true;
		}).forEach(this::applyCurse);
	}
	
	public void applyCurse(Entity e) {
		Random rand = new Random();
		LivingEntity le = (LivingEntity) e;
		le.addPotionEffect(effect);
		
		{
			Location loc = e.getLocation();
			loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1.2f, 0.8f);
			loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc.clone().add(0,1,0), 15, 0.6f, 0.25f, 0.6f, 0.13f);
		}
		
		effected.put(e, p);
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			@Override
			public void run() {
				if(isCancelled())
					return;
				if(!effected.containsKey(e) || !effected.get(e).equals(p)) {
					this.cancel();
					return;
				}
				if(timer <= 0 || !casterInCastWorld() || le.isDead()) {
					effected.remove(e);
					this.cancel();
					return;
				}
				--timer;
				
				Location loc = e.getLocation().clone().add(0,1,0);
				for(int i = 0; i < 12; ++i) {
					double speed = rand.nextDouble(0.2) + 0.05;
					double r = rand.nextDouble(0.5) + 0.25;
					double theta = rand.nextDouble(Math.PI*2);
					double y = rand.nextDouble(1.8) - 0.9;
					double x = r * Math.sin(theta);
					double z = r * Math.cos(theta);
					Location tmp = loc.clone().add(x,y,z);
					loc.getWorld().spawnParticle(Particle.SMOKE, tmp, 0, 0, 1, 0, speed);
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}

}
