package me.Vark123.EpicRPG.FightSystem.EffectListeners.Professions;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.Vark123.EpicRPG.FightSystem.DamageType;
import me.Vark123.EpicRPG.FightSystem.Calculators.IDamageCalculator.DamageCalculatorResult;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicDefenseEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import net.md_5.bungee.api.ChatColor;

public class WarriorProfessionModifierListener implements Listener {
	
	@EventHandler
	public void onMeleeAttackMod(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(e.getArgs() == null || e.getArgs().length < 1 || !(e.getArgs()[0] instanceof DamageCalculatorResult))
			return;
		
		if(!e.getDamageType().equals(DamageType.MELEE))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgPlayerInfo info = rpg.getInfo();
		
		if(!ChatColor.stripColor(info.getShortProf().toLowerCase()).equalsIgnoreCase("woj"))
			return;
		
		e.increaseModifier(0.05);
	}
	
	@EventHandler
	public void onProjectileAttackMod(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(e.getArgs() == null || e.getArgs().length < 1 || !(e.getArgs()[0] instanceof DamageCalculatorResult))
			return;
		
		if(!e.getDamageType().equals(DamageType.PROJECTILE))
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;
		
		Entity projectile = e.getDamageSource().getDirectEntity();
		if(!(projectile instanceof Projectile))
			return;

		ItemStack bow = (ItemStack) projectile.getMetadata("rpg_bow").get(0).value();
		if(!bow.getType().equals(Material.CROSSBOW))
			return;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgPlayerInfo info = rpg.getInfo();
		
		if(!ChatColor.stripColor(info.getShortProf().toLowerCase()).equalsIgnoreCase("woj"))
			return;
		
		e.increaseModifier(0.05);
	}
	
	@EventHandler
	public void onDefenseMod(EpicDefenseEvent e) {
		if(e.isCancelled())
			return;
		
		Entity victim = e.getVictim();
		if(!(victim instanceof Player))
			return;
		
		Player p = (Player) victim;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgPlayerInfo info = rpg.getInfo();
		
		if(!ChatColor.stripColor(info.getShortProf().toLowerCase()).equalsIgnoreCase("woj"))
			return;
		
		e.decreaseModifier(0.07);
	}

}
