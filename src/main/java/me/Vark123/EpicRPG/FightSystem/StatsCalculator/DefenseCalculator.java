package me.Vark123.EpicRPG.FightSystem.StatsCalculator;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.Runes.SilaJednosci;
import me.Vark123.EpicRPG.Utils.Pair;

public class DefenseCalculator implements IDamageCalculator {

	@Override
	public Pair<Double, Boolean> calc(Entity damager, Entity victim, double dmg, Object... args) {
		Pair<Double, Boolean> pair = new Pair<>(dmg, false);
		
		if(!(victim instanceof Player)
				|| !PlayerManager.getInstance().playerExists((Player) victim))
			return pair;

		Player p = (Player) victim;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgStats stats = rpg.getStats();
		RpgPlayerInfo info = rpg.getInfo();
		RpgModifiers modifiers = rpg.getModifiers();
		
		if(modifiers.hasSilaJednosci() && SilaJednosci.getGlobalEffected().containsKey(rpg.getPlayer())) {
			SilaJednosci rune = SilaJednosci.getGlobalEffected().get(rpg.getPlayer());
			Player caster = rune.getCaster();
			Location castLoc = rune.getLoc();
			if(caster.isOnline() && caster.getWorld().getUID().equals(castLoc.getWorld().getUID())
					&& caster.getLocation().distance(castLoc) <= rune.getRune().getObszar() && !caster.equals(p)) {
				dmg *= 0.6;
			}
		}
		
		if(damager instanceof Player 
				&& PlayerManager.getInstance().playerExists((Player) damager)) {
			pair.setKey(dmg);
			return pair;
		}
		
		int wytrzymalosc = stats.getFinalWytrzymalosc() < 0 ? 0 : stats.getFinalWytrzymalosc();
		double def;
		if(info.getLevel() < 70) {
			def = 0.13 * wytrzymalosc * stats.getFinalOchrona() / (100*0.05*info.getLevel());
		} else {
			def = 0.13 * wytrzymalosc * stats.getFinalOchrona() / (100*0.05*70);
		}
		double def2 = stats.getFinalOchrona()*0.35;
		
		dmg -= def;
		dmg -= def2;
		
		pair.setKey(dmg);
		return pair;
	}

}
