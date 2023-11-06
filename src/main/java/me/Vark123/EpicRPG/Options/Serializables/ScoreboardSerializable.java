package me.Vark123.EpicRPG.Options.Serializables;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.Vark123.EpicOptions.OptionSystem.ISerializable;

@Data
@AllArgsConstructor
@Builder
public class ScoreboardSerializable implements ISerializable {

	public static final List<String> DEFAULT_VALUES = new LinkedList<String>(Arrays.asList(
			"%epicscoreboard_info%",
			"%epicscoreboard_nick%",
			"%epicscoreboard_level%",
			"%epicscoreboard_exp%",
			"%epicscoreboard_pn%",
			"%epicscoreboard_resources%",
			"%epicscoreboard_money%",
			"%epicscoreboard_stygia%",
			"%epicscoreboard_coins%",
			"%epicscoreboard_ruda%",
			"%epicscoreboard_stats%",
			"%epicscoreboard_krag%",
			"%epicscoreboard_hp%",
			"%epicscoreboard_mana%"));
	
	private boolean enabled;
	
	private List<String> lines;

	@Override
	public void serialize(ConfigurationSection section) {
		section.set("lines", lines);
		section.set("enabled", enabled);
	}

	@Override
	public void deserialize(ConfigurationSection section) {
		lines = section.getStringList("lines");
		enabled = section.getBoolean("enabled");
	}

	@Override
	public ISerializable copy() {
		return builder()
				.lines(new LinkedList<>(lines))
				.enabled(enabled)
				.build();
	}
	
	
	
	
	
}
