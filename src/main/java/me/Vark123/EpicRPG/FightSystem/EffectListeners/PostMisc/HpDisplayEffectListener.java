package me.Vark123.EpicRPG.FightSystem.EffectListeners.PostMisc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class HpDisplayEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMod(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		if(!(victim instanceof LivingEntity))
			return;
		
		Entity damager = e.getDamager();
		if(damager == null)
			return;
		
		if(!(damager instanceof Player)) {
			if(!MythicBukkit.inst().getMobManager().isActiveMob(damager.getUniqueId()))
				return;
			
			ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(damager);
			if(!aMob.getOwner().isPresent()
					|| Bukkit.getPlayer(aMob.getOwner().get()) == null)
				return;
			
			damager = Bukkit.getEntity(aMob.getOwner().get());
		}

		ActiveMob mob = MythicBukkit.inst().getAPIHelper().getMythicMobInstance(victim);
		if(mob == null) 
			return;
		if(mob.getType().usesBossBar())
			return;

		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		double damage = e.getFinalDamage();
		
		rpg.updateEnemyHpBar((LivingEntity) victim, damage);
	}

}
