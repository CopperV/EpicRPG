package me.Vark123.EpicRPG.RuneSystem.Runes.Rownowaga;

import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Vark123.EpicComponentAPI.EpicComponent;
import me.Vark123.EpicInventory.Content.InventoryContents;
import me.Vark123.EpicInventory.Content.InventoryProvider;
import me.Vark123.EpicInventory.Enums.DisabledEvents;
import me.Vark123.EpicInventory.Pagination.EpicInventory;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Listeners.Runes.KamiennyObserwatorEvent;

public class KamiennyObserwator extends ACastableRune {

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
					EpicComponent comp = new EpicComponent(newClock);
					comp.setInteger("clock_time", time);
					comp.applyTo(newClock);
					
					ItemMeta im = newClock.getItemMeta();
					im.setDisplayName("§a§lGODZINA: §e§l"+getTime(hour));
					newClock.setItemMeta(im);
					
					contents.set(clockSlots[i], newClock);
				}
			}
		};
	}

	public KamiennyObserwator(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}
	
	@Override
	public void castSpell() {
		Location loc = player.getLocation().clone().add(0, 1, 0);
		World world = castLoc.getWorld();
		if(!world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE).booleanValue()) {
			player.sendMessage(Main.getInstance().getPrefix()+" §cW tym swiecie nie mozna zmieniac czasu!");
			
			world.playSound(loc, Sound.ENTITY_VILLAGER_NO, 2, 0.8f);
			
			world.spawnParticle(Particle.ANGRY_VILLAGER, loc, 25, 0.4f, 1f, 0.4f, 0.1f);
			world.spawnParticle(Particle.LARGE_SMOKE, loc, 25, 0.4f, 1f, 0.4f, 0.02f);
			world.spawnParticle(Particle.DAMAGE_INDICATOR, loc, 25, 0.4f, 1f, 0.4f, 0.1f);
			return;
		}
		
		world.playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1, .5f);
		openMenu(player);
	}
	
	public static void openMenu(Player player) {
		EpicInventory.builder()
			.title("§7§lKamienny obserwator")
			.size(54)
			.ignoreEvents(DisabledEvents.INVENTORY_DRAG)
			.listener(KamiennyObserwatorEvent.getClickEvent())
			.disableUpdateTask()
			.provider(provider)
			.build(Main.getInstance())
			.open(player);
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
