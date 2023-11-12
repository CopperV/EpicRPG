package me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Projectile;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;

public class RuneProjectileModifierListener implements Listener {
	
	@EventHandler
	public void onMod(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(EpicDamageType.PROJECTILE))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Projectile))
			return;
		
		Projectile projectile = (Projectile) damager;
		damager = (Entity) projectile.getShooter();
		if(!(damager instanceof Player))
			return;

		ItemStack bow = (ItemStack) projectile.getMetadata("rpg_bow").get(0).value();
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		double modifier = 0;
		if(modifiers.hasWyostrzoneZmysly())
			modifier += 0.15;
		if(modifiers.hasPrecyzyjnyStrzal())
			modifier += 0.5;
		if(modifiers.hasLodowaStrzala())
			modifier += 0.3;
		if(modifiers.hasSwietaStrzala())
			modifier += 0.2;
		if(modifiers.hasKrwawaStrzala())
			modifier += 0.3;
		if(modifiers.hasEksplodujacaStrzala()) {
			if(bow.getType().equals(Material.CROSSBOW)) {
				int enchant = bow.getEnchantmentLevel(Enchantment.QUICK_CHARGE);
				modifier += (0.4 - 0.05*enchant);
			} else {
				modifier += 0.3;
			}
		}
		if(modifiers.hasEksplodujacaStrzala_h()) {
			if(bow.getType().equals(Material.CROSSBOW)) {
				int enchant = bow.getEnchantmentLevel(Enchantment.QUICK_CHARGE);
				modifier += (0.46 - 0.06*enchant);
			} else {
				modifier += 0.34;
			}
		}
		if(modifiers.hasEksplodujacaStrzala_m()) {
			if(bow.getType().equals(Material.CROSSBOW)) {
				int enchant = bow.getEnchantmentLevel(Enchantment.QUICK_CHARGE);
				modifier += (0.54 - 0.07*enchant);
			} else {
				modifier += 0.4;
			}
		}
		
		e.increaseModifier(modifier);
	}

}
