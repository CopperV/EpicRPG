package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Misc;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.RuneSystem.RuneTimeEffect;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;

public class TrujacaAuraEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(e.getDamageType().equals(EpicDamageType.CUSTOM))
			return;
		
		Entity victim = e.getVictim();
		Entity damager = e.getDamager();
		if(damager instanceof Projectile)
			damager = (Entity) ((Projectile) damager).getShooter();
		if(!(damager instanceof Player))
			return;

		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		
		if(!modifiers.hasTrujacaAura())
			return;
		
		double dmg = e.getDmg()*0.05;
		if(RuneDamage.directDamageEffect(p, (LivingEntity) victim, 
				Optional.of(dmg), RuneUtils.getCustomTimeEffect(dmg, RuneTimeEffect.POISON, EpicDamageType.CUSTOM))) {
			Location loc2 = victim.getLocation().add(0, 1, 0);
			loc2.getWorld().spawnParticle(Particle.SLIME, loc2, 15, 0.2, 0.2, 0.2, 0.05);
		}
		
	}

}
