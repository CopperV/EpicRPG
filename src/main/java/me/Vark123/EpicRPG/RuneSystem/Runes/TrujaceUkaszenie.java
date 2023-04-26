package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.RuneDamage;
import net.minecraft.world.phys.AxisAlignedBB;

public class TrujaceUkaszenie extends ARune {
	
	private LivingEntity le;

	public TrujaceUkaszenie(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EVOKER_FANGS_ATTACK, 1.1f, 1.25f);
		new BukkitRunnable() {
			double t = 0;
			Location loc = p.getLocation().add(0,1.25,0);
			Vector vec = loc.getDirection().normalize().multiply(0.85);
			DustOptions dust = new DustOptions(Color.fromRGB(0, 140, 0), 0.85f);
			@Override
			public void run() {
				if(t >= 50 || !casterInCastWorld()) {
					this.cancel();
					return;
				}
				++t;
				loc.add(vec);
				p.getWorld().spawnParticle(Particle.REDSTONE, loc, 14, 0.15f, 0.15f, 0.15f, 0.05f, dust);
				
				for(Entity e : loc.getWorld().getNearbyEntities(loc, 3, 3, 3)) {
					AxisAlignedBB aabb = ((CraftEntity)e).getHandle().cw();
					AxisAlignedBB aabb2 = new AxisAlignedBB(loc.getX()-0.75, loc.getY()-1.5, loc.getZ()-0.75, loc.getX()+0.75, loc.getY()+1.5, loc.getZ()+0.75);
					if(aabb.c(aabb2)) {
						if(!e.equals(p) && e instanceof LivingEntity) {
							if(e instanceof Player || e.hasMetadata("NPC")) {
								RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
								ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(e.getLocation()));
								if(set.queryValue(null, Flags.PVP) == null || set.queryValue(null, Flags.PVP).equals(StateFlag.State.DENY) || loc.getWorld().getName().toLowerCase().contains("dungeon"))
									continue;
							}
							le = (LivingEntity) e;
							RuneDamage.damageNormal(p, le, dr, (p, le, dr) ->{
								le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 1));
								double dmg = dr.getDamage()/5;
								new BukkitRunnable() {
									int timer = dr.getDurationTime();
									@Override
									public void run() {
										if(timer <= 0 || !casterInCastWorld() || !entityInCastWorld(e)) {
											this.cancel();
											return;
										}
										--timer;
										boolean end = RuneDamage.damageTiming(p, le, dr, dmg);
										if(!end) {
											this.cancel();
											return;
										}
										p.getWorld().spawnParticle(Particle.REDSTONE, le.getLocation(), 14, 0.3f, 0.4f, 0.3f, 0.05f, dust);
									}
								}.runTaskTimer(Main.getInstance(), 0, 20);
							});
							p.getWorld().spawnParticle(Particle.REDSTONE, loc, 30, 0.6f, 0.6f, 0.6f, 0.1f, dust);
							p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BEE_STING, 0.9f, 0.5f);
							this.cancel();
							return;
						}
					}
				}
				
				if(loc.getBlock().getType().isSolid() && !loc.getBlock().isLiquid()) {
					this.cancel();
					return;
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	}

}
