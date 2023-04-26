package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RPGFight.ManualDamage;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;

public class CienAssasyna extends ARune {

	public CienAssasyna(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		RpgPlayer rpg = Main.getListaRPG().get(p.getUniqueId().toString());
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
		rpg.setCienAssasyna(true);
		p.sendMessage("�7[�6EpicRPG�7] �aUzyles runy "+dr.getName());
		
		new BukkitRunnable() {
			
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar("�a�lCien assasyna�f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
				bar.setVisible(true);
				bar.addPlayer(p);
				bar.setProgress(timer/time);
			}
			
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					bar.removeAll();
					bar.setVisible(false);
					this.cancel();
					return;
				}
				
				bar.setTitle("�a�lCien assasyna�f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					p.sendMessage("�7[�6EpicRPG�7] �aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1.7f);
					rpg.setCienAssasyna(false);
					this.cancel();
					return;
				}
				
				Location loc = p.getLocation().add(0,1,0);
				p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 3, 0.3f, 0.3f, 0.3f, 0.1f);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}

	public static void spellEffect(Player player, Entity entity, double damage) {
		List<Entity> shooted = new ArrayList<>();
		shooted.add(entity);
		new BukkitRunnable() {
			List<Entity> last = new ArrayList<>(shooted);
			@Override
			public void run() {
				
				List<Entity> newTargets = new ArrayList<>();
				Map<Entity, Location> locations = new ConcurrentHashMap<>();
				for(Entity en : last) {
					List<Entity> tmp = en.getNearbyEntities(3, 3, 3);
					for(Entity e : tmp) {
						if(shooted.contains(e)) continue;
						if(newTargets.contains(e)) continue;
						if(e.equals(player)) continue;
						if(!(e instanceof LivingEntity)) continue;
						if(en instanceof Player || en.hasMetadata("NPC")) {
							Location loc = en.getLocation();
							RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
							ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(en.getLocation()));
							if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
								continue;
						}
						newTargets.add(e);
						locations.put(e, en.getLocation());
						shooted.add(e);
					}
				}
				
				if(newTargets.isEmpty()) {
					this.cancel();
					return;
				}
				
				last = new ArrayList<>(newTargets);
				for(Entity e : locations.keySet()) {
					
					if(e instanceof Player) {
						Player tmp = (Player) e;	
						if(tmp.getGameMode().equals(GameMode.SPECTATOR) || tmp.getGameMode().equals(GameMode.CREATIVE)) continue;
					}
					
					EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, e, DamageCause.CONTACT, damage);
					Bukkit.getPluginManager().callEvent(event);
					if(event.isCancelled()) {
						continue;
					}
					ManualDamage.doDamage(player, (LivingEntity)e, damage, event);
					drawLine(e.getLocation(), locations.get(e));
//					EntityContactDamage.damage(e, player, damage, DamageCause.CONTACT);
					e.getWorld().spawnParticle(Particle.SMOKE_NORMAL, e.getLocation(), 15, 0.4f, 0.4f, 0.4f, 0.15f);
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}
	
	private static void drawLine(Location loc1, Location loc2) {
		double space = 0.4;
//		Vector p1 = loc1.getDirection();
//		Vector p2 = loc2.getDirection();
		Vector p1 = new Vector(loc1.getX(), loc1.getY(), loc1.getZ());
		Vector p2 = new Vector(loc2.getX(), loc2.getY(), loc2.getZ());
		double distance = loc1.distance(loc2);
		Vector vec = p2.clone().subtract(p1).normalize().multiply(space);
		for(double length = 0; length < distance; p1.add(vec), length += space) {
			loc1.getWorld().spawnParticle(Particle.SMOKE_NORMAL, p1.getX(), p1.getY(), p1.getZ() , 3,0.1F,0.1F,0.1F,0.01F);
		}
	}
	
}
