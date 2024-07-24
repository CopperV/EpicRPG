package me.Vark123.EpicRPG.FightSystem.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.FightSystem.ManualDamage;

public class WeaknessAttackListener implements Listener {

	@EventHandler
	public void onClick(PlayerAnimationEvent e) {
		if (e.isCancelled())
			return;

		Player p = e.getPlayer();
		if(!p.hasPotionEffect(PotionEffectType.WEAKNESS))
			return;
		
		Location pEyeLoc = p.getEyeLocation();
		RayTraceResult result = p.getWorld()
				.rayTraceEntities(pEyeLoc, pEyeLoc.getDirection(), 4, 1.5, entity -> {
					if (!MythicBukkit.inst().getAPIHelper().isMythicMob(entity))
						return false;
					if (entity.isDead())
						return false;
					if (!BukkitAdapter.adapt(entity).isDamageable())
						return false;
					if (!BukkitAdapter.adapt(entity).isValid())
						return false;
					if (entity.equals(p))
						return false;
					if (!(entity instanceof LivingEntity))
						return false;
					ActiveMob ae = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity);
					MythicMob mob = ae.getType();
					if(ae.hasImmunityTable()) {
						if(ae.getImmunityTable().onCooldown(BukkitAdapter.adapt(p)))
							return false;
					} else if(ae.getNoDamageTicks() > 0)
						return false;
					if (mob.getDisguise() != null && mob.getDisguise().toLowerCase().contains("ender_dragon"))
						return false;
					return true;
				});

		if(result == null || result.getHitEntity() == null)
			return;
		Entity entity = result.getHitEntity();

		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(p, entity, DamageCause.ENTITY_ATTACK, 1);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		
		ManualDamage.doDamage(p, (LivingEntity) entity, event.getDamage(), event);
	}

}
