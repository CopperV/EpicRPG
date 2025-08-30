package me.Vark123.EpicRPG.RuneSystem.SummonSystem.Listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.util.Vector;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.SummonManager;
import me.Vark123.EpicRPG.Utils.Utils;

public class SummonDismissListener implements Listener {

	private static final long DISMISS_CD = 250L;
	private static final Map<UUID, Long> playerCd = new HashMap<>();
	
	@EventHandler
	private void onClick(PlayerInteractAtEntityEvent e) {
		if(e.isCancelled())
			return;
		
		Player player = e.getPlayer();
		Entity entity = e.getRightClicked();
		if(!player.isSneaking())
			return;
		
		AbstractEntity aEntity = BukkitAdapter.adapt(entity);
		if(!MythicBukkit.inst().getMobManager().isActiveMob(aEntity))
			return;
		
		ActiveMob summon = MythicBukkit.inst().getMobManager().getMythicMobInstance(aEntity);
		if(!summon.getOwnerUUID().isPresent() || !summon.getOwnerUUID().get().equals(player.getUniqueId()))
			return;
		
		UUID uid = player.getUniqueId();
		if(playerCd.containsKey(uid) && playerCd.get(uid) + DISMISS_CD > System.currentTimeMillis())
			return;
		
		playerCd.put(uid, System.currentTimeMillis());
		
		{
			Location loc = BukkitAdapter.adapt(summon.getEntity().getLocation()).clone().add(0, 0.1, 0);
			loc.getWorld().playSound(loc, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.1f, 0.9f);
			
			Random rand = new Random();
			double radius = 1.5;
			int points = 16;
			
			for(int i = 0; i < points; ++i) {
				double angle = Math.PI * 2 * (double) i / (double) points;
				
				double x = Math.sin(angle) * radius;
				double z = Math.cos(angle) * radius;
				
				Location p1 = loc.clone().add(x, 0, z);
				
				p1.getWorld().spawnParticle(Particle.SMOKE, p1, 1);
			}
			for(int i = 0; i < 25; ++i) {
				double angle = rand.nextDouble(Math.PI*2);
				
				double x = Math.sin(angle) * radius;
				double z = Math.cos(angle) * radius;
				
				Location p1 = loc.clone().add(x, 0, z);
				
				p1.getWorld().spawnParticle(Particle.SMOKE, p1, 0, 0, 1, 0, rand.nextFloat(0.03f, 0.15f));
			}
			
			Utils.drawPentagram(
					Particle.SMALL_FLAME,
					loc,
					new Vector(0,1,0),
					5,
					radius,
					0.05,
					2,
					rand.nextDouble(Math.PI * 2));
		}
		
		SummonManager.get().dismissSummon(summon);
	}
	
}
