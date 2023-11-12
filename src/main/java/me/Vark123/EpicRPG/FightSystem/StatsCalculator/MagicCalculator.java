package me.Vark123.EpicRPG.FightSystem.StatsCalculator;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.FightSystem.DamageUtils;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.ItemStackRune;
import me.Vark123.EpicRPG.Stats.ChangeStats;
import me.Vark123.EpicRPG.Utils.Pair;

public class MagicCalculator implements IDamageCalculator {

	@Override
	public Pair<Double, Boolean> calc(Entity damager, Entity victim, double dmg, Object... args) {
		Pair<Double, Boolean> pair = new Pair<>(dmg, false);
		
		if(!(damager instanceof Player)
				|| !PlayerManager.getInstance().playerExists((Player) damager))
			return pair;
		
		Player p = (Player) damager;
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgStats stats = rpg.getStats();
		RpgPlayerInfo info = rpg.getInfo();
		ChangeStats.change(rpg, null);
		
		if(args == null 
				|| args.length <= 0
				|| !(args[0] instanceof ItemStackRune))
			return new Pair<>(-1., false);
		ItemStackRune ir = (ItemStackRune) args[0];
		double wplyw = ir.getWplyw();
		double dmgLevel = 0;
		double addDmg = 0;
		double dmgKrag = 0;
		double dmgMana = 0;
		
		if(wplyw > 0) {
			double wplywDmg = rpg.getStats().getFinalObrazenia() / wplyw / 100.;
			dmg += wplywDmg;
		}
		
		boolean crit = DamageUtils.checkCrit(rpg);
		pair.setValue(crit);
		if(victim instanceof Player) {
			String type = ir.getMagicType().toLowerCase();
			switch(type) {
				case "ogien":
					dmg = wplyw*1.8*dmg;
					break;
				case "woda":
					dmg = wplyw*0.9*dmg;
					break;
				case "natura":
					dmg = wplyw*0.8*dmg;
					break;
				case "tajemna":
					dmg = wplyw*0.7*dmg;
					break;
				case "mrok":
					dmg = (wplyw+0.1)*dmg;
					break;
				case "swiatlo":
					dmg = wplyw*1.65*dmg;
					break;
				case "rownowaga":
					dmg = wplyw*1.75*dmg;
					break;
				case "chaos":
					dmg = wplyw*0.5*dmg;
					break;
				case "krew":
					dmg = wplyw*2*dmg;
					break;
				default:
					dmg = wplyw*0.5*dmg;
					break;
			}
			dmgLevel = (dmg*0.01*info.getLevel());
			dmg *= crit ? 1.1 : 0.8;
		} else {
			if(crit) {
				dmg *= 1.75;
				wplyw *= 1.25;
			}
				
			addDmg = wplyw * stats.getFinalInteligencja() * dmg / 100.0;
			dmg += addDmg;
				
			dmgLevel = dmg * 0.005 * info.getLevel();
			dmgMana = dmg * 0.0003 * stats.getFinalMana();
		}
		
		dmgKrag = dmg * 0.05 * stats.getKrag();
		
		dmg += dmgKrag;
		dmg += dmgLevel;
		dmg += dmgMana;
		dmg = DamageUtils.randomizeDamage(dmg, stats);

		pair.setKey(dmg);
		return pair;
	}

}
