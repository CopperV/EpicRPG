package me.Vark123.EpicRPG.FightSystem.EffectListeners.Sets;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicOptions.Main;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class NietykalnyNiezwyciezonySetListener implements Listener {

	private static final Collection<UUID> divineShieldCooldowns = new HashSet<>();
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onDamageEffectToHeal(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getDamager();
		if(!(victim instanceof Player player))
			return;
		
		if(e.getDamager() == null)
			return;
		
		Random rand = new Random();
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgPlayerInfo info = rpg.getInfo();
		

		if(info.getSetCounts().getOrDefault("Nietykalny_Niezwyciezony", 0) < 3 ||
				rand.nextDouble() > 0.005)
			return;
		
		double hpToRestore = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.05;
		RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, hpToRestore);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()) {
			player.spawnParticle(Particle.HEART, player.getLocation().clone().add(0,1,0), 16, 0.5f, 0.8f, 0.5f, 0.1f);
			player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 1.44f);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onDivineShield(EpicPostDamageEffectEvent e) {
		if(e.isCancelled())
			return;

		Entity victim = e.getDamager();
		if(!(victim instanceof Player player))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgPlayerInfo info = rpg.getInfo();
		
		if(info.getSetCounts().getOrDefault("Nietykalny_Niezwyciezony", 0) < 3)
			return;

		
		Random rand = new Random();
		if(rand.nextDouble() > 0.0025)
			return;

		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 0.8f, 1.44f);
		player.getWorld().spawnParticle(Particle.COMPOSTER, player.getLocation().add(0,1,0), 24, 0.45f, 1f, 0.45f, 0.12f);

		double amount = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*0.2;

		AttributeModifier modifier = new AttributeModifier(
				new NamespacedKey(me.Vark123.EpicRPG.Main.getInstance(), "templariusz_divineshield"),
				amount,
				Operation.ADD_NUMBER,
				EquipmentSlotGroup.ANY);

		player.getAttribute(Attribute.GENERIC_MAX_ABSORPTION).removeModifier(modifier);
		player.getAttribute(Attribute.GENERIC_MAX_ABSORPTION).addModifier(modifier);
		player.setAbsorptionAmount(player.getAbsorptionAmount()+amount);
		
		divineShieldCooldowns.add(player.getUniqueId());
		new BukkitRunnable() {
			@Override
			public void run() {
				divineShieldCooldowns.remove(player.getUniqueId());
				if(!player.isOnline() || player.getAbsorptionAmount() <= 0)
					return;

				player.getWorld().playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 0.9f, 0.8f);
				if(player.getAbsorptionAmount() < amount)
					player.setAbsorptionAmount(0);
				else
					player.setAbsorptionAmount(player.getAbsorptionAmount() - amount);
				player.getAttribute(Attribute.GENERIC_MAX_ABSORPTION).removeModifier(modifier);
			}
		}.runTaskLater(Main.getInst(), 20*5);
	}
	
}
