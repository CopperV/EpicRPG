package me.Vark123.EpicRPG.RuneSystem.Events;

import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.other.EventCreator;

public class KamiennyObserwatorEvent implements Listener {
	
	public static EventCreator<InventoryClickEvent> getClickEvent(){
		Consumer<InventoryClickEvent> event = e -> {
			ItemStack clock = e.getCurrentItem();
			if(clock == null || !clock.getType().equals(Material.CLOCK))
				return;
			
			NBTItem nbt = new NBTItem(clock);
			if(!nbt.hasTag("clock_time"))
				return;
			
			Player p = (Player) e.getWhoClicked();
			Location loc = p.getLocation();
			int time = nbt.getInteger("clock_time");
			p.getWorld().setTime(time);
			p.getWorld().playSound(loc, Sound.ITEM_TOTEM_USE, 1, 1);
			p.spawnParticle(Particle.TOTEM, loc.clone().add(0,1,0),
					25, 0.6, 0.6, 0.6, 0.2);
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 0));
			
			p.closeInventory();
		};

		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
	@Deprecated
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		
	}

}
