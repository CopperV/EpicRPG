package me.Vark123.EpicRPG.FightSystem.Modifiers.ModList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.FightSystem.Modifiers.DamageModifier;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgModifiers;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class RytualKrwiModifier implements DamageModifier {

	@Override
	public double modifyDamage(Entity damager, Entity victim, double damage, DamageCause cause) {
		
		if(!(victim instanceof Player))
			return damage;
		
		Player p = (Player) victim;
		if(!PlayerManager.getInstance().playerExists(p))
			return damage;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		if(!modifiers.hasRytualKrwi())
			return damage;
		
		ItemStack rune = MythicBukkit.inst().getItemManager().getItemStack("RytualKrwi");
		if(rune == null)
			return damage;

		//TODO
//		DynamicRune dr = new DynamicRune(rune);
//		RytualKrwi runa = new RytualKrwi(dr, p);
//		runa.castEffect();
		
		return damage;
	}

}
