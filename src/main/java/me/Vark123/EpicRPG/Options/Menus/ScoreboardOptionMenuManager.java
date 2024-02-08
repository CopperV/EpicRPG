package me.Vark123.EpicRPG.Options.Menus;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.Bukkit;
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
import me.Vark123.EpicRPG.RpgScoreboard;
import me.Vark123.EpicRPG.Options.Serializables.ScoreboardSerializable;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.clip.placeholderapi.PlaceholderAPI;

public final class ScoreboardOptionMenuManager {

	private static final ScoreboardOptionMenuManager inst = new ScoreboardOptionMenuManager();

	private final ItemStack empty;
	private final ItemStack back;
	
	private final ItemStack[] lines;

	private final ItemStack on;
	private final ItemStack off;
	private final ItemStack reset;
	private final ItemStack add;

	private final ItemStack changeLine;
	private final ItemStack removeLine;
	private final Map<String, ItemStack> scoreboardValues;
	private final ItemStack customLine;
	
	private ScoreboardOptionMenuManager() {
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
		
		changeLine = new ItemStack(Material.WRITABLE_BOOK, 1);{
			ItemMeta im = changeLine.getItemMeta();
			im.setDisplayName("§eZMIEN");
			changeLine.setItemMeta(im);
		}
		removeLine = new ItemStack(Material.BARRIER, 1);{
			ItemMeta im = removeLine.getItemMeta();
			im.setDisplayName("§cUSUN");
			removeLine.setItemMeta(im);
		}
		
		ItemStack lineTemplate = new ItemStack(Material.PAPER);
		lines = new ItemStack[15];
		for(int i = 0; i < lines.length; ++i) {
			ItemStack it = lineTemplate.clone();
			ItemMeta im = it.getItemMeta();
			im.setDisplayName("§2Linia §o"+(i+1));
			it.setItemMeta(im);
			lines[i] = it;
		}
		
		on = new ItemStack(Material.GREEN_TERRACOTTA);{
			ItemMeta im = on.getItemMeta();
			im.setDisplayName("§a§lWLACZONY");
			on.setItemMeta(im);
		}
		off = new ItemStack(Material.RED_TERRACOTTA);{
			ItemMeta im = off.getItemMeta();
			im.setDisplayName("§c§lWYLACZONY");
			off.setItemMeta(im);
		}
		reset = new ItemStack(Material.CLOCK);{
			ItemMeta im = reset.getItemMeta();
			im.setDisplayName("§e§lRESETUJ DO USTAWIEN DOMYSLNYCH");
			reset.setItemMeta(im);
		}
		add = new ItemStack(Material.EMERALD);{
			ItemMeta im = add.getItemMeta();
			im.setDisplayName("§2§lDODAJ NOWA POZYCJE");
			add.setItemMeta(im);
		}
		
		List<String> scoreboardPlaceholdedrs = new LinkedList<>(Arrays.asList(
				" ",
				"%epicscoreboard_info%",
				"%epicscoreboard_resources%",
				"%epicscoreboard_stats%",
				"%epicscoreboard_nick%",
				"%epicscoreboard_klasa%",
				"%epicscoreboard_level%",
				"%epicscoreboard_exp%",
				"%epicscoreboard_percent_exp%",
				"%epicscoreboard_present_exp%",
				"%epicscoreboard_nextLevel_exp%",
				"%epicscoreboard_pn%",
				"%epicscoreboard_money%",
				"%epicscoreboard_stygia%",
				"%epicscoreboard_coins%",
				"%epicscoreboard_ruda%",
				"%epicscoreboard_str%",
				"%epicscoreboard_wytrz%",
				"%epicscoreboard_zr%",
				"%epicscoreboard_zd%",
				"%epicscoreboard_walka%",
				"%epicscoreboard_krag%",
				"%epicscoreboard_int%",
				"%epicscoreboard_mana%",
				"%epicscoreboard_present_mana%",
				"%epicscoreboard_max_mana%",
				"%epicscoreboard_hp%",
				"%epicscoreboard_percent_hp%"));
		scoreboardValues = new LinkedHashMap<>();
		scoreboardPlaceholdedrs.parallelStream().forEach(placeholder -> {
			ItemStack it = new ItemStack(Material.PAPER);
			ItemMeta im = it.getItemMeta();
			im.setDisplayName(placeholder);
			it.setItemMeta(im);
			scoreboardValues.put(placeholder, it);
		});
		customLine = new ItemStack(Material.PAPER);{
			ItemMeta im = customLine.getItemMeta();
			im.setDisplayName("§7§lWLASNY TEKST");
			customLine.setItemMeta(im);
		}
		
	}
	
	public static final ScoreboardOptionMenuManager get() {
		return inst;
	}
	
	public void openMenu(OPlayer op) {
		Player p = op.getPlayer();
		@SuppressWarnings("unchecked")
		PlayerOption<ScoreboardSerializable> option = (PlayerOption<ScoreboardSerializable>) op.getPlayerOptionByID("epicrpg_scoreboard").orElseThrow();
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		
		RyseInventory.builder()
//			.title("§7§lUSTAWIENIA - §e§lSCOREBOARD")
			.disableUpdateTask()
			.rows(3)
			.provider(new InventoryProvider() {
				@Override
				public void init(Player player, InventoryContents contents) {
					for(int i = 0; i < 27; ++i)
						contents.set(i, empty);
					
					if(option.getValue().isEnabled())
						contents.set(0, IntelligentItem.of(on, e -> {
							p.closeInventory();
							p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
							option.getValue().setEnabled(false);
						}));
					else 
						contents.set(0, IntelligentItem.of(off, e -> {
							p.closeInventory();
							p.setScoreboard(rpg.getBoard());
							option.getValue().setEnabled(true);
						}));
					contents.set(1, IntelligentItem.of(reset, e -> {
						p.closeInventory();
						option.getValue().setLines(new LinkedList<>(ScoreboardSerializable.DEFAULT_VALUES));
					}));
					
					if(option.getValue().getLines().size() < 15)
						contents.set(2, IntelligentItem.of(add, e -> {
							openNewLineMenu(op);
						}));
					
					contents.set(8, IntelligentItem.of(back, e -> OptionMenuManager.get().openMenu(p)));
					
					List<String> lines = option.getValue().getLines();
					for(int i = 0; i < lines.size(); ++i) {
						ItemStack it = ScoreboardOptionMenuManager.this.lines[i].clone();
						ItemMeta im = it.getItemMeta();
						String line = lines.get(i);
						im.setLore(Arrays.asList(" ",PlaceholderAPI.setPlaceholders(player, line)));
						it.setItemMeta(im);
						
						MutableInt index = new MutableInt(i);
						contents.set(i+9, IntelligentItem.of(it, e -> {
							openLineEditorMenu(op, index.getValue());
							p.closeInventory();
							return;
						}));
					}
				}
			})
			.build(Main.getInstance())
			.open(p);
	}
	
	private void openLineEditorMenu(OPlayer op, int lineNum) {
		Player p = op.getPlayer();
		@SuppressWarnings("unchecked")
		PlayerOption<ScoreboardSerializable> option = (PlayerOption<ScoreboardSerializable>) op.getPlayerOptionByID("epicrpg_scoreboard").orElseThrow();
		
		RyseInventory.builder()
			.title("§7§lUSTAWIENIA - §e§lSCOREBOARD")
			.disableUpdateTask()
			.rows(2)
			.provider(new InventoryProvider() {
				@Override
				public void init(Player player, InventoryContents contents) {
					for(int i = 0; i < 18; ++i)
						contents.set(i, empty);
					
					contents.set(8, IntelligentItem.of(back, e -> openMenu(op)));

					List<String> lines = option.getValue().getLines();
					{
						ItemStack it = ScoreboardOptionMenuManager.this.lines[lineNum].clone();
						ItemMeta im = it.getItemMeta();
						String line = lines.get(lineNum);
						im.setLore(Arrays.asList(" ",PlaceholderAPI.setPlaceholders(player, line)));
						it.setItemMeta(im);
						
						contents.set(4, it);
					}
					
					contents.set(9, IntelligentItem.of(changeLine, e -> {
						openLineChangeMenu(op, lineNum);
					}));
					contents.set(10, IntelligentItem.of(removeLine, e -> {
						p.closeInventory();
						option.getValue().getLines().remove(lineNum);
					}));
				}
			})
			.build(Main.getInstance())
			.open(p);
	}
	
	private void openLineChangeMenu(OPlayer op, int lineNum) {
		Player p = op.getPlayer();
		@SuppressWarnings("unchecked")
		PlayerOption<ScoreboardSerializable> option = (PlayerOption<ScoreboardSerializable>) op.getPlayerOptionByID("epicrpg_scoreboard").orElseThrow();
		
		RyseInventory.builder()
			.title("§7§lUSTAWIENIA - §e§lSCOREBOARD")
			.rows(5)
			.disableUpdateTask()
			.provider(new InventoryProvider() {
				@Override
				public void init(Player player, InventoryContents contents) {
					for(int i = 0; i < 45; ++i)
						contents.set(i, empty);

					contents.set(8, IntelligentItem.of(back, e -> openLineEditorMenu(op, lineNum)));
					
					MutableInt index = new MutableInt();
					scoreboardValues.entrySet().stream()
						.filter(entry -> {
							return !entry.getKey().equals(option.getValue().getLines().get(lineNum));
						})
						.forEach(entry -> {
							ItemStack it = entry.getValue().clone();
							
							ItemMeta im = it.getItemMeta();
							final String text = im.getDisplayName();
							im.setDisplayName(PlaceholderAPI.setPlaceholders(p, text));
							it.setItemMeta(im);
							
							contents.set(index.getAndIncrement()+9, IntelligentItem.of(it, e -> {
								p.closeInventory();
								option.getValue().getLines().set(lineNum, text);
								RpgScoreboard.updateScore(p);
							}));
						});
					
					contents.set(index.getValue()+9, IntelligentItem.of(customLine, e -> {
						openCustomTextMenu(op, lineNum);
					}));
				}
			})
			.build(Main.getInstance())
			.open(p);
	}
	
	private void openCustomTextMenu(OPlayer op, int lineNum) {
		Player p = op.getPlayer();
		@SuppressWarnings("unchecked")
		PlayerOption<ScoreboardSerializable> option = (PlayerOption<ScoreboardSerializable>) op.getPlayerOptionByID("epicrpg_scoreboard").orElseThrow();
		
		RyseInventory.builder()
			.title("§7§lUSTAWIENIA - §e§lSCOREBOARD")
			.disableUpdateTask()
			.type(InventoryOpenerType.ANVIL)
			.provider(new InventoryProvider() {
				@Override
				public void anvil(Player player, Builder anvil) {
					ItemStack it = new ItemStack(Material.PAPER);
					ItemMeta im = it.getItemMeta();
//					im.setDisplayName("TWOJ WLASNY TEKST");
					String odlText = option.getValue().getLines().get(lineNum).replace('§', '&');
					im.setDisplayName(odlText);
					it.setItemMeta(im);
					
					anvil.itemLeft(it);
					anvil.onClick((slot, _anvil) -> {
						if(slot != 2)
							return Arrays.asList();
						
						ItemStack result = _anvil.getOutputItem();
						if(result == null || result.getType().equals(Material.AIR))
							return Arrays.asList();
						String text = _anvil.getText();
						
						option.getValue().getLines().set(lineNum, text);
						return Arrays.asList(ResponseAction.close());
					});
				}
			})
			.build(Main.getInstance())
			.open(p);
	}
	
	private void openCustomTextMenu(OPlayer op) {
		Player p = op.getPlayer();
		@SuppressWarnings("unchecked")
		PlayerOption<ScoreboardSerializable> option = (PlayerOption<ScoreboardSerializable>) op.getPlayerOptionByID("epicrpg_scoreboard").orElseThrow();
		
		RyseInventory.builder()
			.title("§7§lUSTAWIENIA - §e§lSCOREBOARD")
			.disableUpdateTask()
			.type(InventoryOpenerType.ANVIL)
			.provider(new InventoryProvider() {
				@Override
				public void anvil(Player player, Builder anvil) {
					ItemStack it = new ItemStack(Material.PAPER);
					ItemMeta im = it.getItemMeta();
					im.setDisplayName("TWOJ WLASNY TEKST");
					it.setItemMeta(im);
					
					anvil.itemLeft(it);
					anvil.onClick((slot, _anvil) -> {
						if(slot != 2)
							return Arrays.asList();
						
						ItemStack result = _anvil.getOutputItem();
						if(result == null || result.getType().equals(Material.AIR))
							return Arrays.asList();
						String text = _anvil.getText();
						
						option.getValue().getLines().add(text);
						return Arrays.asList(ResponseAction.close());
					});
				}
			})
			.build(Main.getInstance())
			.open(p);
	}
	
	private void openNewLineMenu(OPlayer op) {
		Player p = op.getPlayer();
		@SuppressWarnings("unchecked")
		PlayerOption<ScoreboardSerializable> option = (PlayerOption<ScoreboardSerializable>) op.getPlayerOptionByID("epicrpg_scoreboard").orElseThrow();
		
		RyseInventory.builder()
			.title("§7§lUSTAWIENIA - §e§lSCOREBOARD")
			.rows(5)
			.disableUpdateTask()
			.provider(new InventoryProvider() {
				@Override
				public void init(Player player, InventoryContents contents) {
					for(int i = 0; i < 45; ++i)
						contents.set(i, empty);

					contents.set(8, IntelligentItem.of(back, e -> openMenu(op)));
					
					MutableInt index = new MutableInt();
					scoreboardValues.entrySet().stream()
						.forEach(entry -> {
							ItemStack it = entry.getValue().clone();
							
							ItemMeta im = it.getItemMeta();
							final String text = im.getDisplayName();
							im.setDisplayName(PlaceholderAPI.setPlaceholders(p, text));
							it.setItemMeta(im);
							
							contents.set(index.getAndIncrement()+9, IntelligentItem.of(it, e -> {
								p.closeInventory();
								option.getValue().getLines().add(text);
								RpgScoreboard.updateScore(p);
							}));
						});
					
					contents.set(index.getValue()+9, IntelligentItem.of(customLine, e -> {
						openCustomTextMenu(op);
					}));
				}
			})
			.build(Main.getInstance())
			.open(p);
	}
	
}
