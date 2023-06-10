package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.BlockData;
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

public class ZyciodajnaZiemia_M extends ARune {
	
	List<Player> effected;
	private static List<Player> debuff = new ArrayList<>();
	List<Player> debuff_tmp = new ArrayList<>();

	public ZyciodajnaZiemia_M(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		effected = new ArrayList<>();
		debuff_tmp = new ArrayList<>();
		Location loc = p.getLocation().clone().add(0,0.2d,0);
		loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 2, 1.4f);
		BlockData data = Bukkit.createBlockData(Material.GRASS_BLOCK);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					effected.stream().filter(tmp -> {
						return tmp.isOnline();
					}).forEach(tmp -> {
						RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(tmp);
						if(rpg.getModifiers().hasZyciodajnaZiemia_m()) 
							rpg.getModifiers().setZyciodajnaZiemia_m(false);
					});
					debuff.addAll(debuff_tmp);
					effected.clear();
					this.cancel();
					return;
				}

				List<Player> tmp = new ArrayList<>(effected);
				tmp.stream().filter(temp -> {
					if(temp.getWorld().getName().equalsIgnoreCase(loc.getWorld().getName())
							&& temp.getLocation().distance(loc) <= dr.getObszar())
						return false;
					return true;
				}).forEach(temp -> {
					effected.remove(temp);
					RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(temp);
					if(rpg.getModifiers().hasZyciodajnaZiemia_m()) 
						rpg.getModifiers().setZyciodajnaZiemia_m(false);
				});
				
				Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, dr.getObszar(), dr.getObszar(), dr.getObszar());
				entities.stream().filter(e -> {
					if(!(e instanceof Player))
						return false;
					Player temp = (Player) e;
					if(debuff.contains(temp))
						return false;
					if(effected.contains(temp))
						return false;
					if(!temp.equals(p)) {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
						State flag = set.queryValue(null, Flags.PVP);
						if(flag != null && flag.equals(State.ALLOW)
								&& !e.getWorld().getName().toLowerCase().contains("dungeon"))
							return true;
						return false;
					}
					return true;
				}).forEach(e -> {
					Player temp = (Player) e;
					if(!debuff_tmp.contains(temp))
						debuff_tmp.add(temp);
					RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(temp);
					rpg.getModifiers().setZyciodajnaZiemia_m(true);
				});
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
		
		new BukkitRunnable() {
			DustOptions dust = new DustOptions(Color.RED, 1);
			@Override
			public void run() {
				debuff_tmp.stream().forEach(tmp -> {
					if(debuff.contains(tmp))
						debuff.remove(tmp);
					if(!tmp.isOnline())
						return;
					tmp.sendMessage("§7[§6EpicRPG§7] §aDebuff runy "+dr.getName()+" skonczyl sie");
					tmp.playSound(p.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 5, 1.5f);
					tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp.getLocation(), 10, 0.5f, 0.5f, 0.5f, 0.1f,dust);
				});
			}
		}.runTaskLater(Main.getInstance(), 60*10*20);
		
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
					Player temp = (Player) e;
					if(debuff.contains(temp))
						return false;
					if(!temp.equals(p)) {
						RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
						ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
						State flag = set.queryValue(null, Flags.PVP);
						if(flag != null && flag.equals(State.ALLOW)
								&& !e.getWorld().getName().toLowerCase().contains("dungeon"))
							return true;
						return false;
					}
					return true;
				}).forEach(e -> {
					Player tmp = (Player) e;
					RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(tmp);
					double hpAmount = tmp.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*0.1;
					int manaAmount = (int) (rpg.getStats().getFinalMana()*0.1);
					
					RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, hpAmount);
					Bukkit.getPluginManager().callEvent(event);
					
					rpg.getStats().addPresentManaSmart(manaAmount);
					
					tmp.getWorld().spawnParticle(Particle.HEART, tmp.getLocation().add(0,1,0),2,0.3F,0.3F,0.3F,0.1F);
					tmp.getWorld().spawnParticle(Particle.END_ROD, tmp.getLocation().add(0,1,0),2,0.3F,0.3F,0.3F,0.1F);
					tmp.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp.getLocation().add(0,1,0),2,0.3F,0.3F,0.3F,0.1F,data);
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
					p.getWorld().spawnParticle(Particle.BLOCK_CRACK, tmp, 3, 0.2F, 0.2F, 0.2F, 0.15F, data);
				}
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}

}
