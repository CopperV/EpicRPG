package me.Vark123.EpicRPG.OldFightSystem.Modifiers.ModList;

import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.OldFightSystem.Modifiers.DamageModifier;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class ScaleDamageModifier implements DamageModifier {
	
	private static final double MAG_SCALE = 0.9;
	private static final double WOJ_SCALE = 0.85;
	private static final double MYS_SCALE = 0.7;
	private static final double OBY_SCALE = 1.2;

	@Override
	public double modifyDamage(Entity damager, Entity victim, double damage, DamageCause cause) {
		switch(cause) {
			case PROJECTILE:
				if(damager instanceof AbstractArrow)
					damager = (Entity) ((AbstractArrow)damager).getShooter();
				break;
			default:
		}
		
		if(!(damager instanceof Player))
			return damage;
		
		Player p = (Player) damager;
		if(!PlayerManager.getInstance().playerExists(p))
			return damage;
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		switch(rpg.getInfo().getShortProf().toLowerCase()) {
			case "woj":
				damage *= WOJ_SCALE;
				break;
			case "mag":
				damage *= MAG_SCALE;
				break;
			case "mys":
				damage *= MYS_SCALE;
				break;
			case "oby":
				damage *= OBY_SCALE;
				break;
		}
		
		return damage;
	}

}
