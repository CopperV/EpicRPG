package me.Vark123.EpicRPG.MenuSystem;

import org.bukkit.entity.Player;

import lombok.Getter;
import me.Vark123.EpicRPG.Backpacks.BackpackManager;
import me.Vark123.EpicRPG.Gems.GemManager;
import me.Vark123.EpicRPG.Klejnoty.KlejnotyManager;
import me.Vark123.EpicRPG.KosturSystem.KosturMenuManager;
import me.Vark123.EpicRPG.RubySystem.RubyMenuManager;
import me.Vark123.EpicRPG.Stats.ResetStatsMenuManager;
import me.Vark123.EpicRPG.SzponBeliara.SzponBeliaraManager;

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
			case 4:
				KlejnotyManager.getInstance().openInsertMenu(p);
				return true;
			case 6:
				SzponBeliaraManager.getInstance().openSzponAwakeningMenu(p);
				return true;
			case 7:
				KlejnotyManager.getInstance().openRemoveMenu(p);
				return true;
			case 8:
				KosturMenuManager.getInstance().openMenu(p);
				return true;
			case 9:
				ResetStatsMenuManager.getInstance().open(p);
				return true;
			case 10:
				RubyMenuManager.getInstance().openWarpedMenu(p);
				return true;
			case 11:
				RubyMenuManager.getInstance().openKyrianMenu(p);
				return true;
			case 12:
				KosturMenuManager.getInstance().openCreateMenu(p);
				return true;
		}
		return false;
	}
	
}
