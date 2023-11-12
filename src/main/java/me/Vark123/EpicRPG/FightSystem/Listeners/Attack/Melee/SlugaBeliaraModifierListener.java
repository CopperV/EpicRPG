package me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Melee;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicRPG.FightSystem.EpicDamageType;
import me.Vark123.EpicRPG.FightSystem.Events.EpicAttackEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgSkills;

public class SlugaBeliaraModifierListener implements Listener {
	
	@EventHandler
	public void onMod(EpicAttackEvent e) {
		if(e.isCancelled())
			return;
		
		if(!e.getDamageType().equals(EpicDamageType.MELEE))
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
		if(item == null || item.getType().equals(Material.AIR))
			return;

		NBTItem nbti = new NBTItem(item);
		if(!nbti.hasTag("SzponBeliara")) 
			return;
		
		double chance = p.getHealth() / 75.;
		int rand = new Random().nextInt(100);
		if(rand > chance)
			return;
		
		Entity victim = e.getVictim();
		DustOptions dust = new DustOptions(Color.RED, 1.5f);
		victim.getWorld().spawnParticle(Particle.REDSTONE, victim.getLocation().add(0,1,0), 10, 0.5f, 0.5f, 0.5f, 0.15f, dust);
		p.getWorld().playSound(p.getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 1, 0.1f);
		
		double extraDamage = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 1.25;
		e.setDmg(e.getDmg() + extraDamage);
	}

}
