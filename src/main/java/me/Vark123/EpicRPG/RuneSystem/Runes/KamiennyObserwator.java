package me.Vark123.EpicRPG.RuneSystem.Runes;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.DisabledEvents;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ARune;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.RuneSystem.Events.KamiennyObserwatorEvent;

public class KamiennyObserwator extends ARune {

	private static final InventoryProvider provider;
	private static final int[] clockSlots;
	private static final ItemStack clock;
	
	static {
		clockSlots = new int[] {1,3,5,7, 10,12,14,16, 19,21,23,25,
				28,30,32,34, 37,39,41,43, 46,48,50,52};
		clock = new ItemStack(Material.CLOCK, 1);
		provider = new InventoryProvider() {
			@Override
			public void init(Player player, InventoryContents contents) {
				for(int i = 0; i < clockSlots.length; ++i) {
					ItemStack newClock = clock.clone();
					
					int hour = i + 1;
					newClock.setAmount(hour);
					int time = hour < 6 ? 24_000 + (hour - 6)*1000 : (hour - 6)*1000;
					NBTItem nbt = new NBTItem(newClock);
					nbt.setInteger("clock_time", time);
					nbt.applyNBT(newClock);
					
					ItemMeta im = newClock.getItemMeta();
					im.setDisplayName("§a§lGODZINA: §e§l"+getTime(hour));
					newClock.setItemMeta(im);
					
					contents.set(clockSlots[i], newClock);
				}
			}
		};
	}
	
	public KamiennyObserwator(ItemStackRune dr, Player p) {
		super(dr, p);
	}

	@Override
	public void castSpell() {
		Location loc = p.getLocation();
		World w = p.getWorld();
		if(!w.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE).booleanValue()) {
			p.sendMessage(Main.getInstance().getPrefix()+" §cW tym swiecie nie mozna zmieniac czasu!");
			p.getWorld().playSound(loc, Sound.ENTITY_VILLAGER_HURT, 2, 0.6f);
			w.spawnParticle(Particle.VILLAGER_ANGRY, loc, 25, 0f, 1f, 0f, 0.1f);
			w.spawnParticle(Particle.SMOKE_LARGE, loc, 25, 0f, 1f, 0f, 0.1f);
			w.spawnParticle(Particle.DAMAGE_INDICATOR, loc, 25, 0f, 1f, 0f, 0.1f);
			return;
		}
		
		p.getWorld().playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1, .5f);
		openRyseMenu(p);
	}
	
	@Deprecated
	public static void openMenu(Player p) {
		Inventory inv = Bukkit.createInventory(new KamiennyObserwatorMenu(), 
				54, "§7§lKamienny obserwator");
		
		ItemStack it = new ItemStack(Material.CLOCK, 1);
		
		for(int row = 0; row < 6; ++row) {
			for(int slot = 1; slot < 5; ++slot) {
				int hour = row*4 + slot;
				ItemStack clock = it.clone();
				clock.setAmount(hour);
				
				int time = hour < 6 ? 24_000 + (hour - 6)*1000 : (hour - 6)*1000;
				NBTItem nbt = new NBTItem(it);
				nbt.setInteger("clock_time", time);
				nbt.applyNBT(clock);
				
				ItemMeta im = clock.getItemMeta();
				im.setDisplayName("§a§lGODZINA: §e§l"+getTime(hour));
				clock.setItemMeta(im);
				
				inv.setItem(9*row - 1 + 2*slot, clock);
			}
		}
		
		p.openInventory(inv);
	}
	
	public static void openRyseMenu(Player p) {
		RyseInventory.builder()
			.title("§7§lKamienny obserwator")
			.size(54)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.listener(KamiennyObserwatorEvent.getClickEvent())
			.disableUpdateTask()
			.provider(provider)
			.build(Main.getInstance())
			.open(p);
	}
	
	@Deprecated
	public static class KamiennyObserwatorMenu implements InventoryHolder {

		@Override
		public Inventory getInventory() {
			return null;
		}
		
	}
	
	private static String getTime(int hour) {
		switch(hour) {
			case 0:
				return "00:00";
			case 1:
				return "01:00";
			case 2:
				return "02:00";
			case 3:
				return "03:00";
			case 4:
				return "04:00";
			case 5:
				return "05:00";
			case 6:
				return "06:00";
			case 7:
				return "07:00";
			case 8:
				return "08:00";
			case 9:
				return "09:00";
			case 10:
				return "10:00";
			case 11:
				return "11:00";
			case 12:
				return "12:00";
			case 13:
				return "13:00";
			case 14:
				return "14:00";
			case 15:
				return "15:00";
			case 16:
				return "16:00";
			case 17:
				return "17:00";
			case 18:
				return "18:00";
			case 19:
				return "19:00";
			case 20:
				return "20:00";
			case 21:
				return "21:00";
			case 22:
				return "22:00";
			case 23:
				return "23:00";
			case 24:
				return "24:00";
		}
		return "00:00";
	}

}
