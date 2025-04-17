package me.Vark123.EpicRPG.OldFightSystem.Listeners.Attack.Projectile;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicRPG.OldFightSystem.EpicDamageType;
import me.Vark123.EpicRPG.OldFightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;

public class RuneProjectileModifierListener implements Listener {
	
	private static final Random rand = new Random();
	
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
		if(modifiers.hasZakletaStrzala())
			modifier += 0.2;
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
		if(modifiers.hasSzalPrzedwiecznych())
			modifier += 0.3;
		if(modifiers.hasSzalPrzedwiecznych_h())
			modifier += 0.44;
		if(modifiers.hasSzalPrzedwiecznych_m())
			modifier += 0.6;
		if(modifiers.hasTajemnyBlask_m() && rpg.getInfo().getProffesion().equals("§2Mysliwy")) {
			modifier += rand.nextDouble(0.5) + 0.25;
		} else if(modifiers.hasTajemnyBlask() && rpg.getInfo().getProffesion().equals("§2Mysliwy")) {
			modifier += rand.nextDouble(0.3) + 0.15;
		}
		
		e.increaseModifier(modifier);
	}

}
