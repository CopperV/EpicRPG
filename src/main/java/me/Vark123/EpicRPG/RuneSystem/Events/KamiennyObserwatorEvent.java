package me.Vark123.EpicRPG.RuneSystem.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class KamiennyObserwatorEvent implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		//TODO
//		Inventory inv = e.getClickedInventory();
//		if(!inv.getType().equals(InventoryType.CHEST))
//			return;
//		if(inv.getHolder() == null ||
//				!(e.getClickedInventory().getHolder() instanceof KamiennyObserwatorMenu))
//			return;
//		
//		e.setCancelled(true);
//		ItemStack clock = e.getCurrentItem();
//		if(clock == null || !clock.getType().equals(Material.CLOCK))
//			return;
//		
//		NBTItem nbt = new NBTItem(clock);
//		if(!nbt.hasTag("clock_time"))
//			return;
//		
//		Player p = (Player) e.getWhoClicked();
//		Location loc = p.getLocation();
//		int time = nbt.getInteger("clock_time");
//		p.getWorld().setTime(time);
//		p.getWorld().playSound(loc, Sound.ITEM_TOTEM_USE, 1, 1);
//		p.spawnParticle(Particle.TOTEM, loc.clone().add(0,1,0),
//				25, 0.6, 0.6, 0.6, 0.2);
//		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 0));
//		
//		e.getWhoClicked().closeInventory();
	}

}
