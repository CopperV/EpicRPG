package me.Vark123.EpicRPG.HorseSystem;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;

@Getter
public class HorseMenuEvents {

	private static final HorseMenuEvents container = new HorseMenuEvents();
	
	private final EventCreator<InventoryClickEvent> clickEvent;
	
	private HorseMenuEvents() {
		clickEvent = clickEventCreator();
	}
	
	public static final HorseMenuEvents getEvents() {
		return container;
	}
	
	private EventCreator<InventoryClickEvent> clickEventCreator(){
		Consumer<InventoryClickEvent> event = e -> {
			if(e.isCancelled())
				return;
			
			ItemStack it = e.getCurrentItem();
			if(it == null || it.getType().equals(Material.AIR))
				return;
			
			NBTItem nbt = new NBTItem(it);
			if(!nbt.hasTag("MountSummonClass"))
				return;
			
			String strClass = nbt.getString("MountSummonClass");
			Class<?> _class;
			Constructor<?> constructor;
			Object obj;
			try {
				_class = Class.forName(strClass);
				constructor = _class.getConstructor();
				obj = constructor.newInstance();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
				return;
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
				return;
			} catch (SecurityException e1) {
				e1.printStackTrace();
				return;
			} catch (InstantiationException e1) {
				e1.printStackTrace();
				return;
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
				return;
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
				return;
			} catch (InvocationTargetException e1) {
				e1.printStackTrace();
				return;
			}
			
			if(!(obj instanceof AEpicHorse))
				return;
			
			Player p = (Player) e.getWhoClicked();
			AEpicHorse horse = (AEpicHorse) obj;
			horse.summonMount(p);
			
			p.closeInventory();
			
			HorseManager.getInstance().getHorseCd().put(p, new Date());
			new BukkitRunnable() {
				
				@Override
				public void run() {
					p.sendMessage(Main.getInstance().getPrefix() + " §a§lMozesz znow przywolac wierzchowca!");
				}
			}.runTaskLater(Main.getInstance(), 20*HorseManager.getInstance().HORSE_SUMMON_CD);
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
}
