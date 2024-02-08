package me.Vark123.EpicRPG.Options.Listeners;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicOptions.OptionSystem.Option;
import me.Vark123.EpicOptions.OptionSystem.OptionManager;
import me.Vark123.EpicOptions.OptionSystem.Events.OptionsRegistryEvent;
import me.Vark123.EpicRPG.Options.Items.AdvancedOptionItem;
import me.Vark123.EpicRPG.Options.Menus.MarkerOptionMenuManager;
import me.Vark123.EpicRPG.Options.Serializables.MarkerSerializable;

public class MarkerRegistryListener implements Listener {

	@EventHandler
	public void onRegister(OptionsRegistryEvent e) {
		Option<?> pOption = new Option<MarkerSerializable>(
				"epicrpg_markers",
				new AdvancedOptionItem<MarkerSerializable>(
						"§6§lZNACZNIK",
						Arrays.asList(" ","§7Ustawiaj swoj cel podrozy","§7przez wykorzystanie serwerowego systemu znacznikow!"),
						Material.BEACON,
						7),
				(op, option) -> {
					MarkerOptionMenuManager.get().openMenu(op);
				},
				new MarkerSerializable(false, false, "world", 0, 0));
		OptionManager.get().registerOption(pOption);
	}

}
