package me.Vark123.EpicRPG.MenuSystem;

import org.bukkit.entity.Player;

import lombok.Getter;
import me.Vark123.EpicRPG.Backpacks.BackpackManager;
import me.Vark123.EpicRPG.Gems.GemManager;

@Getter
public class MenuManager {

	private static final MenuManager instance = new MenuManager();
	
	private MenuManager() {}
	
	public static MenuManager getInstance() {
		return instance;
	}
	
	public boolean openMenu(Player p, int id) {
		switch(id) {
			case 1:
				BackpackManager.getInstance().openBackpackCreatorMenu(p);
				return true;
			case 2:
				GemManager.getInstance().openPowerfulGemCreator(p);
				return true;
			case 3:
				GemManager.getInstance().openAnnihilusGemCreator(p);
				return true;
		}
		return false;
	}
	
}
