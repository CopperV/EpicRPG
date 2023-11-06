package me.Vark123.EpicRPG.Options.Menus;

import java.util.Arrays;

import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.rysefoxx.inventory.anvilgui.AnvilGUI.Builder;
import io.github.rysefoxx.inventory.anvilgui.AnvilGUI.ResponseAction;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.InventoryOpenerType;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import me.Vark123.EpicOptions.MenuSystem.OptionMenuManager;
import me.Vark123.EpicOptions.PlayerSystem.OPlayer;
import me.Vark123.EpicOptions.PlayerSystem.PlayerOption;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Options.Serializables.MarkerSerializable;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class MarkerOptionMenuManager {
	
	private static final MarkerOptionMenuManager inst = new MarkerOptionMenuManager();

	private final ItemStack empty;
	private final ItemStack back;
	
	private final ItemStack remove;
	private final ItemStack create;

	private final ItemStack set;

	private final ItemStack show;
	private final ItemStack hide;
	
	private MarkerOptionMenuManager() {
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

		remove = new ItemStack(Material.STONE_SHOVEL, 1);{
			ItemMeta im = remove.getItemMeta();
			im.setDisplayName("§cUSUN");
			remove.setItemMeta(im);
		}
		create = new ItemStack(Material.IRON_PICKAXE, 1);{
			ItemMeta im = create.getItemMeta();
			im.setDisplayName("§eSTWORZ");
			create.setItemMeta(im);
		}

		set = new ItemStack(Material.FEATHER, 1);{
			ItemMeta im = set.getItemMeta();
			im.setDisplayName("§2USTAW");
			set.setItemMeta(im);
		}

		show = new ItemStack(Material.TORCH, 1);{
			ItemMeta im = show.getItemMeta();
			im.setDisplayName("§7POKAZ");
			show.setItemMeta(im);
		}
		hide = new ItemStack(Material.REDSTONE_TORCH, 1);{
			ItemMeta im = hide.getItemMeta();
			im.setDisplayName("§7UKRYJ");
			hide.setItemMeta(im);
		}
	}
	
	public static final MarkerOptionMenuManager get() {
		return inst;
	}
	
	public void openMenu(OPlayer op) {
		Player p = op.getPlayer();
		@SuppressWarnings("unchecked")
		PlayerOption<MarkerSerializable> option = (PlayerOption<MarkerSerializable>) op.getPlayerOptionByID("epicrpg_markers").orElseThrow();
	
		RyseInventory.builder()
			.title("§7§lUSTAWIENIA - §6§lZNACZNIK")
			.disableUpdateTask()
			.rows(2)
			.provider(new InventoryProvider() {
				@Override
				public void init(Player player, InventoryContents contents) {
					for(int i = 0; i < 18; ++i)
						contents.set(i, empty);
					
					contents.set(8, IntelligentItem.of(back, e -> OptionMenuManager.get().openMenu(p)));
					
					MarkerSerializable marker = option.getValue();
					if(!marker.isCreated()) {
						contents.set(9, IntelligentItem.of(create, e -> openXMarkerCreator(op)));
						return;
					}

					RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
					contents.set(9, IntelligentItem.of(remove, e -> {
						rpg.getMarker().hideMarker();
						marker.setCreated(false);
						p.closeInventory();
					}));
					
					contents.set(10, IntelligentItem.of(set, e -> openXMarkerCreator(op)));
					
					contents.set(17, IntelligentItem.of(marker.isShowed() ? hide : show, e -> {
						if(marker.isShowed())
							rpg.getMarker().hideMarker();
						else
							rpg.getMarker().showMarker();
						
						marker.setShowed(!marker.isShowed());
						p.closeInventory();
					}));
					
					ItemStack info = new ItemStack(Material.PAPER);
					ItemMeta im = info.getItemMeta();
					im.setDisplayName("§6§lAKTUALNY ZNACZNIK");
					im.setLore(Arrays.asList(
							" ",
							"§aSwiat: §7"+marker.getWorld(),
							"§aKoordy: §7["+String.format("%.2f", marker.getX())+";"+String.format("%.2f", marker.getZ())+"]"));
					info.setItemMeta(im);
					contents.set(4, info);
				}
			})
			.build(Main.getInstance())
			.open(p);
	}
	
	private void openXMarkerCreator(OPlayer op) {
		Player p = op.getPlayer();

		RyseInventory.builder()
			.title("§7§lUSTAWIENIA - §6§lZNACZNIK")
			.disableUpdateTask()
			.type(InventoryOpenerType.ANVIL)
			.provider(new InventoryProvider() {
				@Override
				public void anvil(Player player, Builder anvil) {
					ItemStack it = new ItemStack(Material.PAPER);
					ItemMeta im = it.getItemMeta();
					im.setDisplayName("USTAW ZNACZNIK - [X]");
					it.setItemMeta(im);
					
					anvil.itemLeft(it);
					anvil.onClick((slot, _anvil) -> {
						if(slot != 2)
							return Arrays.asList();
						
						ItemStack result = _anvil.getOutputItem();
						if(result == null || result.getType().equals(Material.AIR))
							return Arrays.asList();
						
						String text = _anvil.getText();
						if(!NumberUtils.isCreatable(text))
							return Arrays.asList();
						
						double x = Double.parseDouble(text);
						openZMarkerCreator(op, x);
						return Arrays.asList();
					});
				}
			})
			.build(Main.getInstance())
			.open(p);
	}
	
	private void openZMarkerCreator(OPlayer op, double x) {
		Player p = op.getPlayer();
		@SuppressWarnings("unchecked")
		PlayerOption<MarkerSerializable> option = (PlayerOption<MarkerSerializable>) op.getPlayerOptionByID("epicrpg_markers").orElseThrow();

		RyseInventory.builder()
			.title("§7§lUSTAWIENIA - §6§lZNACZNIK")
			.disableUpdateTask()
			.type(InventoryOpenerType.ANVIL)
			.provider(new InventoryProvider() {
				@Override
				public void anvil(Player player, Builder anvil) {
					ItemStack it = new ItemStack(Material.PAPER);
					ItemMeta im = it.getItemMeta();
					im.setDisplayName("USTAW ZNACZNIK - [Z]");
					it.setItemMeta(im);
					
					anvil.itemLeft(it);
					anvil.onClick((slot, _anvil) -> {
						if(slot != 2)
							return Arrays.asList();
						
						ItemStack result = _anvil.getOutputItem();
						if(result == null || result.getType().equals(Material.AIR))
							return Arrays.asList();
						
						String text = _anvil.getText();
						if(!NumberUtils.isCreatable(text))
							return Arrays.asList();
						
						double z = Double.parseDouble(text);
						
						MarkerSerializable marker = option.getValue();
						marker.setX(x);
						marker.setZ(z);
						marker.setWorld(p.getWorld().getName());
						marker.setCreated(true);
						marker.setShowed(true);
						
						RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
						rpg.getMarker().createMarker();
						
						return Arrays.asList(ResponseAction.close());
					});
				}
			})
			.build(Main.getInstance())
			.open(p);
	}

}
