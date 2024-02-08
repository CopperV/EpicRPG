package me.Vark123.EpicRPG.API;

import org.bukkit.Bukkit;

import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager;
import lombok.Getter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Chat.ChatManager;
import me.Vark123.EpicRPG.Core.CoinsSystem;
import me.Vark123.EpicRPG.Core.ExpSystem;
import me.Vark123.EpicRPG.Core.MoneySystem;
import me.Vark123.EpicRPG.Core.StygiaSystem;
import me.Vark123.EpicRPG.FightSystem.DamageManager;
import me.Vark123.EpicRPG.HorseSystem.HorseManager;
import me.Vark123.EpicRPG.MenuSystem.MenuManager;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Potions.PotionManager;
import me.Vark123.EpicRPG.RuneSystem.RuneManager;
import me.nikl.calendarevents.CalendarEvents;
import me.nikl.calendarevents.CalendarEventsApi;

@Getter
public class EpicRPGApi {

	private static final EpicRPGApi inst = new EpicRPGApi();
	
	private ChatManager chatManager;
	private DamageManager damageManager;
	private PlayerManager playerManager;
	private PotionManager potionManager;
	private RuneManager runeManager;
	private InventoryManager inventoryManager;
	private MenuManager menuManager;
	private HorseManager horseManager;
	
	private CoinsSystem coinsSystem;
	private ExpSystem expSystem;
	private MoneySystem moneySystem;
	private StygiaSystem stygiaSystem;

	private CalendarEventsApi calendarManager;
	
	private EpicRPGApi() {
		this.chatManager = ChatManager.getInstance();
		this.damageManager = DamageManager.getInstance();
		this.playerManager = PlayerManager.getInstance();
		this.potionManager = PotionManager.getInstance();
		this.runeManager = RuneManager.getInstance();
		this.inventoryManager = Main.getInstance().getManager();
		this.menuManager = MenuManager.getInstance();
		this.horseManager = HorseManager.getInstance();
		
		this.coinsSystem = CoinsSystem.getInstance();
		this.expSystem = ExpSystem.getInstance();
		this.moneySystem = MoneySystem.getInstance();
		this.stygiaSystem = StygiaSystem.getInstance();
		
		this.calendarManager = ((CalendarEvents) Bukkit.getPluginManager().getPlugin("CalendarEvents")).getApi();
	}
	
	public static EpicRPGApi getApi() {
		return inst;
	}
	
}
