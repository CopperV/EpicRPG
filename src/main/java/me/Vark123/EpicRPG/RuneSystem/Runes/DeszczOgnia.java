package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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
import me.Vark123.EpicRPG.RuneSystem.RuneDamage;
import me.Vark123.EpicRPG.RuneSystem.ARunesTimerCheck;

public class DeszczOgnia extends ARune{

	List<Entity> entitiesList = new ArrayList<>();
	
	public DeszczOgnia(ItemStackRune dr, Player p) {
		super(dr, p);
		this.modifier2 = true;
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 10, 1);
//		worldBorder();
		new BukkitRunnable() {
			double t = 0;
//			double dmg = dr.getDamage();
//			RpgPlayer rpg = Main.getListaRPG().get(p.getUniqueId().toString());
			LivingEntity le;
			@Override
			public void run() {
				Location loc = p.getLocation();
				t++;
				double x = loc.getX()+Math.random()*2*dr.getObszar()-dr.getObszar();
				double z = loc.getZ()+Math.random()*2*dr.getObszar()-dr.getObszar();
				Location target = new Location(loc.getWorld(), x, loc.getY(), z);
				spellEffect(target.clone());
				x = loc.getX()+Math.random()*2*dr.getObszar()-dr.getObszar();
				z = loc.getZ()+Math.random()*2*dr.getObszar()-dr.getObszar();
				target = new Location(loc.getWorld(), x, loc.getY(), z);
				spellEffect(target.clone());
				x = loc.getX()+Math.random()*2*dr.getObszar()-dr.getObszar();
				z = loc.getZ()+Math.random()*2*dr.getObszar()-dr.getObszar();
				target = new Location(loc.getWorld(), x, loc.getY(), z);
				spellEffect(target.clone());
				
				Collection<Entity> tmpList = loc.getWorld().getNearbyEntities(loc,dr.getObszar(), dr.getObszar(), dr.getObszar());
				for(Entity en : tmpList) {
					if(entitiesList.contains(en)) continue;
					entitiesList.add(en);
					if(!en.equals(p) && en instanceof LivingEntity) {
						if(en instanceof Player || en.hasMetadata("NPC")) {
							RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
							ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(en.getLocation()));
							if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
								continue;
						}
//						if(rpg.hasInkantacja()) dmg *= 1.3;
						le = (LivingEntity) en;
						RuneDamage.damageNormal(p, le, dr, (p, le, dr) -> {
							new BukkitRunnable() {
								int timer = 20;
								double dmg = dr.getDamage()/50.0;
								@Override
								public void run() {
									if(timer <= 0 || !casterInCastWorld() || !entityInCastWorld(en)) {
										this.cancel();
										return;
									}
									--timer;
									boolean end = RuneDamage.damageTiming(p, le, dr, dmg);
									if(!end) {
										this.cancel();
										return;
									}
									Location loc = le.getLocation().add(0,1,0);
									p.getWorld().spawnParticle(Particle.FLAME, loc,10,0.2,0.2,0.2,0.05);
									p.getWorld().playSound(loc, Sound.ENTITY_GENERIC_BURN, 1, 1);
								}
							}.runTaskTimer(Main.getInstance(), 0, 20);
						});
//						if(!(en instanceof ArmorStand))
//							((LivingEntity)en).setFireTicks(20*60);
					}
				}
				
				if(t>30 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 10);
		
		RunesTimerCheck.getObszarowkiCd().put(p.getUniqueId(), new Date());
		
	}
	
	private void spellEffect(Location target) {
		target.add(0, 20, 0);
		new BukkitRunnable() {
			double t = 20;
			@Override
			public void run() {
				target.add(0, t, 0);
				p.getWorld().spawnParticle(Particle.FLAME, target, 10, 0.2F, 0.2F, 0.2F, 0.05F);

				target.subtract(0, t, 0);
				t--;
				if(t<(-50) || !casterInCastWorld()) {
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
//	@SuppressWarnings("unused")
//	private void worldBorder() {
//		List<Entity> lista = p.getNearbyEntities(dr.getObszar(), dr.getObszar(), dr.getObszar());
//		lista.add(p);
//		WorldBorder wb = new WorldBorder();
//		for(Entity e:lista) {
//			if(e instanceof Player) {
//				Player p2 = (Player)e;
//				wb.setCenter(p2.getLocation().getX()+10_000, p2.getLocation().getZ()+10_000);
//				wb.setSize(1);
//				PacketPlayOutWorldBorder border = new PacketPlayOutWorldBorder(wb, EnumWorldBorderAction.INITIALIZE);
//				((CraftPlayer)p2).getHandle().playerConnection.sendPacket(border);
//				new BukkitRunnable() {
//					
//					@Override
//					public void run() {
//						WorldBorder ww = new WorldBorder();
//						ww.setSize(30_000_000);
//						ww.setCenter(p2.getLocation().getX(), p2.getLocation().getZ());
//						PacketPlayOutWorldBorder border = new PacketPlayOutWorldBorder(ww, EnumWorldBorderAction.INITIALIZE);
//						((CraftPlayer)p2).getHandle().playerConnection.sendPacket(border);
//					}
//				}.runTaskLater(Main.getInstance(), 20*15);
//			}
//		}
//	}

}
