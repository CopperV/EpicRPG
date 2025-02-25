package me.Vark123.EpicRPG.MMExtension.Conditions;

import org.bukkit.entity.Player;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractPlayer;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.conditions.ISkillMetaComparisonCondition;
import io.lumine.mythic.api.skills.placeholders.PlaceholderInt;
import io.lumine.mythic.core.skills.SkillCondition;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;

public class EnoughManaCondition extends SkillCondition implements ISkillMetaComparisonCondition {

	private PlaceholderInt amount;
	
	public EnoughManaCondition(final String line, final MythicLineConfig mlc) {
		super(line);
		this.amount = mlc.getPlaceholderInteger(new String[] { "amount", "a", "value", "val" }, "10", this.conditionVar);
	}

	
	@Override
	public boolean check(SkillMetadata arg0, AbstractEntity arg1) {
		int mana = amount.get(arg0);
		
		if(!(arg1 instanceof AbstractPlayer))
			return false;
		
		Player player = (Player) arg1.getBukkitEntity();
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		
		return rpg.getStats().getPresentMana() >= mana;
	}

}
