package me.Vark123.EpicRPG.Players.Components.Scoreboard;

import java.io.Serializable;

import org.bukkit.entity.Player;

import lombok.Getter;
import me.Vark123.EpicOptions.OptionsAPI;
import me.Vark123.EpicOptions.PlayerSystem.OPlayer;
import me.Vark123.EpicOptions.PlayerSystem.PlayerOption;
import me.Vark123.EpicRPG.Options.Serializables.ScoreboardSerializable;
import me.Vark123.EpicRPG.Players.RpgPlayer;

@Getter
public class EpicScoreboard implements Serializable {
	
	private static final long serialVersionUID = -7911872319091971074L;
	
	private RpgPlayer rpg;
	private PlayerOption<ScoreboardSerializable> option;
	
	@SuppressWarnings("unchecked")
	public EpicScoreboard(RpgPlayer rpg) {
		this.rpg = rpg;
		
		Player p = rpg.getPlayer();
		OPlayer op = OptionsAPI.get().getPlayerManager().getPlayerOptions(p).orElseThrow();
		this.option = (PlayerOption<ScoreboardSerializable>) op.getPlayerOptionByID("epicrpg_scoreboard").orElseThrow();
	}
	
}
