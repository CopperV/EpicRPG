package me.Vark123.EpicRPG.RubySystem;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class RubyUseEvent implements Listener {

	@EventHandler
	public void onUse(PlayerInteractEvent e) {
		
		Action ac = e.getAction();
		if(!(ac.equals(Action.RIGHT_CLICK_AIR) 
				|| ac.equals(Action.RIGHT_CLICK_BLOCK)))
			return;

		ItemStack ruby = e.getItem();
		if(ruby == null 
				|| ruby.getType().equals(Material.AIR))
			return;
		
		NBTItem nbt = new NBTItem(ruby);
		if(!(nbt.hasTag("RPGType")))
			return;
		
		String type = nbt.getString("RPGType");
		if(!(type.equalsIgnoreCase("hp_ruby") 
				|| type.equalsIgnoreCase("mana_ruby")))
			return;

		Player p = e.getPlayer();
		if(RubyManager.getInstance().getCooldowns().containsKey(p)
				&& (new Date().getTime() - RubyManager.getInstance().getCooldowns().get(p).getTime()) < RubyManager.getInstance().CLICK_COOLDOWN)
			return;
		RubyManager.getInstance().getCooldowns().put(p, new Date());
		
		boolean use = p.isSneaking();
		boolean hpRuby = type.equalsIgnoreCase("hp_ruby");

		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		DustOptions dust;
		float volume;
		Sound sound;
		List<String> lore = new LinkedList<>();
		if(use) {
			int present = Integer.parseInt(nbt.getString("PresentValue"));
			int toUse = present;
			if(present == 0)
				return;
			
			if(hpRuby) {
				int toHeal = (int) (p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - p.getHealth());
				if(toHeal < 0)
					return;
				if(toHeal < present)
					toUse = toHeal;
				present -= toUse;
				
				RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, toUse);
				Bukkit.getPluginManager().callEvent(event);
				
				dust = new DustOptions(Color.RED, 1f);
				volume = 0.8f;
				sound = Sound.BLOCK_END_PORTAL_SPAWN;
				lore.add("§cZycie: §7"+present+"/"+nbt.getString("MaxValue"));
			} else {
				int toRegen = rpg.getStats().getFinalMana() - rpg.getStats().getPresentMana();
				if(toRegen < 0)
					return;
				if(toRegen < present)
					toUse = toRegen;
				present -= toUse;
				
				rpg.getStats().addPresentManaSmart(present);
				
				dust = new DustOptions(Color.BLUE, 1f);
				volume = 1.3f;
				sound = Sound.BLOCK_END_PORTAL_SPAWN;
				lore.add("§9Mana: §7"+present+"/"+nbt.getString("MaxValue"));
			}
			
			nbt.setString("PresentValue", present+"");
		} else {
			int max = Integer.parseInt(nbt.getString("MaxValue"));
			int present = Integer.parseInt(nbt.getString("PresentValue"));
			if(present == max) 
				return;
			
			int percent = max/100;
			int diff = max-present;
			
			int toAdd;
			if(percent > diff)
				toAdd = diff;
			else 
				toAdd = percent;
			
			if(hpRuby) {
				if((p.getHealth() - 5) < toAdd)
					return;
				
				RubyStorageEvent storeEvent = new RubyStorageEvent(p, toAdd, toAdd, true);
				Bukkit.getPluginManager().callEvent(storeEvent);
				if(storeEvent.isCancelled()) {
					e.setCancelled(true);
					return;
				}
				
				if(storeEvent.getUse() > 0) {
					EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(p, p, DamageCause.CONTACT, storeEvent.getUse());
					Bukkit.getPluginManager().callEvent(event);
				}

				p.setHealth(p.getHealth()-storeEvent.getUse());
				p.playEffect(EntityEffect.HURT);

				dust = new DustOptions(Color.RED, 1.5f);
				volume = 1.3f;
				sound = Sound.ENTITY_GENERIC_EXTINGUISH_FIRE;
				
				toAdd = (int) storeEvent.getStore();
				if(max < present + toAdd)
					toAdd = max - present;
				lore.add("§cZycie: §7"+(present+toAdd)+"/"+max);
			} else {
				if((rpg.getStats().getPresentMana() + 5) < toAdd)
					return;
				
				RubyStorageEvent storeEvent = new RubyStorageEvent(p, toAdd, toAdd, false);
				Bukkit.getPluginManager().callEvent(storeEvent);
				if(storeEvent.isCancelled()) {
					e.setCancelled(true);
					return;
				}

				if(storeEvent.getUse() > 0)
					rpg.getStats().removePresentManaSmart((int) storeEvent.getUse());
				
				dust = new DustOptions(Color.BLUE, 1.5f);
				volume = 0.8f;
				sound = Sound.BLOCK_ENCHANTMENT_TABLE_USE;
				
				toAdd = (int) storeEvent.getStore();
				if(max < present + toAdd)
					toAdd = max - present;
				lore.add("§9Mana: §7"+(present+toAdd)+"/"+max);
			}
			nbt.setString("PresentValue", (present+toAdd)+"");
		}

		p.playSound(p.getLocation(), sound, 1f, volume);
		p.spawnParticle(Particle.REDSTONE, p.getLocation().add(0, 1, 0), 30, 1f, 1f, 1f, 1f, dust);
		
		nbt.applyNBT(ruby);
		ItemMeta im = ruby.getItemMeta();
		im.setLore(lore);
		ruby.setItemMeta(im);
		
		e.setUseInteractedBlock(Result.DENY);
		e.setUseItemInHand(Result.DENY);
		
		p.updateInventory();
		return;
	}
	
}
