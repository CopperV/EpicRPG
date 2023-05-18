package me.Vark123.EpicRPG.Players.Components;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import lombok.Setter;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Utils.ChatPrintable;

@Setter
public class RpgRzemiosla implements Serializable, ChatPrintable {
	
	private static final long serialVersionUID = -7348456492743263474L;

	private RpgPlayer rpg;
	
	private boolean alchemia;
	private boolean kowalstwo;
	private boolean platnerstwo;
	private boolean luczarstwo;
	private boolean jubilerstwo;
	
	public RpgRzemiosla(RpgPlayer rpg) {
		this.rpg = rpg;
	}
	
	public RpgRzemiosla(RpgPlayer rpg, ResultSet set) throws SQLException {
		this.rpg = rpg;
		set.first();
		this.alchemia = set.getBoolean("player_rzemioslo.alchemia");
		this.platnerstwo = set.getBoolean("player_rzemioslo.platnerstwo");
		this.kowalstwo = set.getBoolean("player_rzemioslo.kowalstwo");
		this.luczarstwo = set.getBoolean("player_rzemioslo.luczarstwo");
		this.jubilerstwo = set.getBoolean("player_rzemioslo.jubilerstwo");
	}
	
	public RpgRzemiosla(RpgPlayer rpg, YamlConfiguration fYml) {
		this.rpg = rpg;
		this.alchemia = fYml.getBoolean("alchemia");
		this.platnerstwo = fYml.getBoolean("platnerstwo");
		this.kowalstwo = fYml.getBoolean("kowalstwo");
		this.luczarstwo = fYml.getBoolean("luczarstwo");
		this.jubilerstwo = fYml.getBoolean("jubilerstwo");
	}

	public RpgPlayer getRpg() {
		return rpg;
	}

	public boolean hasAlchemia() {
		return alchemia;
	}

	public boolean hasKowalstwo() {
		return kowalstwo;
	}

	public boolean hasPlatnerstwo() {
		return platnerstwo;
	}

	public boolean hasLuczarstwo() {
		return luczarstwo;
	}

	public boolean hasJubilerstwo() {
		return jubilerstwo;
	}

	@Override
	public void print(CommandSender sender) {
		StringBuilder learned = new StringBuilder();
		if(alchemia)
			learned.append("§aAlchemia§7, ");
		if(kowalstwo)
			learned.append("§aKowalstwo§7, ");
		if(platnerstwo)
			learned.append("§aPlatnerstwo§7, ");
		if(luczarstwo)
			learned.append("§aLuczarstwo§7, ");
		if(jubilerstwo)
			learned.append("§aJubilerstwo§7, ");
		
		if(!learned.isEmpty())
			learned.setLength(learned.length()-4);
		
		sender.sendMessage("§6§l========================= ");
		sender.sendMessage("    §2Nauczone rzemiosla: "+(learned.isEmpty() ? 
				"§cZADNE" :
				learned.toString()));
		
	}
	
}
