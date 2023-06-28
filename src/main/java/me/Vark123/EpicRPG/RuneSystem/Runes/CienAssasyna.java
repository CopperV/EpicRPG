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
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class CienAssasyna extends ARune {

	public CienAssasyna(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
		rpg.getModifiers().setCienAssasyna(true);
		p.sendMessage("§7[§6EpicRPG§7] §aUzyles runy "+dr.getName());
		
		new BukkitRunnable() {
			
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar("§a§lCien assasyna§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
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
				
				bar.setTitle("§a§lCien assasyna§f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					p.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1.7f);
					rpg.getModifiers().setCienAssasyna(false);
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
				
				last.stream().forEach(en -> {
					en.getNearbyEntities(3, 3, 3).stream().filter(e -> {
						if(shooted.contains(e) || last.contains(e) || newTargets.contains(e))
							return false;
						if(!(e instanceof LivingEntity))
							return false;
						if(e.equals(player))
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
						newTargets.add(e);
						locations.put(e, en.getLocation());
						shooted.add(e);
					});
				});
				
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
					//TODO
//					ManualDamage.doDamage(player, (LivingEntity)e, damage, event);
					drawLine(e.getLocation(), locations.get(e));
					e.getWorld().spawnParticle(Particle.SMOKE_NORMAL, e.getLocation(), 15, 0.4f, 0.4f, 0.4f, 0.15f);
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}
	
	private static void drawLine(Location loc1, Location loc2) {
		double space = 0.4;
		Vector p1 = new Vector(loc1.getX(), loc1.getY(), loc1.getZ());
		Vector p2 = new Vector(loc2.getX(), loc2.getY(), loc2.getZ());
		double distance = loc1.distance(loc2);
		Vector vec = p2.clone().subtract(p1).normalize().multiply(space);
		for(double length = 0; length < distance; p1.add(vec), length += space) {
			loc1.getWorld().spawnParticle(Particle.SMOKE_NORMAL, p1.getX(), p1.getY(), p1.getZ() , 3,0.1F,0.1F,0.1F,0.01F);
		}
	}
	
}
