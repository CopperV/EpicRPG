package me.Vark123.EpicRPG.FightSystem.EffectListeners.Misc;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicComponentAPI.EpicComponent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.FightSystem.Events.EpicPostDamageEffectEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgSkills;
import me.Vark123.EpicRPG.Utils.Utils;

public class SlugaBeliaraEffectListener implements Listener {
	
	private static final Random rand = new Random();
	
	@EventHandler(priority = EventPriority.LOW)
	private void onAttack(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		Entity damager = e.getDamager();
		if(!(damager instanceof Player))
			return;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgSkills skills = rpg.getSkills();
		if(!skills.hasSlugaBeliara())
			return;

		ItemStack item = p.getInventory().getItemInMainHand();
		if(item == null || item.getType().equals(Material.AIR)
				|| !MythicBukkit.inst().getItemManager().isMythicItem(item))
			return;
		
		EpicComponent comp = new EpicComponent(item);
		if(!comp.hasKey("szpon_beliara"))
			return;
		
		double chance = (p.getHealth() / 75.) * 0.01;
		double randChance = rand.nextDouble();
		if(randChance > chance)
			return;
		
		AbstractEntity ae = BukkitAdapter.adapt(e.getVictim());
		ae.setMetadata("SzponBeliaraEffect", true);
		
		double extraDamage = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 2 + rpg.getStats().getFinalObrazenia();
		e.setDamage(e.getDamage() + extraDamage);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onEffect(EpicPostDamageEffectEvent e) {
		AbstractEntity ae = BukkitAdapter.adapt(e.getVictim());
		if(!ae.hasMetadata("SzponBeliaraEffect"))
			return;
		
		ae.removeMetadata("SzponBeliaraEffect");
		if(e.isCancelled())
			return;
		
		Bukkit.broadcastMessage("Dodanie obslugi anulowania obrazen");
		
		Location current = e.getVictim().getLocation().clone();
		current.getWorld().playSound(current, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.5f, 0.9f);
		
		int count = 3 + rand.nextInt(3);
		for(int i = 0; i < count; ++i) {
			double offsetX = (rand.nextDouble() - 0.5) * 3;
            double offsetZ = (rand.nextDouble() - 0.5) * 3;
            double offsetY = 2 + rand.nextDouble() * 3;
            
            Location next = current.clone().add(new Vector(offsetX, offsetY, offsetZ));
            
            Utils.drawLine(Particle.ELECTRIC_SPARK, current, next, 0.1, 2, 0.05f, 0.05f, 0.05f, 0.03f);
		
            current = next;
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onDamageController(EntityDamageEvent e) {
		AbstractEntity ae = BukkitAdapter.adapt(e.getEntity());
		if(e.isCancelled() && ae.hasMetadata("SzponBeliaraEffect"))
			ae.removeMetadata("SzponBeliaraEffect");
	}

}
