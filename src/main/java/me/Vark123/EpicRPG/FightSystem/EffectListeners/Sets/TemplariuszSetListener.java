package me.Vark123.EpicRPG.FightSystem.EffectListeners.Sets;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

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
import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;

public class TemplariuszSetListener implements Listener {

	private static final Collection<UUID> divineShieldCooldowns = new HashSet<>();
	
	@EventHandler
	public void onAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.MAGIC))
			return;
		
		Object[] args = e.getArgs();
		if(args == null 
				|| args.length < 2
				|| !(args[1] instanceof EpicRune))
			return;
		
		EpicRune rune = (EpicRune) args[1];
		String type = rune.getMagicType();
		if(!type.equalsIgnoreCase("swiatlo"))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player player))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgPlayerInfo info = rpg.getInfo();
		
		if(info.getSetCounts().getOrDefault("Templariusz", 0) < 2)
			return;
		
		e.increaseModifier(0.04);
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
		
		if(info.getSetCounts().getOrDefault("Templariusz", 0) < 4)
			return;

		
		Random rand = new Random();
		if(rand.nextDouble() > 0.001)
			return;

		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 0.8f, 1.44f);
		player.getWorld().spawnParticle(Particle.COMPOSTER, player.getLocation().add(0,1,0), 24, 0.45f, 1f, 0.45f, 0.12f);

		double amount = player.getAttribute(Attribute.MAX_HEALTH).getValue()*0.2;

		AttributeModifier modifier = new AttributeModifier(
				new NamespacedKey(me.Vark123.EpicRPG.Main.getInstance(), "templariusz_divineshield"),
				amount,
				Operation.ADD_NUMBER,
				EquipmentSlotGroup.ANY);

		player.getAttribute(Attribute.MAX_ABSORPTION).removeModifier(modifier);
		player.getAttribute(Attribute.MAX_ABSORPTION).addModifier(modifier);
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
				player.getAttribute(Attribute.MAX_ABSORPTION).removeModifier(modifier);
			}
		}.runTaskLater(Main.getInst(), 20*5);
	}
	
}
