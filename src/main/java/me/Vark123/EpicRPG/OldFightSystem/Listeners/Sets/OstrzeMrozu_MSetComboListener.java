package me.Vark123.EpicRPG.OldFightSystem.Listeners.Sets;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicOptions.Main;
import me.Vark123.EpicRPG.OldFightSystem.EpicDamageType;
import me.Vark123.EpicRPG.OldFightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.OldFightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.OldFightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.OldRuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class OstrzeMrozu_MSetComboListener implements Listener {

	private static final Collection<UUID> divineShieldCooldowns = new HashSet<>();
	
	//WOJOWNIK
	@EventHandler
	public void onWojownikAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		ItemStack weapon = p.getInventory().getItemInMainHand();
		if(weapon == null || weapon.getType().equals(Material.AIR))
			return;
		
		NBTItem nbt = new NBTItem(weapon);
		if(!nbt.hasTag("MYTHIC_TYPE") || !nbt.getString("MYTHIC_TYPE").equals("Raid_1_M_Unikat"))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgPlayerInfo info = rpg.getInfo();
		
		double modifier = 0;
		
		switch(info.getSetCounts().getOrDefault("Runiczny_Egzekutor", 0)) {
			case 4:
			case 3:
			case 2:
				modifier += 0.06;
			case 1:
				modifier += 0.06;
				break;
		}
		switch(info.getSetCounts().getOrDefault("Runiczny_Egzekutor_H", 0)) {
			case 4:
			case 3:
			case 2:
				modifier += 0.09;
			case 1:
				modifier += 0.09;
				break;
		}
		switch(info.getSetCounts().getOrDefault("Runiczny_Egzekutor_M", 0)) {
			case 4:
			case 3:
			case 2:
				modifier += 0.125;
			case 1:
				modifier += 0.125;
				break;
		}
		
		e.increaseModifier(modifier);
	}

	@EventHandler
	public void onWojownikDefense(EpicDefenseEvent e) {
		if(e.isCancelled())
			return;

		Entity victim = e.getDamager();
		if(!(victim instanceof Player))
			return;
		
		Player p = (Player) victim;
		ItemStack weapon = p.getInventory().getItemInMainHand();
		if(weapon == null || weapon.getType().equals(Material.AIR))
			return;
		
		NBTItem nbt = new NBTItem(weapon);
		if(!nbt.hasTag("MYTHIC_TYPE") || !nbt.getString("MYTHIC_TYPE").equals("Raid_1_M_Unikat"))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgPlayerInfo info = rpg.getInfo();
		
		double modifier = 0;
		
		switch(info.getSetCounts().getOrDefault("Runiczny_Egzekutor", 0)) {
			case 4:
			case 3:
				modifier += 0.075;
				break;
		}
		switch(info.getSetCounts().getOrDefault("Runiczny_Egzekutor_H", 0)) {
			case 4:
			case 3:
				modifier += 0.125;
				break;
		}
		switch(info.getSetCounts().getOrDefault("Runiczny_Egzekutor_M", 0)) {
			case 4:
			case 3:
				modifier += 0.19;
				break;
		}

		e.decreaseModifier(modifier);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onWojownikDivineShield(EpicEffectEvent e) {
		if(e.isCancelled())
			return;

		Entity victim = e.getDamager();
		if(!(victim instanceof Player))
			return;
		
		Player p = (Player) victim;
		if(divineShieldCooldowns.contains(p.getUniqueId()))
			return;
		
		ItemStack weapon = p.getInventory().getItemInMainHand();
		if(weapon == null || weapon.getType().equals(Material.AIR))
			return;
		
		NBTItem nbt = new NBTItem(weapon);
		if(!nbt.hasTag("MYTHIC_TYPE") || !nbt.getString("MYTHIC_TYPE").equals("Raid_1_M_Unikat"))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgPlayerInfo info = rpg.getInfo();
		
		double chance = 0;
		
		if(info.getSetCounts().getOrDefault("Runiczny_Egzekutor", 0) >= 4)
			chance += 0.015;
		if(info.getSetCounts().getOrDefault("Runiczny_Egzekutor_H", 0) >= 4)
			chance += 0.0225;
		if(info.getSetCounts().getOrDefault("Runiczny_Egzekutor_M", 0) >= 4)
			chance += 0.03;
		
		Random rand = new Random();
		if(rand.nextDouble() > chance)
			return;
		
		p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 0.8f, 1.3f);
		p.getWorld().spawnParticle(Particle.COMPOSTER, p.getLocation().add(0,1,0), 24, 0.45f, 1f, 0.45f, 0.12f);

		double amount = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*0.7;
		p.setAbsorptionAmount(p.getAbsorptionAmount()+amount);
		
		divineShieldCooldowns.add(p.getUniqueId());
		new BukkitRunnable() {
			@Override
			public void run() {
				divineShieldCooldowns.remove(p.getUniqueId());
				if(!p.isOnline() || p.getAbsorptionAmount() <= 0)
					return;

				p.getWorld().playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 0.9f, 0.8f);
				if(p.getAbsorptionAmount() < amount)
					p.setAbsorptionAmount(0);
				else
					p.setAbsorptionAmount(p.getAbsorptionAmount() - amount);
			}
		}.runTaskLater(Main.getInst(), 20*8);
	}
	
	//MYSLIWY
	@EventHandler
	public void onMysliwyCritAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();
		if(!(damager instanceof Player))
			return;
		
		if(!e.getCalculatedDamage().getValue())
			return;
		
		Player p = (Player) damager;
		ItemStack weapon = p.getInventory().getItemInMainHand();
		if(weapon == null || weapon.getType().equals(Material.AIR))
			return;
		
		NBTItem nbt = new NBTItem(weapon);
		if(!nbt.hasTag("MYTHIC_TYPE") || !nbt.getString("MYTHIC_TYPE").equals("Raid_1_M_Unikat"))
			return;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgPlayerInfo info = rpg.getInfo();
		
		double modifier = 0;
		
		if(info.getSetCounts().getOrDefault("Wieczny_Wedrowiec", 0) >= 4)
			modifier += 0.07;
		if(info.getSetCounts().getOrDefault("Wieczny_Wedrowiec_H", 0) >= 4)
			modifier += 0.11;
		if(info.getSetCounts().getOrDefault("Wieczny_Wedrowiec_M", 0) >= 4)
			modifier += 0.15;

		e.increaseModifier(modifier);
	}
	
	//MAG
	@EventHandler
	public void onMagMagicAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(EpicDamageType.MAGIC))
			return;
		
		Object[] args = e.getArgs();
		if(args == null 
				|| args.length <= 0
				|| !(args[0] instanceof ItemStackRune))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;

		ItemStackRune ir = (ItemStackRune) args[0];
		String type = ir.getMagicType();
		if(!type.equalsIgnoreCase("woda"))
			return;
		

		Player p = (Player) damager;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgPlayerInfo info = rpg.getInfo();
		
		ItemStack weapon = rpg.getBackItem();
		if(weapon == null || weapon.getType().equals(Material.AIR))
			return;
		
		NBTItem nbt = new NBTItem(weapon);
		if(!nbt.hasTag("MYTHIC_TYPE") || !nbt.getString("MYTHIC_TYPE").equals("Raid_1_M_Unikat"))
			return;
		
		double modifier = 0;
		
		switch(info.getSetCounts().getOrDefault("Mroczna_Zamiec", 0)) {
			case 4:
				modifier += 0.05;
			case 3:
				modifier += 0.05;
			case 2:
				modifier += 0.05;
			case 1:
				modifier += 0.05;
				break;
		}
		switch(info.getSetCounts().getOrDefault("Mroczna_Zamiec_H", 0)) {
			case 4:
				modifier += 0.07;
			case 3:
				modifier += 0.07;
			case 2:
				modifier += 0.07;
			case 1:
				modifier += 0.07;
				break;
		}
		switch(info.getSetCounts().getOrDefault("Mroczna_Zamiec_M", 0)) {
			case 4:
				modifier += 0.09;
			case 3:
				modifier += 0.09;
			case 2:
				modifier += 0.09;
			case 1:
				modifier += 0.09;
				break;
		}

		e.increaseModifier(modifier);
	}
	
}
