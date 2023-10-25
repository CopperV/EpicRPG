package me.Vark123.EpicRPG.Players.Components.Compass;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import lombok.Getter;
import me.Vark123.EpicOptions.OptionsAPI;
import me.Vark123.EpicRPG.Players.RpgPlayer;

@Getter
public class EpicCompass implements Serializable {
	
	private static final long serialVersionUID = -7911872319091971074L;

	private RpgPlayer rpg;
	private BossBar compass;
	private boolean enabled = true;

	public EpicCompass(RpgPlayer rpg) {
		this.rpg = rpg;
		
		Player p = rpg.getPlayer();
		OptionsAPI.get().getPlayerManager().getPlayerOptions(p)
			.ifPresent(op -> {
				op.getPlayerOptionByID("epicrpg_compass").ifPresent(option -> {
					enabled = (boolean) option.getValue();
				});
			});
		
		compass = Bukkit.createBossBar(getCompassDisplay(p.getLocation().getYaw()), BarColor.GREEN, BarStyle.SOLID);
		compass.setProgress(1.);
		compass.addPlayer(p);
		compass.setVisible(enabled);
	}
	
	public void enableCompass() {
		enabled = true;
		compass.setVisible(enabled);
	}
	
	public void disableCompass() {
		enabled = false;
		compass.setVisible(enabled);
	}
	
	public void toggleCompass() {
		enabled = !enabled;
		compass.setVisible(enabled);
		updateCompass();
	}
	
	public void updateCompass() {
		compass.setTitle(getCompassDisplay(rpg.getPlayer().getLocation().getYaw()));
	}
	
	private static String getCompassDisplay(float yaw) {
		if(yaw < 0.0f)
			yaw += 360.0f;
		yaw += 2.25F;
		if (yaw > 360.0F) {
			yaw -= 360.0F;
		}

		if (yaw >= 180.0F && (double) yaw < 184.5D) {
			return "§7 · · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · ";
		} else if ((double) yaw >= 184.5D && yaw < 189.0F) {
			return "§7    · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · §f Ｅ §7 ";
		} else if (yaw >= 189.0F && (double) yaw < 193.5D) {
			return "§7    · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · §f Ｅ §7 · ";
		} else if ((double) yaw >= 193.5D && yaw < 198.0F) {
			return "§7    · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · §f Ｅ §7 · · ";
		} else if (yaw >= 198.0F && (double) yaw < 202.5D) {
			return "§7    · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · §f Ｅ §7 · · · ";
		} else if ((double) yaw >= 202.5D && yaw < 207.0F) {
			return "§7    · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · §f Ｅ §7 · · · · ";
		} else if (yaw >= 207.0F && (double) yaw < 211.5D) {
			return "§7    ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · §f Ｅ §7 · · · · · ";
		} else if ((double) yaw >= 211.5D && yaw < 216.0F) {
			return "§7   §f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · §f Ｅ §7 · · · · · · ";
		} else if (yaw >= 216.0F && (double) yaw < 220.5D) {
			return "§7 §f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · §f Ｅ §7 · · · · · · · ";
		} else if ((double) yaw >= 220.5D && yaw < 225.0F) {
			return "§fＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · §f Ｅ §7 · · · · · · ·  ";
		} else if (yaw >= 225.0F && (double) yaw < 229.5D) {
			return "§7 · · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · §f Ｅ §7 · · · · · · · ";
		} else if ((double) yaw >= 229.5D && yaw < 234.0F) {
			return "§7    · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · §f Ｅ §7 · · · · · · ·§f ＳＥ §7";
		} else if (yaw >= 234.0F && (double) yaw < 238.5D) {
			return "§7    · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · §f Ｅ §7 · · · · · · ·§f ＳＥ §7· ";
		} else if ((double) yaw >= 238.5D && yaw < 243.0F) {
			return "§7    · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · §f Ｅ §7 · · · · · · ·§f ＳＥ §7· · ";
		} else if (yaw >= 243.0F && (double) yaw < 247.5D) {
			return "§7    · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · §f Ｅ §7 · · · · · · ·§f ＳＥ §7· · · ";
		} else if ((double) yaw >= 247.5D && yaw < 252.0F) {
			return "§7    · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · §f Ｅ §7 · · · · · · ·§f ＳＥ §7· · · · ";
		} else if (yaw >= 252.0F && (double) yaw < 256.5D) {
			return "§7    · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · §f Ｅ §7 · · · · · · ·§f ＳＥ §7· · · · · ";
		} else if ((double) yaw >= 256.5D && yaw < 261.0F) {
			return "§7    §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · §f Ｅ §7 · · · · · · ·§f ＳＥ §7· · · · · · ";
		} else if (yaw >= 261.0F && (double) yaw < 265.5D) {
			return "§7  §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · §f Ｅ §7 · · · · · · ·§f ＳＥ §7· · · · · · · ";
		} else if ((double) yaw >= 265.5D && yaw < 270.0F) {
			return "§7§c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · §f Ｅ §7 · · · · · · ·§f ＳＥ §7· · · · · · ·  ";
		} else if (yaw >= 270.0F && (double) yaw < 274.5D) {
			return "§7 · · · · · · ·§f ＮＥ §7· · · · · · ·§f Ｅ §7· · · · · · ·§f ＳＥ §7· · · · · · · ";
		} else if ((double) yaw >= 274.5D && yaw < 279.0F) {
			return "§7    · · · · · ·§f ＮＥ §7· · · · · · ·§f Ｅ §7· · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 ";
		} else if (yaw >= 279.0F && (double) yaw < 283.5D) {
			return "§7    · · · · ·§f ＮＥ §7· · · · · · ·§f Ｅ §7· · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · ";
		} else if ((double) yaw >= 283.5D && yaw < 288.0F) {
			return "§7    · · · ·§f ＮＥ §7· · · · · · ·§f Ｅ §7· · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · ";
		} else if (yaw >= 288.0F && (double) yaw < 292.5D) {
			return "§7    · · ·§f ＮＥ §7· · · · · · ·§f Ｅ §7· · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · ";
		} else if ((double) yaw >= 292.5D && yaw < 297.0F) {
			return "§7    · ·§f ＮＥ §7· · · · · · ·§f Ｅ §7· · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · ";
		} else if (yaw >= 297.0F && (double) yaw < 301.5D) {
			return "§7    ·§f ＮＥ §7· · · · · · ·§f Ｅ §7· · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · ";
		} else if ((double) yaw >= 301.5D && yaw < 306.0F) {
			return "§7   §f ＮＥ §7· · · · · · ·§f Ｅ §7· · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ";
		} else if (yaw >= 306.0F && (double) yaw < 310.5D) {
			return "§7 §f ＮＥ §7· · · · · · ·§f Ｅ §7· · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · · ";
		} else if ((double) yaw >= 310.5D && yaw < 315.0F) {
			return "§fＮＥ §7· · · · · · ·§f Ｅ §7· · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ·  ";
		} else if (yaw >= 315.0F && (double) yaw < 319.5D) {
			return "§7 · · · · · · ·§f Ｅ §7· · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · · ";
		} else if ((double) yaw >= 319.5D && yaw < 324.0F) {
			return "§7    · · · · · ·§f Ｅ §7· · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7";
		} else if (yaw >= 324.0F && (double) yaw < 328.5D) {
			return "§7    · · · · ·§f Ｅ §7· · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· ";
		} else if ((double) yaw >= 328.5D && yaw < 333.0F) {
			return "§7    · · · ·§f Ｅ §7· · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · ";
		} else if (yaw >= 333.0F && (double) yaw < 337.5D) {
			return "§7    · · ·§f Ｅ §7· · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · ";
		} else if ((double) yaw >= 337.5D && yaw < 342.0F) {
			return "§7    · ·§f Ｅ §7· · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · ";
		} else if (yaw >= 342.0F && (double) yaw < 346.5D) {
			return "§7    ·§f Ｅ §7· · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · ";
		} else if ((double) yaw >= 346.5D && yaw < 351.0F) {
			return "§7   §f Ｅ §7· · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · ";
		} else if (yaw >= 351.0F && (double) yaw < 355.5D) {
			return "§7 §f Ｅ §7· · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · · ";
		} else if ((double) yaw >= 355.5D && yaw < 360.0F) {
			return "§fＥ §7· · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · ·  ";
		} else if (yaw >= 0.0F && (double) yaw < 4.5D) {
			return "§7 · · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · · ";
		} else if ((double) yaw >= 4.5D && yaw < 9.0F) {
			return "§7    · · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 ";
		} else if (yaw >= 9.0F && (double) yaw < 13.5D) {
			return "§7    · · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · ";
		} else if ((double) yaw >= 13.5D && yaw < 18.0F) {
			return "§7    · · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · ";
		} else if (yaw >= 18.0F && (double) yaw < 22.5D) {
			return "§7    · · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · ";
		} else if ((double) yaw >= 22.5D && yaw < 27.0F) {
			return "§7    · ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · ";
		} else if (yaw >= 27.0F && (double) yaw < 31.5D) {
			return "§7    ·§f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · ";
		} else if ((double) yaw >= 31.5D && yaw < 36.0F) {
			return "§7   §f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ";
		} else if (yaw >= 36.0F && (double) yaw < 40.5D) {
			return "§7 §f ＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · · ";
		} else if ((double) yaw >= 40.5D && yaw < 45.0F) {
			return "§fＳＥ §7· · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ·  ";
		} else if (yaw >= 45.0F && (double) yaw < 49.5D) {
			return "§7 · · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · · ";
		} else if ((double) yaw >= 49.5D && yaw < 54.0F) {
			return "§7    · · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7";
		} else if (yaw >= 54.0F && (double) yaw < 58.5D) {
			return "§7    · · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· ";
		} else if ((double) yaw >= 58.5D && yaw < 63.0F) {
			return "§7    · · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · ";
		} else if (yaw >= 63.0F && (double) yaw < 67.5D) {
			return "§7    · · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · ";
		} else if ((double) yaw >= 67.5D && yaw < 72.0F) {
			return "§7    · · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · ";
		} else if (yaw >= 72.0F && (double) yaw < 76.5D) {
			return "§7    · §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · ";
		} else if ((double) yaw >= 76.5D && yaw < 81.0F) {
			return "§7    §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · ";
		} else if (yaw >= 81.0F && (double) yaw < 85.5D) {
			return "§7  §f Ｓ §7 · · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · · ";
		} else if ((double) yaw >= 85.5D && yaw < 90.0F) {
			return "§7Ｓ  · · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · ·  ";
		} else if (yaw >= 90.0F && (double) yaw < 94.5D) {
			return "§7 · · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ·§f§f ＮＷ §7§7· · · · · · · ";
		} else if ((double) yaw >= 94.5D && yaw < 99.0F) {
			return "§7    · · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 ";
		} else if (yaw >= 99.0F && (double) yaw < 103.5D) {
			return "§7    · · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · ";
		} else if ((double) yaw >= 103.5D && yaw < 108.0F) {
			return "§7    · · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · ";
		} else if (yaw >= 108.0F && (double) yaw < 112.5D) {
			return "§7    · · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · ";
		} else if ((double) yaw >= 112.5D && yaw < 117.0F) {
			return "§7    · ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · ";
		} else if (yaw >= 117.0F && (double) yaw < 121.5D) {
			return "§7    ·§f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · ";
		} else if ((double) yaw >= 121.5D && yaw < 126.0F) {
			return "§7   §f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ";
		} else if (yaw >= 126.0F && (double) yaw < 130.5D) {
			return "§7 §f ＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · · ";
		} else if ((double) yaw >= 130.5D && yaw < 135.0F) {
			return "§fＳＷ §7· · · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ·  ";
		} else if (yaw >= 135.0F && (double) yaw < 139.5D) {
			return "§7 · · · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · · ";
		} else if ((double) yaw >= 139.5D && yaw < 144.0F) {
			return "§7    · · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7";
		} else if (yaw >= 144.0F && (double) yaw < 148.5D) {
			return "§7    · · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· ";
		} else if ((double) yaw >= 148.5D && yaw < 153.0F) {
			return "§7    · · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · ";
		} else if (yaw >= 153.0F && (double) yaw < 157.5D) {
			return "§7    · · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · ";
		} else if ((double) yaw >= 157.5D && yaw < 162.0F) {
			return "§7    · · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · ";
		} else if (yaw >= 162.0F && (double) yaw < 166.5D) {
			return "§7    · §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · ";
		} else if ((double) yaw >= 166.5D && yaw < 171.0F) {
			return "§7    §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · ";
		} else if (yaw >= 171.0F && (double) yaw < 175.5D) {
			return "§7  §f Ｗ §7 · · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · · ";
		} else {
			return (double) yaw >= 175.5D && yaw < 180.0F
					? "§fＷ §7 · · · · · · ·§f ＮＷ §7· · · · · · · §c Ｎ §7 · · · · · · ·§f ＮＥ §7· · · · · · ·  "
					: "§8Compass Error";
		}
	}

}
