package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
import me.Vark123.EpicRPG.Healing.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.RuneDamage;
import net.minecraft.world.phys.AxisAlignedBB;

public class Wyssanie extends ARune{

	public Wyssanie(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation();
			Vector vec = loc.getDirection().normalize();
			public void run() {
				t++;
				double x = vec.getX()*t;
				double y = vec.getY()*t+1.5;
				double z = vec.getZ()*t;
				loc.add(x,y,z);
				p.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, loc, 1, 0, 0, 0, 0);
				p.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 1, 0,0,0,0);
				
				for(Entity e:loc.getWorld().getNearbyEntities(loc, 10, 10, 10)) {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.75, loc.getY()-0.75, loc.getZ()-0.75, loc.getX()+0.75, loc.getY()+0.75, loc.getZ()+0.75);
					if(aabb.c(aabb2)) {
						if(!e.equals(p) && e instanceof LivingEntity) {
							if(e instanceof Player || e.hasMetadata("NPC")) {
								RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
								ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
								if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
									continue;
							}
							spellEffect(e);
							this.cancel();
							return;
						}
					}
				}
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
//				if(!loc.getBlock().getType().equals(Material.AIR)) {
					this.cancel();
					return;
				}
				loc.subtract(x, y, z);
				if(t>30 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

	private void spellEffect(Entity e) {
		if(e instanceof Player || e.hasMetadata("NPC")) {
			Location loc = e.getLocation();
			RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
			ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
			if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
				return;
		}
		RpgPlayer rpg = Main.getListaRPG().get(p.getUniqueId().toString());
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_CAT_HISS, 1, 0.9f);
		double amount = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*0.07*rpg.getKrag();
		if(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() > (amount+p.getHealth())) {
//			p.setHealth((int)(p.getHealth()+p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*0.05*rpg.getKrag()));
			p.sendMessage("§7[§6EpicRPG§7] §6Uleczyles sie w "+(7*rpg.getKrag())+"%");
		}else {
//			p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			p.sendMessage("§7[§6EpicRPG§7] §6Uleczyles sie w pelni");
		}
		
		RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, amount);
		Bukkit.getPluginManager().callEvent(event);
		
		RuneDamage.damageNormal(p, (LivingEntity)e, dr);
		new BukkitRunnable() {
			double y = 0;
			Location loc = p.getLocation();
			Location loc2 = e.getLocation();
			@Override
			public void run() {
				y+=0.1;
				for(double theta = 0;theta <= 2*Math.PI; theta=theta+(Math.PI/8)) {
					double x = y*Math.sin(theta);
					double z = y*Math.cos(theta);
					loc.add(x, y, z);
					loc2.add(x, y, z);
					p.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, loc2, 1, 0, 0, 0, 0);
					p.getWorld().spawnParticle(Particle.HEART, loc, 1, 0, 0, 0, 0);
					loc2.subtract(x, y, z);
					loc.subtract(x, y, z);
				}
				if(y>=1.5 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}
	
}
