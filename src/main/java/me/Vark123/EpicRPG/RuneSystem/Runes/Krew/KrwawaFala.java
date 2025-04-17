package me.Vark123.EpicRPG.RuneSystem.Runes.Krew;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Particle.DustOptions;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.WaveRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class KrwawaFala extends ACastableRune {
	
	private static final DustOptions dust = new DustOptions(Color.fromRGB(128, 0, 0), 2);
	private static final Random rand = new Random();
	
	private IRuneHitCondition hitCondition;
	
	public KrwawaFala(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
	}

	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1.1, 0);
		
		WaveRuneTemplate.castWave(
				this,
				startLoc, 
				rune.getObszar(),
				7, 
				0.19, 
				2,
				1, 
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_BIG_FALL, 1.25f, 0.1f);
				},
				loc -> {
					loc.getWorld().spawnParticle(Particle.ENTITY_EFFECT, loc.clone().add(0, -1, 0), 5,
							0.3, 0.05, 0.3, rand.nextDouble(0.25, 0.65), Color.fromRGB(128, 0, 0));
					loc.getWorld().spawnParticle(Particle.DUST, loc, 10,
							0.3, 1.1, 0.3, rand.nextDouble(0.1, 0.4), dust);
				}, 
				hitCondition, 
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune)) {
						loc.getWorld().playSound(loc, Sound.ENTITY_SLIME_ATTACK, 1.2f, 0.8f);
					}
					
				},
				loc -> { });
	}

}
