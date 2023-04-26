package me.Vark123.EpicRPG.FightSystem;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import me.Vark123.EpicRPG.EpicRPGMobManager;
import me.Vark123.EpicRPG.Core.ExpSystem;
import me.Vark123.EpicRPG.Core.StygiaSystem;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgSkills;

public class RpgDeathEvent implements Listener {

	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		LivingEntity victim = e.getEntity();
		Player killer = e.getEntity().getKiller();
		
		if(killer == null)
			return;
		
		if(!PlayerManager.getInstance().playerExists(killer))
			return;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(killer);
		RpgSkills skills = rpg.getSkills();
		RpgModifiers modifiers = rpg.getModifiers();

		String name = victim.getName();
		
		EpicRPGMobManager.getInstance().executeCommands(killer, name);
		int xp = EpicRPGMobManager.getInstance().getRandomMobExp(name);
		if(xp > 0) {
			ExpSystem.getInstance().addMobExp(rpg, xp);
			StygiaSystem.getInstance().addMobStygia(rpg, xp);
		}
		
		//TODO
//		if(skills.hasRozprucie()) {
//			Random rand = new Random();
//			if(rand.nextInt(5) == 0) {
//				
//				victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_PLAYER_BIG_FALL, 1, 0.75f);
//				DustOptions dust = new DustOptions(Color.fromRGB(128, 0, 0), 0.4f);
//				
//				List<Vector> directions = new LinkedList<>();
//				double r = 2;
//				double x,y,z,theta;
//				for(int i = 0; i < 20; ++i) {
//					theta = Math.random()*Math.PI*2;
//					x = r * Math.sin(theta);
//					y = Math.random()*2.5+0.1;
//					z = r * Math.cos(theta);
//					directions.add(new Vector(x, y, z).normalize().multiply(0.25));
//				}
//				for(Vector vec : directions) {
//					new BukkitRunnable() {
//						Vector clone = vec.clone();
//						int timer = 0;
//						@Override
//						public void run() {
//							if(timer >= 20) {
//								this.cancel();
//								return;
//							}
//							killer.getWorld().spawnParticle(Particle.REDSTONE, victim.getLocation().clone().add(0,1,0).add(clone), 4, 0.06f, 0.06f, 0.06f, 0.01f, dust);
//							clone.add(vec);
//							++timer;
//						}
//					}.runTaskTimer(Main.getInstance(), 0, 1);
//				}
//				
//				double dmg = victim.getLastDamageCause().getDamage() * 0.3;
//				List<Entity> set = victim.getNearbyEntities(5, 5, 5);
//				for(Entity entity : set) {
//					if(!(entity instanceof LivingEntity))
//						continue;
//					if(entity.equals(victim) || entity.equals(killer))
//						continue;
//					EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(killer, entity, DamageCause.CONTACT, dmg);
//					Bukkit.getPluginManager().callEvent(event);
//					if(event.isCancelled()) {
//						continue;
//					}
//					ManualDamage.doDamage(killer, (LivingEntity) entity, dmg, event);
//				}
//			}
//		}

		//TODO
//		if(modifiers.hasWampiryzm()) {
//			double restoreHp = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.05;
//			RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, restoreHp);
//			Bukkit.getPluginManager().callEvent(event);
//			if(!event.isCancelled())
//				killer.getWorld().spawnParticle(Particle.HEART, killer.getLocation(), 10, 0.5F, 0.5F, 0.5F, 0.1f);
//		}
//		if(modifiers.hasWampiryzm_h()) {
//			double restoreHp = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.065;
//			RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, restoreHp);
//			Bukkit.getPluginManager().callEvent(event);
//			if(!event.isCancelled())
//				killer.getWorld().spawnParticle(Particle.HEART, killer.getLocation(), 10, 0.5F, 0.5F, 0.5F, 0.1f);
//		}
//		if(modifiers.hasWampiryzm_m()) {
//			double restoreHp = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.09;
//			RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, restoreHp);
//			Bukkit.getPluginManager().callEvent(event);
//			if(!event.isCancelled())
//				killer.getWorld().spawnParticle(Particle.HEART, killer.getLocation(), 10, 0.5F, 0.5F, 0.5F, 0.1f);
//		}
//		if(modifiers.hasBarbarzynskiSzal()) {
//			new BukkitRunnable() {
//				int timer = 0;
//				double restoreHp = killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.0075;
//				@Override
//				public void run() {
//					if(timer >= 10) {
//						this.cancel();
//						return;
//					}
//					++timer;
//					RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, restoreHp);
//					Bukkit.getPluginManager().callEvent(event);
//					if(!event.isCancelled()){
//						killer.getWorld().spawnParticle(Particle.HEART, killer.getLocation().add(0,1.25,0), 5, 0.5F, 0.5F, 0.5F, 0.1f);
//					}
//				}
//			}.runTaskTimer(Main.getInstance(), 0, 20);
//		}

		//TODO
//		if(victim instanceof Player) {
//			if(Main.getListaRPG().containsKey(victim.getUniqueId().toString()))
//				RpgSystem.addXp(Main.getListaRPG().get(
//							victim.getUniqueId().toString()
//						).getLevel()*3,
//						killer.getUniqueId().toString());
//		}
		
	}
	
}
