package me.Vark123.EpicRPG.Options.Menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import me.Vark123.EpicOptions.MenuSystem.OptionMenuManager;
import me.Vark123.EpicOptions.PlayerSystem.OPlayer;
import me.Vark123.EpicOptions.PlayerSystem.PlayerOption;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Options.Serializables.ResourcesInfoSerializable;

public class ResourceInfoOptionMenuManager {
	
	private static final ResourceInfoOptionMenuManager inst = new ResourceInfoOptionMenuManager();

	private final ItemStack empty;
	private final ItemStack back;
	
	private final ItemStack onTemplate;
	private final ItemStack offTemplate;
	
	private ResourceInfoOptionMenuManager() {
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		back = new ItemStack(Material.BARRIER, 1);{
			ItemMeta im = back.getItemMeta();
			im.setDisplayName("§cPOWROT");
			back.setItemMeta(im);
		}

		onTemplate = new ItemStack(Material.GREEN_TERRACOTTA);{
			ItemMeta im = onTemplate.getItemMeta();
			im.setDisplayName(": §a§lWLACZONE");
			onTemplate.setItemMeta(im);
		}
		offTemplate = new ItemStack(Material.RED_TERRACOTTA);{
			ItemMeta im = offTemplate.getItemMeta();
			im.setDisplayName(": §c§lWYLACZONE");
			offTemplate.setItemMeta(im);
		}
	}
	
	public static final ResourceInfoOptionMenuManager get() {
		return inst;
	}
	
	public void openMenu(OPlayer op) {
		Player p = op.getPlayer();
		@SuppressWarnings("unchecked")
		PlayerOption<ResourcesInfoSerializable> option = (PlayerOption<ResourcesInfoSerializable>) op.getPlayerOptionByID("epicrpg_resources").orElseThrow();
	
		RyseInventory.builder()
			.title("§7§lUSTAWIENIA - §b§lPOWIADOMIENIA")
			.disableUpdateTask()
			.rows(2)
			.provider(new InventoryProvider() {
				@Override
				public void init(Player player, InventoryContents contents) {
					for(int i = 0; i < 18; ++i)
						contents.set(i, empty);
					
					contents.set(8, IntelligentItem.of(back, e -> OptionMenuManager.get().openMenu(p)));
					
					ResourcesInfoSerializable info = option.getValue();
					{
						ItemStack it = info.isExpInfo() ? onTemplate.clone() : offTemplate.clone();
						ItemMeta im = it.getItemMeta();
						String display = "§eDoswiadczenie"+im.getDisplayName();
						im.setDisplayName(display);
						it.setItemMeta(im);
						
						contents.set(9, IntelligentItem.of(it, e -> {
							info.setExpInfo(!info.isExpInfo());
							p.closeInventory();
						}));
					}
					{
						ItemStack it = info.isMoneyInfo() ? onTemplate.clone() : offTemplate.clone();
						ItemMeta im = it.getItemMeta();
						String display = "§6Kasa"+im.getDisplayName();
						im.setDisplayName(display);
						it.setItemMeta(im);
						
						contents.set(10, IntelligentItem.of(it, e -> {
							info.setMoneyInfo(!info.isMoneyInfo());
							p.closeInventory();
						}));
					}
					{
						ItemStack it = info.isStygiaInfo() ? onTemplate.clone() : offTemplate.clone();
						ItemMeta im = it.getItemMeta();
						String display = "§3Stygia"+im.getDisplayName();
						im.setDisplayName(display);
						it.setItemMeta(im);
						
						contents.set(11, IntelligentItem.of(it, e -> {
							info.setStygiaInfo(!info.isStygiaInfo());
							p.closeInventory();
						}));
					}
					{
						ItemStack it = info.isCoinsInfo() ? onTemplate.clone() : offTemplate.clone();
						ItemMeta im = it.getItemMeta();
						String display = "§4Smocze monety"+im.getDisplayName();
						im.setDisplayName(display);
						it.setItemMeta(im);
						
						contents.set(12, IntelligentItem.of(it, e -> {
							info.setCoinsInfo(!info.isCoinsInfo());
							p.closeInventory();
						}));
					}
					{
						ItemStack it = info.isRudaInfo() ? onTemplate.clone() : offTemplate.clone();
						ItemMeta im = it.getItemMeta();
						String display = "§9Brylki rudy"+im.getDisplayName();
						im.setDisplayName(display);
						it.setItemMeta(im);
						
						contents.set(13, IntelligentItem.of(it, e -> {
							info.setRudaInfo(!info.isRudaInfo());
							p.closeInventory();
						}));
					}
					{
						ItemStack it = info.isClanExpInfo() ? onTemplate.clone() : offTemplate.clone();
						ItemMeta im = it.getItemMeta();
						String display = "§e§oDoswiadczenie klanowe"+im.getDisplayName();
						im.setDisplayName(display);
						it.setItemMeta(im);
						
						contents.set(14, IntelligentItem.of(it, e -> {
							info.setClanExpInfo(!info.isClanExpInfo());
							p.closeInventory();
						}));
					}
				}
			})
			.build(Main.getInstance())
			.open(p);
	}

}
