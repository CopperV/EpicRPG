package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.mythic.api.adapters.AbstractPlayer;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.utils.lib.lang3.mutable.MutableBoolean;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;

public class Zryw extends ARune {

	public Zryw(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 0.8f);
		rpg.getModifiers().setZryw(true);
		p.sendMessage("§7[§6EpicRPG§7] §aUzyles runy "+dr.getName());
		
		List<Entity> tmpTargets = new ArrayList<>();
		List<Entity> entities = p.getNearbyEntities(dr.getObszar(), dr.getObszar(), dr.getObszar());

		AbstractPlayer aPlayer = BukkitAdapter.adapt(p);
		entities.stream().filter(e -> {
			if(e instanceof Player || !(e instanceof LivingEntity))
				return false;
			if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(e).isDamageable())
				return false;
			ActiveMob mob = MythicBukkit.inst().getAPIHelper().getMythicMobInstance(e);
			if(mob == null) 
				return false;
			return true;
		}).forEach(e -> {
			ActiveMob mob = MythicBukkit.inst().getAPIHelper().getMythicMobInstance(e);
			if(mob.hasThreatTable()) {
				mob.getThreatTable().Taunt(aPlayer);
				mob.getThreatTable().targetHighestThreat();
				tmpTargets.add(e);
				Prowokacja.getTargets().put(e, p);
				return;
			}
			MythicMob mMob = mob.getType();
			MutableBoolean canTargeting = new MutableBoolean(false);
			List<String> AI = mMob.getAIGoalSelectors();
			List<String> AI2 = mMob.getAITargetSelectors();
			if((AI == null || AI.isEmpty()) && (AI2 == null || AI2.isEmpty())) {
				if(BukkitAdapter.adapt(e).isMonster())
					canTargeting.setValue(true);
			} else {
				if(AI2 == null || AI2.isEmpty() || AI2.contains("players")) {
					AI.stream().filter(s -> {
						if(s.contains("meleeattack") 
								|| s.contains("arrowattack") 
								|| s.contains("spiderattack")
								|| s.contains("rangedattack")
								|| s.contains("bowattack")
								|| s.contains("bowshoot")
								|| s.contains("bowmaster")
								|| s.contains("crossbowAttack")) {
							return true;
						}
						return false;
					}).findAny().ifPresent(s -> {
						canTargeting.setValue(true);
					});
				}
			}
			
			if(!canTargeting.booleanValue())
				return;
			mob.setTarget(aPlayer);
			tmpTargets.add(e);
			Prowokacja.getTargets().put(e, p);
		});
		
		new BukkitRunnable() {
			
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar("§3Zryw§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
				bar.setVisible(true);
				bar.addPlayer(p);
				bar.setProgress(timer/time);
			}
			
			@Override
			public void run() {
				if(timer <= 0 || !casterInCastWorld()) {
					bar.removeAll();
					bar.setVisible(false);
					
					p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1, 0.8f);
					p.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, p.getLocation().add(0,1,0), 15, 0.5f, 0.5f, 0.5f, 0.1f);
					rpg.getModifiers().setZryw(false);
					tmpTargets.stream().filter(e -> {
						return Prowokacja.getTargets().containsKey(e) && Prowokacja.getTargets().get(e).equals(p);
					}).forEach(e -> {
						if(Prowokacja.getTargets().containsKey(e)) Prowokacja.getTargets().remove(e);
					});
					tmpTargets.clear();
					p.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
					
					this.cancel();
					return;
				}
				
				bar.setTitle("§3Zryw§f: "+(int)timer+" sekund");
				bar.setProgress(timer/time);
				
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
				
				Location loc = p.getLocation().add(0,1,0);
				p.getWorld().spawnParticle(Particle.SNEEZE, loc, 5, 0.5f, 0.5f, 0.5f, 0.1f);
				
				--timer;
			}
		}.runTaskTimer(Main.getInstance(), 0, 5);
	}

}
