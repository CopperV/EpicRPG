package me.Vark123.EpicRPG.FightSystem.Modifiers.ModList;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.Vark123.EpicRPG.FightSystem.Modifiers.DamageModifier;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;

public class WedrownyCienModifier implements DamageModifier {

	@Override
	public double modifyDamage(Entity damager, Entity victim, double damage, DamageCause cause) {
		if(!(damager instanceof Player))
			return damage;
		
		Player p = (Player) damager;
		if(!PlayerManager.getInstance().playerExists(p))
			return damage;

		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		if(!modifiers.hasWedrownyCien())
			return damage;

		//TODO
//		if(!WedrownyCien.getEffected().contains(p))
//			return damage;
//
//		WedrownyCien.getEffected().remove(p);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1.85f);
		p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, p.getLocation().add(0,1,0), 25, 0.5f, 0.75f, 0.5f, 0.1f);
		
		return damage;
	}

}
