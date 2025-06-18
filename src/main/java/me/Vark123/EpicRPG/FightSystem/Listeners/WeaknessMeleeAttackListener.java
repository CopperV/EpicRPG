package me.Vark123.EpicRPG.FightSystem.Listeners;

import org.bukkit.Location;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;

import io.papermc.paper.event.player.PlayerArmSwingEvent;
import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class WeaknessMeleeAttackListener implements Listener {

	private static final IRuneHitCondition hitCondition = new PvPRuneHitCondition();
	
	@EventHandler
	private void onSwing(PlayerArmSwingEvent e) {
		if(e.isCancelled())
			return;
		
		Player player = e.getPlayer();
		if(!player.hasPotionEffect(PotionEffectType.WEAKNESS))
			return;
		
		Location eyeLoc = player.getEyeLocation();
		RayTraceResult result = player.getWorld()
				.rayTraceEntities(eyeLoc, eyeLoc.getDirection().normalize(), 5, en -> !en.equals(player));
		if(result == null || result.getHitEntity() == null)
			return;
		
		Entity entity = result.getHitEntity();
		LivingEntity le = (LivingEntity) entity;
		if(!hitCondition.check(player, le))
			return;
		
		if(Utils.hasNoDamageTicks(player, player))
			return;
		
		if(DamageUtils.applyDirectDamageEffect(player, le, 1, DamageType.PLAYER_ATTACK, DamageCause.ENTITY_ATTACK)) {
			Utils.setEntityNoDamageTicks(player, le);
		}
	}

}
