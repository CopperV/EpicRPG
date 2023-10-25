package me.Vark123.EpicRPG.Options.Compass.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.Vark123.EpicOptions.OptionSystem.Option;
import me.Vark123.EpicOptions.OptionSystem.OptionManager;
import me.Vark123.EpicOptions.OptionSystem.Events.OptionsRegistryEvent;
import me.Vark123.EpicRPG.Options.TestClassSerializable;
import me.Vark123.EpicRPG.Options.Items.TestItem;

public class TestOptionRegistryListener implements Listener {

	@EventHandler
	public void onRegister(OptionsRegistryEvent e) {
		Option<?> pOption = new Option<TestClassSerializable>(
				"epicrpg_test",
				new TestItem(),
				(op, option) -> {
					op.getPlayer().sendMessage("Ustawienie testowe");
				},
				new TestClassSerializable());
		OptionManager.get().registerOption(pOption);
	}
	
}
