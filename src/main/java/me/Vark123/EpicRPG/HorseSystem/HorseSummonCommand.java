package me.Vark123.EpicRPG.HorseSystem;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicOptions.PlayerSystem.PlayerManager;
import me.Vark123.EpicOptions.PlayerSystem.PlayerOption;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Files.FileOperations;
import me.Vark123.EpicRPG.Options.Serializables.StringSerializable;

public class HorseSummonCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("kon"))
			return false;
		if(!(sender instanceof Player)) {
			sender.sendMessage("§cTylko gracz moze uzyc tej komendy");
			return false;
		}
		
		Player p = (Player) sender;
		if(p.isInsideVehicle()) {
			p.sendMessage(Main.getInstance().getPrefix() + " §c§lBedac na wierzchowcu nie mozesz przyzwac kolejnego wierzchowca!");
			return false;
		}
		if(!((Entity)p).isOnGround()) {
			p.sendMessage(Main.getInstance().getPrefix() + " §c§lNie mozesz przywolac wierzchowca w powietrzu!");
			return false;
		}
		
		File f = new File(FileOperations.getPlayerHorsesFolder(), p.getName().toLowerCase()+"horse.yml");
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(f);
		if(!fYml.contains("horses")) {
			p.sendMessage(Main.getInstance().getPrefix()+" §cNie posiadasz zadnych koni");
			return false;
		}
		
		final Location loc = p.getLocation();
		if(
				loc.clone().add(0, 1, 0).getBlock().getType().isSolid() ||
				loc.clone().add(-1, 1, 0).getBlock().getType().isSolid() ||
				loc.clone().add(0, 1, 1).getBlock().getType().isSolid() ||
				loc.clone().add(0, 1, -1).getBlock().getType().isSolid() ||
				loc.clone().add(1, 1, 1).getBlock().getType().isSolid() ||
				loc.clone().add(1, 1, -1).getBlock().getType().isSolid() ||
				loc.clone().add(-1, 1, 1).getBlock().getType().isSolid() ||
				loc.clone().add(-1, 1, 1).getBlock().getType().isSolid() ||
				
				loc.clone().add(0, 2, 0).getBlock().getType().isSolid() ||
				loc.clone().add(-1, 2, 0).getBlock().getType().isSolid() ||
				loc.clone().add(0, 2, 1).getBlock().getType().isSolid() ||
				loc.clone().add(0, 2, -1).getBlock().getType().isSolid() ||
				loc.clone().add(1, 2, 1).getBlock().getType().isSolid() ||
				loc.clone().add(1, 2, -1).getBlock().getType().isSolid() ||
				loc.clone().add(-1, 2, 1).getBlock().getType().isSolid() ||
				loc.clone().add(-1, 2, 1).getBlock().getType().isSolid()) {
			p.sendMessage(Main.getInstance().getPrefix()+" §cW tym miejscu nie mozesz przyzwac konia");
			return false;
		}
		
		if(HorseManager.getInstance().getHorseCd().containsKey(p)
				&& (new Date().getTime() - HorseManager.getInstance().getHorseCd().get(p).getTime()) < (HorseManager.getInstance().HORSE_SUMMON_CD*1000)) {
			p.sendMessage(Main.getInstance().getPrefix() + " §c§lNie mozesz przywolac jeszcze wierzchowca!");
			return false;
		}
		
		if(args.length > 0) {
			switch(args[0].toLowerCase()) {
				case "summon":
				case "przyzwij":
				case "sum":
				case "s":
					MutableBoolean returnValue = new MutableBoolean(true);
					PlayerManager.get().getPlayerOptions(p).ifPresent(op -> {
						@SuppressWarnings("unchecked")
						PlayerOption<StringSerializable> option = (PlayerOption<StringSerializable>) op
								.getPlayerOptionByID("epicrpg_horse").orElseThrow();
						AEpicHorse horse = HorseManager.getInstance().getHorse(option.getValue().getValue());
						
						if(horse == null) {
							p.sendMessage(Main.getInstance().getPrefix() + " §cNie posiadasz ustawionego zadnego wierzchowca!");
							returnValue.setFalse();
							return;
						}
						
						horse.summonMount(p);
						HorseManager.getInstance().getHorseCd().put(p, new Date());
						new BukkitRunnable() {
							
							@Override
							public void run() {
								p.sendMessage(Main.getInstance().getPrefix() + " §a§lMozesz znow przywolac wierzchowca!");
							}
						}.runTaskLater(Main.getInstance(), 20*HorseManager.getInstance().HORSE_SUMMON_CD);
					});
					return true;
			}
		}
		
		HorseManager.getInstance().openMenu(p);
		return true;
	}

}
