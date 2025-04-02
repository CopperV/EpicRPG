package me.Vark123.EpicRPG.FightSystem.Listeners;

import org.bukkit.Location;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.RayTraceResult;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.Utils.Utils;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;

public class DragonMeleeAttackListener implements Listener {

	@EventHandler
	private void onSwing(PlayerArmSwingEvent e) {
		if(e.isCancelled())
			return;
		
		Player player = e.getPlayer();
		Location eyeLoc = player.getEyeLocation();
		RayTraceResult result = player.getWorld()
				.rayTraceEntities(eyeLoc, eyeLoc.getDirection().normalize(), 5);
		if(result == null || result.getHitEntity() == null)
			return;
		
		Entity entity = result.getHitEntity();
		
		if (!(entity instanceof LivingEntity))
			return;
		
		if (!MythicBukkit.inst().getAPIHelper().isMythicMob(entity))
			return;
		if (entity.isDead())
			return;
		
		AbstractEntity ae = BukkitAdapter.adapt(entity);
		if (!ae.isDamageable())
			return;
		if (!ae.isValid())
			return;

		if(!DisguiseAPI.isDisguised(entity))
			return;
		Disguise disguise = DisguiseAPI.getDisguise(entity);
		if(!disguise.getType().equals(DisguiseType.ENDER_DRAGON))
			return;
		
		
		if(DamageUtils.applyDirectDamageEffect(player, (LivingEntity) entity, 1, DamageType.PLAYER_ATTACK, DamageCause.ENTITY_ATTACK)) {
			Utils.setEntityNoDamageTicks(player, (LivingEntity) entity);
		}
	}
	
}
