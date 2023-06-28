package me.Vark123.EpicRPG.FightSystem.Modifiers.ModList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.FightSystem.Modifiers.DamageModifier;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;

public class ZewKrwiModifier implements DamageModifier {

	@Override
	public double modifyDamage(Entity damager, Entity victim, double damage, DamageCause cause) {
		if(!(victim instanceof Player))
			return damage;
		
		Player p = (Player) victim;
		if(!PlayerManager.getInstance().playerExists(p))
			return damage;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		if(modifiers.hasZewKrwi()){
			modifiers.addZewKrwiMod(1.5);
		}
		
		return damage;
	}

}
