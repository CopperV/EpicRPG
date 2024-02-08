package me.Vark123.EpicRPG.Options.Serializables;

import org.bukkit.configuration.ConfigurationSection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.Vark123.EpicOptions.OptionSystem.ISerializable;

@Data
@AllArgsConstructor
@Builder
public class StringSerializable implements ISerializable {

	private String value;
	
	@Override
	public void serialize(ConfigurationSection section) {
		section.set("value", value);
	}

	@Override
	public void deserialize(ConfigurationSection section) {
		this.value = section.getString("value","0");
	}

	@Override
	public ISerializable copy() {
		return builder()
				.value(value)
				.build();
	}

}
