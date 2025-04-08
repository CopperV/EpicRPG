package me.Vark123.EpicRPG.RuneSystem.Runes.Chaos;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.WaveRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class MasoweZniszczenie extends ACastableRune {
	
	private static final Random rand = new Random();
	private IRuneHitCondition hitCondition;

	public MasoweZniszczenie(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.25, 0);
		
		WaveRuneTemplate.castWave(
				this,
				startLoc, 
				rune.getObszar(),
				4, 
				0.1, 
				1,
				1, 
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_EVOKER_PREPARE_SUMMON, 1.5f, 0.1f);
				},
				loc -> {
					for(int i = 0; i < 5; ++i) {
						DustOptions dust = new DustOptions(Color.PURPLE, rand.nextFloat(2.5f, 4.5f));
						loc.getWorld().spawnParticle(Particle.DUST, loc, 1,
							0.25, 1.25, 0.25, 0.05, dust);
					}
				}, 
				hitCondition, 
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune)) {
						loc.getWorld().playSound(loc, Sound.ENTITY_EVOKER_PREPARE_SUMMON, 0.7f, 3);
					}
				},
				loc -> { });
	}

}
