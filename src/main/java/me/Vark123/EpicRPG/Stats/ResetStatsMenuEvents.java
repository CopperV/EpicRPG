package me.Vark123.EpicRPG.Stats;

import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import lombok.Getter;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

@Getter
public class ResetStatsMenuEvents {

	private static final ResetStatsMenuEvents container = new ResetStatsMenuEvents();
	
	private final EventCreator<InventoryClickEvent> clickEvent;
	
	private ResetStatsMenuEvents() {
		clickEvent = clickEventCreator();
	}
	
	public static final ResetStatsMenuEvents getEvents() {
		return container;
	}
	
	private EventCreator<InventoryClickEvent> clickEventCreator() {
		Consumer<InventoryClickEvent> event = e -> {
			if(!e.getClickedInventory().getType().equals(InventoryType.CHEST))
				return;
			
			int slot = e.getSlot();
			if(slot != 2) {
				if(slot == 6) {
					e.getWhoClicked().closeInventory();
				}
				return;
			}
			
			Player p = (Player) e.getWhoClicked();
			if(!PlayerManager.getInstance().playerExists(p)) {
				p.closeInventory();
				return;
			}
			
			RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
			ResetStatsEvent ev = new ResetStatsEvent(rpg);
			Bukkit.getPluginManager().callEvent(ev);
			if(!ev.isCancelled())
				rpg.resetStats();
			p.closeInventory();
			return;
		};
		
		EventCreator<InventoryClickEvent> creator = new EventCreator<>(InventoryClickEvent.class, event);
		return creator;
	}
	
}
