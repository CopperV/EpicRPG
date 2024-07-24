package me.Vark123.EpicRPG.Core.Listeners;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Vark123.EpicOptions.Main;

public class PlayerJumpModifyListener implements Listener {

	private static final Collection<Player> cd = new HashSet<>();
	
	@EventHandler
	public void onJump(PlayerMoveEvent e) {
		if(e.isCancelled())
			return;
		
		Player p = e.getPlayer();
		if(cd.contains(p))
			return;
		
		PotionEffect potion = p.getPotionEffect(PotionEffectType.SLOW);
		if(potion == null)
			return;
		
		Vector velocity = p.getVelocity();
		if(velocity.getY() < 0.415)
			return;
		
		int level = potion.getAmplifier();
		
		if(level >= 6){
			velocity.setY(-2);
			p.setVelocity(velocity);
			return;
		}
		
		double force = (level + 1) * 0.15;
		double jumpForce = velocity.getY() * (1. - force);
		if(jumpForce < 0)
			jumpForce = 0;
		
		velocity.setY(jumpForce);
		p.setVelocity(velocity);
		
		int cdDuration = 6-level;
		
		cd.add(p);
		new BukkitRunnable() {
			@Override
			public void run() {
				cd.remove(p);
			}
		}.runTaskLater(Main.getInst(), cdDuration);
	}
	
}
