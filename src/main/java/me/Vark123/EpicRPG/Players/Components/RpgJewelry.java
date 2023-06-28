package me.Vark123.EpicRPG.Players.Components;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.command.CommandSender;

import lombok.Getter;
import me.Vark123.EpicRPG.Files.FileOperations;
import me.Vark123.EpicRPG.Jewelry.Amulet;
import me.Vark123.EpicRPG.Jewelry.Gloves;
import me.Vark123.EpicRPG.Jewelry.JewelryItem;
import me.Vark123.EpicRPG.Jewelry.Ring;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Utils.ChatPrintable;

@Getter
public class RpgJewelry implements Serializable, ChatPrintable {
	
	private static final long serialVersionUID = 1536036389821059451L;

	private RpgPlayer rpg;

	private Map<Integer,JewelryItem> akcesoria;
	
	public RpgJewelry(RpgPlayer rpg) {
		this.rpg = rpg;
		akcesoria = createJewerlyEq();
		if(FileOperations.hasPlayerJewelryFile(rpg.getPlayer())) {
			FileOperations.loadPlayerJewelry(this, rpg.getPlayer());
		} else {
			FileOperations.createPlayerJewelryFile(rpg.getPlayer());
		}
	}

	private Map<Integer,JewelryItem> createJewerlyEq() {
		Map<Integer,JewelryItem> toReturn = new ConcurrentHashMap<>();
		toReturn.put(0, new Amulet(0));
		toReturn.put(1, new Ring(0));
		toReturn.put(2, new Ring(0));
		toReturn.put(3, new Gloves(0));
		return toReturn;
	}

	@Override
	public void print(CommandSender sender) {
		
	}
}
