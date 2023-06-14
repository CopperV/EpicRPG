package me.Vark123.EpicRPG.MenuSystem;

import org.bukkit.entity.Player;

import lombok.Getter;
import me.Vark123.EpicRPG.Backpacks.BackpackManager;
import me.Vark123.EpicRPG.FishSystem.FishSellManager;
import me.Vark123.EpicRPG.Gems.GemManager;
import me.Vark123.EpicRPG.Jewelry.JewelryMenuManager;
import me.Vark123.EpicRPG.Klejnoty.KlejnotyManager;
import me.Vark123.EpicRPG.KosturSystem.KosturMenuManager;
import me.Vark123.EpicRPG.MMExtension.RepairSystem.MMRepairManager;
import me.Vark123.EpicRPG.MerchantSystem.MerchantManager;
import me.Vark123.EpicRPG.RubySystem.RubyManager;
import me.Vark123.EpicRPG.RuneSystem.Runes.KamiennyObserwator;
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
		p.closeInventory();
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
				RubyManager.getInstance().openWarpedMenu(p);
				return true;
			case 11:
				RubyManager.getInstance().openKyrianMenu(p);
				return true;
			case 12:
				KosturMenuManager.getInstance().openCreateMenu(p);
				return true;
			case 13:
				FishSellManager.getInstance().open(p);
				return true;
			case 14:
				MMRepairManager.getInstance().open(p);
				return true;
			case 15:
				JewelryMenuManager.getInstance().openMenu(p);
				return true;
			case 16:
				BackpackManager.getInstance().openBackpackRepairMenu(p);
				return true;
			case 17:
				KamiennyObserwator.openRyseMenu(p);
				return true;
			case 18:
				MerchantManager.getInstance().openSellMenu(p);
				return true;
		}
		return false;
	}
	
}
