package me.Vark123.EpicRPG.HorseSystem;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Files.FileOperations;
import me.Vark123.EpicRPG.HorseSystem.Horses.BaseHorse;
import me.Vark123.EpicRPG.HorseSystem.Horses.DiamondHorse;
import me.Vark123.EpicRPG.HorseSystem.Horses.ESkeletonHorse;
import me.Vark123.EpicRPG.HorseSystem.Horses.EZombieHorse;
import me.Vark123.EpicRPG.HorseSystem.Horses.GoldenHorse;
import me.Vark123.EpicRPG.HorseSystem.Horses.HauntedHorse;
import me.Vark123.EpicRPG.HorseSystem.Horses.IronHorse;
import me.Vark123.EpicRPG.HorseSystem.Horses.KidHorse;
import me.Vark123.EpicRPG.HorseSystem.Horses.MasterHorse;

public class HorseStickUseEvent implements Listener {

	@EventHandler
	public void onUse(PlayerInteractEvent e) {
		if(!(e.getAction().equals(Action.RIGHT_CLICK_AIR)
				|| e.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
			return;
		
		Player p = e.getPlayer();
		ItemStack it = e.getItem();
		if(it == null || it.getType().equals(Material.AIR))
			return;
		
		if(!it.getType().equals(Material.CARROT_ON_A_STICK))
			return;
		
		ItemMeta im = it.getItemMeta();
		if(!im.hasDisplayName()
				|| !(im.getDisplayName().contains("Kon") 
						|| im.getDisplayName().contains("Wierzchowiec") 
						|| im.getDisplayName().contains("Zrebie")))
			return;

		NBTItem nbt = new NBTItem(it);
		if(!nbt.hasTag("MYTHIC_TYPE"))
			return;
		
		if(p.isInsideVehicle()) {
			p.sendMessage(Main.getInstance().getPrefix() + " §c§lBedac na wierzchowcu nie mozesz przyzwac kolejnego wierzchowca!");
			e.setCancelled(true);
			e.setUseItemInHand(Result.DEFAULT);
			return;
		}
		
		File f = new File(FileOperations.getPlayerHorsesFolder(), p.getName().toLowerCase()+"horse.yml");
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(f);
		List<String> horses = fYml.contains("horses") ? fYml.getStringList("horses") : new LinkedList<>();

		AEpicHorse horse;
		switch(nbt.getString("MYTHIC_TYPE")) {
			case "Kon1":
				horse = new BaseHorse();
				if(!horses.contains("1")) {
					horses.add("1");
				}
				break;
			case "Kon2":
				horse = new IronHorse();
				if(!horses.contains("2")) {
					horses.add("2");
				}
				break;
			case "Kon3":
				horse = new GoldenHorse();
				if(!horses.contains("3")) {
					horses.add("3");
				}
				break;
			case "Kon4":
				horse = new DiamondHorse();
				if(!horses.contains("4")) {
					horses.add("4");
				}
				break;
			case "Kon5":
				horse = new EZombieHorse();
				if(!horses.contains("5")) {
					horses.add("5");
				}
				break;
			case "Kon6":
				horse = new ESkeletonHorse();
				if(!horses.contains("6")) {
					horses.add("6");
				}
				break;
			case "Kon7":
				horse = new MasterHorse();
				if(!horses.contains("7")) {
					horses.add("7");
				}
				break;
			case "Kon8":
				horse = new HauntedHorse();
				if(!horses.contains("8")) {
					horses.add("8");
				}
				break;
			case "Kon9":
				horse = new KidHorse();
				if(!horses.contains("9")) {
					horses.add("9");
				}
				break;
			default:
				return;
		}
		
		fYml.set("horses", horses);
		try {
			fYml.save(f);
		} catch (IOException e1) {
			System.out.println("Blad zapisu konia diamentowego dla "+p.getName());
		}
		
		horse.summonMount(p);
		
		int amount = it.getAmount();
		if(amount == 0) {
			p.getInventory().setItem(e.getHand(), null);
		} else {
			it.setAmount(--amount);
		}
		p.updateInventory();
		
	}
	
}
