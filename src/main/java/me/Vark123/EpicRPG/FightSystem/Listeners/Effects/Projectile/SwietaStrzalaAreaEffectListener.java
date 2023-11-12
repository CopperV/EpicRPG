package me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Projectile;

import java.util.Optional;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.RuneDamage;
import me.Vark123.EpicRPG.FightSystem.Events.EpicEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;

public class SwietaStrzalaAreaEffectListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMod(EpicEffectEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(EpicDamageType.PROJECTILE))
			return;
		
		Entity victim = e.getVictim();
		Entity damager = e.getDamager();
		if(!(damager instanceof Projectile))
			return;
		
		Projectile projectile = (Projectile) damager;
		damager = (Entity) projectile.getShooter();
		if(!(damager instanceof Player))
			return;

		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgModifiers modifiers = rpg.getModifiers();
		
		if(!modifiers.hasSwietaStrzala())
			return;
		
		double holyDmg = e.getFinalDamage() * 0.25;
		victim.getWorld().getNearbyEntities(victim.getLocation(), 4, 4, 4, entity -> {
				if(entity.equals(p) || !(entity instanceof LivingEntity))
					return false;
				if(entity.equals(victim))
					return false;
				if(entity instanceof Player) {
					RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
					ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(entity.getLocation()));
					State flag = set.queryValue(null, Flags.PVP);
					if(flag != null && flag.equals(State.ALLOW)
							&& !entity.getWorld().getName().toLowerCase().contains("dungeon"))
						return true;
					return false;
				}
				if(!io.lumine.mythic.bukkit.BukkitAdapter.adapt(entity).isDamageable())
					return false;
				return true;
			}).stream().forEach(entity -> {
				RuneDamage.directDamageEffect(p, (LivingEntity) entity, Optional.of(holyDmg), Optional.empty());
			});
		
	}

}
