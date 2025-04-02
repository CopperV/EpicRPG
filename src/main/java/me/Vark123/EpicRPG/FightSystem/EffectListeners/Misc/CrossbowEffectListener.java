package me.Vark123.EpicRPG.FightSystem.EffectListeners.Misc;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;

public class CrossbowEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.PROJECTILE))
			return;
		
		Entity projectile = e.getDamageSource().getDirectEntity();
		if(!(projectile instanceof Projectile))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;

		ItemStack bow = (ItemStack) projectile.getMetadata("rpg_bow").get(0).value();
		if(!bow.getType().equals(Material.CROSSBOW))
			return;
		
		double modifier = 1;
		int enchant = bow.getEnchantmentLevel(Enchantment.QUICK_CHARGE);
		switch(enchant) {
			case 0:
				modifier = 1.3;
				break;
			case 1:
				modifier = 1.15;
				break;
			case 2:
				modifier = 1;
				break;
			case 3:
				modifier = 0.85;
				break;
			case 4:
				modifier = 0.7;
				break;
			case 5:
				modifier = 0.55;
				break;
		}
		
		e.setDamage(e.getDamage() * modifier);
	}

}
