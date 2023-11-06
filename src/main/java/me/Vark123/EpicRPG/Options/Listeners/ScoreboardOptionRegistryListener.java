package me.Vark123.EpicRPG.Options.Listeners;

import java.util.Arrays;
import java.util.LinkedList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicOptions.OptionSystem.Option;
import me.Vark123.EpicOptions.OptionSystem.OptionManager;
import me.Vark123.EpicOptions.OptionSystem.Events.OptionsRegistryEvent;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Options.Items.AdvancedOptionItem;
import me.Vark123.EpicRPG.Options.Menus.ScoreboardOptionMenuManager;
import me.Vark123.EpicRPG.Options.Serializables.ScoreboardSerializable;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class ScoreboardOptionRegistryListener implements Listener {

	@EventHandler
	public void onRegister(OptionsRegistryEvent e) {
		Option<?> pOption = new Option<ScoreboardSerializable>(
				"epicrpg_scoreboard",
				new AdvancedOptionItem<ScoreboardSerializable>(
						"§e§lSCOREBOARD",
						Arrays.asList(" ","§7Modyfikuj spis statystyk po prawej stronie","§7wedlug swoich potrzeb!","§4§oDOSTEPNE OD 10 POZIOMU!"),
						Material.PAPER,
						3),
				(op, option) -> {
					Player p = op.getPlayer();
					RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
					if(rpg.getInfo().getLevel() < 10) {
						p.sendMessage(Main.getInstance().getPrefix()+" §cMasz za niski poziom, by zmieniac swoj scoreboard!");
						p.closeInventory();
						return;
					}
					ScoreboardOptionMenuManager.get().openMenu(op);
				},
				new ScoreboardSerializable(true, new LinkedList<String>(ScoreboardSerializable.DEFAULT_VALUES)));
		OptionManager.get().registerOption(pOption);
	}

}
