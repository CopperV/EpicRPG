package me.Vark123.EpicRPG.Options.Serializables;

import org.bukkit.configuration.ConfigurationSection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.Vark123.EpicOptions.OptionSystem.ISerializable;

@Data
@AllArgsConstructor
@Builder
public class ResourcesInfoSerializable implements ISerializable {

	private boolean expInfo = true;
	private boolean moneyInfo = true;
	private boolean stygiaInfo = true;
	private boolean coinsInfo = true;
	private boolean rudaInfo = true;
	private boolean clanExpInfo = true;
	
	@Override
	public void serialize(ConfigurationSection section) {
		section.set("exp", expInfo);
		section.set("money", moneyInfo);
		section.set("stygia", stygiaInfo);
		section.set("coins", coinsInfo);
		section.set("ruda", rudaInfo);
		section.set("clan-exp", clanExpInfo);
	}
	
	@Override
	public void deserialize(ConfigurationSection section) {
		expInfo = section.getBoolean("exp");
		moneyInfo = section.getBoolean("money");
		stygiaInfo = section.getBoolean("stygia");
		coinsInfo = section.getBoolean("coins");
		rudaInfo = section.getBoolean("ruda");
		clanExpInfo = section.getBoolean("clan-exp");
	}
	
	@Override
	public ISerializable copy() {
		return builder()
				.expInfo(expInfo)
				.moneyInfo(moneyInfo)
				.stygiaInfo(stygiaInfo)
				.coinsInfo(coinsInfo)
				.rudaInfo(rudaInfo)
				.clanExpInfo(clanExpInfo)
				.build();
	}

}