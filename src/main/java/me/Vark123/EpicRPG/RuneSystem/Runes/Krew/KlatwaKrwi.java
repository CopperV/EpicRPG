package me.Vark123.EpicRPG.RuneSystem.Runes.Krew;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Events.RuneCastCostCalcEvent;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;
import me.Vark123.EpicRPG.Utils.Utils;

public class KlatwaKrwi extends ACastableRune {
	
	private static final DustOptions dust = new DustOptions(Color.fromRGB(205, 0, 0), 0.2f);
	private static final Random rand = new Random();
	
	public KlatwaKrwi(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.KLATWA_KRWI,
				player,
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_EVOKER_PREPARE_SUMMON, 1f, 0.7f);
				}, 
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1f);
				
					Location _loc = target.getLocation().clone().add(0, 1, 0);

					double force = rand.nextDouble(0.01, 0.05);
					_loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, _loc, 20, 0.4f, 0.8f, 0.4f, force);
				},
				new TimingRuneEffect(4, target -> {
					Location _loc = target.getLocation().clone().add(0, 1, 0);
					_loc.getWorld().spawnParticle(Particle.DUST, _loc, 6,
							0.4, 0.9, 0.4, rand.nextDouble(0.08, 0.2), dust);
					_loc.getWorld().spawnParticle(Particle.RAID_OMEN, _loc, 1,
							0.4, 0.9, 0.4, rand.nextDouble(0.01, 0.06));
				}),
				new TimingRuneEffect(20, target -> {
					RuneCastCostCalcEvent calcCostEvent = new RuneCastCostCalcEvent(rpgPlayer, rune);
					Bukkit.getPluginManager().callEvent(calcCostEvent);
					if(calcCostEvent.isCancelled())
						return;
					
					Utils.takeEntityHp(target, calcCostEvent.getFinalCost());
				}));
	}

}
