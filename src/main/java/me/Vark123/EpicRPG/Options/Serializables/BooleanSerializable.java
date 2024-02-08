package me.Vark123.EpicRPG.Options.Serializables;

import org.bukkit.configuration.ConfigurationSection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.Vark123.EpicOptions.OptionSystem.ISerializable;

@Data
@AllArgsConstructor
@Builder
public class BooleanSerializable implements ISerializable {

	private boolean value;

	@Override
	public void serialize(ConfigurationSection section) {
		section.set("value", value);
	}

	@Override
	public void deserialize(ConfigurationSection section) {
		this.value = section.getBoolean("value");
	}

	@Override
	public ISerializable copy() {
		return builder()
				.value(value)
				.build();
	}
}
