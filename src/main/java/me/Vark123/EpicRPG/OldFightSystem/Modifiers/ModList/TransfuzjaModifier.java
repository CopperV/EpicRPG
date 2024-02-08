package me.Vark123.EpicRPG.OldFightSystem.Modifiers.ModList;

import org.bukkit.EntityEffect;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.OldFightSystem.Modifiers.DamageModifier;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.RuneSystem.Runes.Transfuzja;

public class TransfuzjaModifier implements DamageModifier {

	@Override
	public double modifyDamage(Entity damager, Entity victim, double damage, DamageCause cause) {
		
		if(!(victim instanceof Player))
			return damage;
		
		Player p = (Player) victim;
		if(!PlayerManager.getInstance().playerExists(p))
			return damage;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		if(!modifiers.hasTransfuzja())
			return damage;

		if(!Transfuzja.getEffected().contains(p))
			return damage;
		
		if(p.getHealth() - damage >= 1.0D)
			return damage;
		
		rpg.getStats().createRegenHpTask(3, p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()*0.05);
		p.playEffect(EntityEffect.TOTEM_RESURRECT);
		p.playEffect(EntityEffect.HURT);
		Transfuzja.getEffected().remove(p);
		return -1;
	}

}
