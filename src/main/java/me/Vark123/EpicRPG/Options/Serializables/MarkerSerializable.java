package me.Vark123.EpicRPG.Options.Serializables;

import org.bukkit.configuration.ConfigurationSection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.Vark123.EpicOptions.OptionSystem.ISerializable;

@Data
@AllArgsConstructor
@Builder
public class MarkerSerializable implements ISerializable {

	private boolean created;
	private boolean showed;
	
	private String world;
	private double x;
	private double z;

	@Override
	public void serialize(ConfigurationSection section) {
		section.set("created", created);
		section.set("showed", showed);
		section.set("location.world", world);
		section.set("location.x", x);
		section.set("location.z", z);
	}

	@Override
	public void deserialize(ConfigurationSection section) {
		created = section.getBoolean("created");
		showed = section.getBoolean("showed");
		
		world = section.getString("location.world");
		x = section.getDouble("location.x");
		z = section.getDouble("location.z");
	}

	@Override
	public ISerializable copy() {
		return builder()
				.created(created)
				.showed(showed)
				.world(world)
				.x(x)
				.z(z)
				.build();
	}
	
	
	
}
