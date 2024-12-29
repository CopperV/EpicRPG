package me.Vark123.EpicRPG.OldRuneSystem.Runes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
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
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.OldRuneSystem.ARune;
import me.Vark123.EpicRPG.OldRuneSystem.ItemStackRune;

public class LodowaAura extends ARune {

	private static final int BASE_VALUE = 45;
	
	public LodowaAura(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		int size = p.getWorld().getNearbyEntities(p.getLocation(), dr.getObszar(), 8, dr.getObszar(), e -> {
			if(e.equals(p) || !(e instanceof LivingEntity))
				return false;
			if(e.getLocation().distanceSquared(p.getLocation()) > dr.getObszar() * dr.getObszar())
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
		}).size();
		
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 10, 0.5f);
		double amount = Math.max(BASE_VALUE, size * BASE_VALUE);
		p.setAbsorptionAmount(p.getAbsorptionAmount()+amount);
		double startAmount = p.getAbsorptionAmount();
		
		new BukkitRunnable() {
			
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar(dr.getName()+"§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
				bar.setVisible(true);
				bar.addPlayer(p);
				bar.setProgress(timer/time);
			}
			
			@Override
			public void run() {				
				if(timer <= 0 || !casterInCastWorld() || startAmount - p.getAbsorptionAmount() >= amount) {
					bar.removeAll();
					bar.setVisible(false);

					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 2, 0.5f);
					p.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, p.getLocation().add(0,1,0), 15, 0.75f, 1f, 0.75f, 0.1f);
					p.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
					
					if(p.getAbsorptionAmount() < amount)
						p.setAbsorptionAmount(0);
					else
						p.setAbsorptionAmount(p.getAbsorptionAmount() - amount);
					
					this.cancel();
					return;
				}
				
				bar.setTitle(dr.getName()+"§f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		new BukkitRunnable() {
			int timer = dr.getDurationTime()*4;
			
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld() || startAmount - p.getAbsorptionAmount() >= amount) {
					this.cancel();
					return;
				}
				Location loc = p.getLocation().add(0,1,0);
				p.getWorld().spawnParticle(Particle.WAX_OFF, loc, 12, 0.6f, 1f, 0.6f, 0.12f);
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}

}
