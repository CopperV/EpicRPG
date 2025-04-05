package me.Vark123.EpicRPG.FightSystem.EffectListeners.Runes;

import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicComponentAPI.EpicComponent;
import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;

public class WybraniecBeliaraEffectListener implements Listener {
	
	@EventHandler
	public void onMod1(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(DamageType.MAGIC))
			return;
		
		Object[] args = e.getArgs();
		if(args == null 
				|| args.length < 2
				|| !(args[1] instanceof EpicRune))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;

		EpicRune rune = (EpicRune) args[1];
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		
		if(!rune.getMagicType().equalsIgnoreCase("mrok"))
			return;
		if(!modifiers.hasActiveModifier(EpicModifierTypes.WYBRANIEC_BELIARA))
			return;
		
		e.increaseModifier(0.35);
	}
	
	@EventHandler
	public void onMod2(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		if(!modifiers.hasActiveModifier(EpicModifierTypes.WYBRANIEC_BELIARA))
			return;
		
		ItemStack item = null;
		if(e.getDamageType().equals(DamageType.PROJECTILE) && e.getDamageSource().getDirectEntity() instanceof AbstractArrow) {
			AbstractArrow projectile = (AbstractArrow) e.getDamageSource().getDirectEntity();
			if(projectile.hasMetadata("rpg_bow")) {
				item = (ItemStack) projectile.getMetadata("rpg_bow").get(0).value();
			}
		} else {
			item = p.getInventory().getItemInMainHand();
		}

		if(item == null || item.getType().equals(Material.AIR)
				|| !MythicBukkit.inst().getItemManager().isMythicItem(item))
			return;

		EpicComponent comp = new EpicComponent(item, MythicBukkit.inst());
		if(!comp.hasKey("szpon_beliara"))
			return;

		e.increaseModifier(0.2);
	}

}
