package me.Vark123.EpicRPG.OldFightSystem.Listeners.Sets;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Vark123.EpicRPG.OldFightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.OldFightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class WiecznyWedrowiecSetListener implements Listener {

	private static final double red = 32./255.;
	private static final double green = 32./255.;
	private static final double blue = 32./255.;
	private static final Random rand = new Random();
	
	private static final PotionEffect slowEffect = new PotionEffect(PotionEffectType.SLOWNESS, 20*4, 0);

	@EventHandler(priority = EventPriority.MONITOR)
	public void slowDonwEnemyEffect(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgPlayerInfo info = rpg.getInfo();
		int amount = info.getSetCounts().getOrDefault("Wieczny_Wedrowiec", 0);
		if(amount < 2)
			return;
		
		LivingEntity victim = (LivingEntity) e.getVictim();
		if(rand.nextDouble() > 0.02)
			return;

		Location loc = victim.getLocation().clone().add(0,1,0);
		p.getWorld().playSound(loc, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 0.8f, 1.3f);
		victim.addPotionEffect(slowEffect);
		for(int i = 0; i < 20; ++i) {
			double x = rand.nextDouble(1.2) - 0.6;
			double y = rand.nextDouble(1.2) - 0.6;
			double z = rand.nextDouble(1.2) - 0.6;
			Location tmp = loc.clone().add(x,y,z);
			p.getWorld().spawnParticle(Particle.ENTITY_EFFECT, tmp, 0, red, green, blue, 1);
		}
	}
	
	@EventHandler
	public void onCritIncrease(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgPlayerInfo info = rpg.getInfo();
		int amount = info.getSetCounts().getOrDefault("Wieczny_Wedrowiec", 0);
		if(amount < 4)
			return;
		
		if(!e.getCalculatedDamage().getValue())
			return;
		
		e.increaseModifier(0.1);
	}
	
}
