package me.Vark123.EpicRPG.Options.Listeners;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicOptions.OptionSystem.Option;
import me.Vark123.EpicOptions.OptionSystem.OptionManager;
import me.Vark123.EpicOptions.OptionSystem.Events.OptionsRegistryEvent;
import me.Vark123.EpicRPG.Options.Items.AdvancedOptionItem;
import me.Vark123.EpicRPG.Options.Menus.HorseOptionMenuManager;
import me.Vark123.EpicRPG.Options.Serializables.StringSerializable;

public class HorseOptionRegistryListener implements Listener {

	@EventHandler
	public void onRegister(OptionsRegistryEvent e) {
		Option<?> pOption = new Option<StringSerializable>(
				"epicrpg_horse", 
				new AdvancedOptionItem<StringSerializable>(
						"§a§lDOMYSLNY WIERZCHOWIEC",
						Arrays.asList(" "),
						Material.LEATHER_HORSE_ARMOR,
						9), 
				(op, option) -> {
					HorseOptionMenuManager.get().openMenu(op);
				}, new StringSerializable("0"));
		OptionManager.get().registerOption(pOption);
	}

}
