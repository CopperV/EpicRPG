package me.Vark123.EpicRPG.Options.Compass.Listeners;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicOptions.OptionSystem.Option;
import me.Vark123.EpicOptions.OptionSystem.OptionManager;
import me.Vark123.EpicOptions.OptionSystem.Events.OptionsRegistryEvent;
import me.Vark123.EpicRPG.Options.Items.BooleanItem;
import me.Vark123.EpicRPG.Players.PlayerManager;

public class CompassOptionRegistryListener implements Listener {

	@EventHandler
	public void onRegister(OptionsRegistryEvent e) {
		Option<?> pOption = new Option<Boolean>(
				"epicrpg_compass",
				new BooleanItem(
						"§2§lKOMPAS",
						Arrays.asList(" ","§7Kompas w postaci BOSS-BARU,","§7ktory zawsze dokladnie wskaze Ci kierunek","§7Twojej podrozy"),
						Material.COMPASS,
						1),
				(op, option) -> {
					boolean value = option.getValue();
					option.setValue(Boolean.valueOf(!value));
					
					PlayerManager.getInstance().getRpgPlayer(op.getPlayer())
						.getCompass().toggleCompass();
					
					op.getPlayer().closeInventory();
				},
				true);
		OptionManager.get().registerOption(pOption);
	}
	
}
