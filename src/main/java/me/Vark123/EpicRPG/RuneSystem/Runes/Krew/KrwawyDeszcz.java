package me.Vark123.EpicRPG.RuneSystem.Runes.Krew;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Effects.DamageCustomEffect;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRunePostDamageEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.RainRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class KrwawyDeszcz extends ACastableRune {
	
	private static final DustOptions dust = new DustOptions(Color.RED, 0.8f);
	private static final Random rand = new Random();
	
	private IRuneHitCondition hitCondition;
	private BoundingBox boundingBox;
	private IRunePostDamageEffect hitEffect;

	public KrwawyDeszcz(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.25, -0.25, -0.25, 
				0.25, 0.25, 0.25);
		hitEffect = new DamageCustomEffect(
				15,
				30,
				rune.getDamage()*0.15,
				this,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT, 0.9f, rand.nextFloat(0.7f, 1.1f));
					loc.getWorld().spawnParticle(Particle.DUST, loc.clone().add(0,1,0), 6,
							0.4, 0.4, 0.4, rand.nextDouble(0.1, 0.25), dust);
				});
	}

	@Override
	public void castSpell() {
		RainRuneTemplate.castRain(
				this,
				castLoc,
				rune.getObszar(),
				rune.getDurationTime(),
				0.4, 
				3,
				4, 
				2,
				1,
				(loc1, loc2) -> {
					Vector vec1 = castLoc.getDirection().normalize();
					Vector vec2 = new Vector(0, -2.5, 0);
					return vec1.add(vec2).normalize();
				}, 
				35, 
				50,
				boundingBox,
				loc -> {
					loc.getWorld().playSound(loc, Sound.WEATHER_RAIN, 2.5f, 0.1f);
				}, 
				loc -> {
					for(int i = 0; i < 6; ++i) {
						loc.getWorld().spawnParticle(Particle.DUST, loc, 1,
								0.2, 0.35, 0.2, rand.nextDouble(0.15, 0.35), new DustOptions(Color.RED, rand.nextFloat(1, 2.5f)));
					}
					for(int i = 0; i < 8; ++i) {
						loc.getWorld().spawnParticle(Particle.ENTITY_EFFECT, loc, 1,
								0.2, 0.35, 0.2, rand.nextDouble(0.3, 0.6), Color.fromRGB(192, 16, 16));
					}
				}, 
				hitCondition, 
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune, hitEffect)) {
						loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_DEATH, 0.8f, 1.15f);
					}
					
				}, 
				loc -> { },
				loc -> { },
				loc -> { });
	}

}
