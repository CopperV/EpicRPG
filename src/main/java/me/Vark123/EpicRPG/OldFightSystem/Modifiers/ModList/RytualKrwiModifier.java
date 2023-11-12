package me.Vark123.EpicRPG.OldFightSystem.Modifiers.ModList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.OldFightSystem.Modifiers.DamageModifier;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.Runes.RytualKrwi;

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

		ItemStackRune ir = new ItemStackRune(rune);
		RytualKrwi runa = new RytualKrwi(ir, p);
		runa.castEffect();
		
		return damage;
	}

}
