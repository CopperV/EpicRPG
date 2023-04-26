package me.Vark123.EpicRPG.RuneSystem.Runes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.mythic.api.adapters.AbstractPlayer;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.ARune;

public class Prowokacja extends ARune {
	
	private static Map<Entity,Player> targets = new ConcurrentHashMap<>();

	public Prowokacja(ItemStackRune dr, Player p) {
		super(dr, p);
		this.modifier1 = true;
	}

	@Override
	public void castSpell() {
		RpgPlayer rpg = Main.getListaRPG().get(p.getUniqueId().toString());
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 0.8f);
		rpg.setProwokacja(true);
		rpg.setModifier1_lock(true);
		p.sendMessage("§7[§6EpicRPG§7] §aUzyles runy "+dr.getName());
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*dr.getDurationTime(), 0));
		
		List<Entity> tmpTargets = new ArrayList<>();
		List<Entity> entities = p.getNearbyEntities(dr.getObszar(), dr.getObszar(), dr.getObszar());
		for(Entity entity : entities) {
			if(entity instanceof Player) continue;
			if(!(entity instanceof LivingEntity)) continue;
			if(entity.hasMetadata("NPC")) continue;
			
			tmpTargets.add(entity);
			targets.put(entity, p);
			
			ActiveMob mob = MythicBukkit.inst().getAPIHelper().getMythicMobInstance(entity);
			if(mob == null) continue;
//			Bukkit.broadcastMessage(mob.getDisplayName());
			AbstractPlayer aPlayer = BukkitAdapter.adapt(p);
			if(mob.hasThreatTable()) {
				mob.getThreatTable().Taunt(aPlayer);
				mob.getThreatTable().targetHighestThreat();
				continue;
			}
			MythicMob mMob = mob.getType();
			boolean canTargeting = false;
			List<String> AI = mMob.getAIGoalSelectors();
			List<String> AI2 = mMob.getAITargetSelectors();
			if((AI == null || AI.isEmpty()) && (AI2 == null || AI2.isEmpty())) {
				if(entity instanceof Monster)
					canTargeting = true;
			} else {
				if(AI2 == null || AI2.isEmpty() || AI2.contains("players") || AI2.contains("attacker"))
					for(String s : mMob.getAIGoalSelectors()) {
						if(s.contains("meleeattack") 
								|| s.contains("arrowattack") 
								|| s.contains("spiderattack")
								|| s.contains("rangedattack")
								|| s.contains("bowattack")
								|| s.contains("bowshoot")
								|| s.contains("bowmaster")
								|| s.contains("crossbowAttack")) {
							canTargeting = true;
							break;
						}
					}
			}
			if(!canTargeting) continue;
			mob.setTarget(aPlayer);
			continue;
		}
		
		new BukkitRunnable() {
			
			double time = dr.getDurationTime();
			double timer = dr.getDurationTime();
			BossBar bar = Bukkit.createBossBar("§9§lProwokacja§f: "+(int)timer+" sekund", BarColor.BLUE, BarStyle.SEGMENTED_12);{
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
					rpg.setProwokacja(false);
					rpg.setModifier1_lock(false);
					for(Entity entity : tmpTargets) {
						if(targets.containsKey(entity)) targets.remove(entity);
					}
					tmpTargets.clear();
					p.sendMessage("§7[§6EpicRPG§7] §aEfekt dzialania runy "+dr.getName()+" skonczyl sie");
					
					this.cancel();
					return;
				}
				
				bar.setTitle("§9§lProwokacja§f: "+(int)timer+" sekund");
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

	public static Map<Entity, Player> getTargets() {
		return targets;
	}
	
}
