package me.Vark123.EpicRPG.Options.Listeners;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicOptions.OptionSystem.Option;
import me.Vark123.EpicOptions.OptionSystem.OptionManager;
import me.Vark123.EpicOptions.OptionSystem.Events.OptionsRegistryEvent;
import me.Vark123.EpicRPG.Options.Items.AdvancedOptionItem;
import me.Vark123.EpicRPG.Options.Menus.ResourceInfoOptionMenuManager;
import me.Vark123.EpicRPG.Options.Serializables.ResourcesInfoSerializable;

public class ResourceInfoRegistryListener implements Listener {

	@EventHandler
	public void onRegister(OptionsRegistryEvent e) {
		Option<?> pOption = new Option<ResourcesInfoSerializable>(
				"epicrpg_resources",
				new AdvancedOptionItem<ResourcesInfoSerializable>(
						"§b§lPOWIADOMIENIA",
						Arrays.asList(" ","§7Zmieniaj mozliwosc powiadomien","§7odnosnie otrzymywanego doswiadczenia, stygii","§7i innych!"),
						Material.OAK_SIGN,
						5),
				(op, option) -> {
					ResourceInfoOptionMenuManager.get().openMenu(op);
				},
				ResourcesInfoSerializable.builder()
					.expInfo(true)
					.moneyInfo(true)
					.stygiaInfo(true)
					.coinsInfo(true)
					.rudaInfo(true)
					.build());
		OptionManager.get().registerOption(pOption);
	}

}
