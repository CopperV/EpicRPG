package me.Vark123.EpicRPG.RuneSystem.Runes.Tajemna;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.MissileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.EmptyEntityRuneCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPAllyRuneCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPAllyRuneCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class PozeraczDusz_M extends ACastableRune {

	private static final Random random = new Random();
	
	private IRuneHitCondition hitCondition;
	private IRuneHitCondition emptyCondition;
	private IRuneHitCondition projectileCondition;
	private BoundingBox boundingBox;
	
	private int regenValue;
	
	public PozeraczDusz_M(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);

		hitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		emptyCondition = new EmptyEntityRuneCondition();
		projectileCondition = rune.getPvp() == 1 ?
				new PvPAllyRuneCondition() : new NonPvPAllyRuneCondition();
		boundingBox = new BoundingBox(
				-0.42, -0.42, -0.42, 
				0.42, 0.42, 0.42);
		
		RpgStats stats = rpgPlayer.getStats();
		regenValue = (int) (1.25 * (0.04 * rpgPlayer.getInfo().getLevel() + 0.02 * stats.getFinalMana() + 0.1 * stats.getFinalInteligencja()));
	}
	
	@Override
	public void castSpell() {
		Location startLoc = player.getLocation().clone().add(0, 1, 0);
		InstantRangeRuneTemplate.castEffect(
				this, 
				startLoc,
				rune.getObszar(),
				0, 
				0,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_AMBIENT, 1, 0.1f);
					
					loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 12, 0.4f, 0.6f, 0.4f, 0.03f);
				}, 
				hitCondition, 
				(loc, entity) -> {
					if(RuneUtils.damage(player, entity, rune)) {
						castProjectile(entity.getEyeLocation());
					}
				});
	}
	
	private void castProjectile(Location startLoc) {
		ProjectileRuneTemplate.castProjectile(
				this,
				startLoc,
				new Vector(0,1,0),
				0.4,
				1,
				1,
				4,
				new BoundingBox(),
				loc -> {
					float pitch = random.nextFloat(0.05f, 0.15f);
					loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_SHOOT, 1, pitch);
				},
				loc -> {
					loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 4, 0.2f, 0.2f, 0.2f, 0.02f);
				}, 
				emptyCondition,
				(loc, entity) -> { },
				loc -> {
					castMissile(loc);
				});
	}
	
	private void castMissile(Location startLoc) {
		MissileRuneTemplate.castMissile(
				this,
				startLoc, 
				player,
				0.2,
				1,
				1,
				rune.getObszar()*1.5,
				boundingBox,
				loc -> { },
				loc -> {
					loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 4, 0.2f, 0.2f, 0.2f, 0.02f);
				},
				projectileCondition,
				(loc, entity) -> {
					if(!(entity instanceof Player target))
						return;

					RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(target);
					rpg.getStats().addPresentManaSmart(regenValue);
					loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, random.nextFloat(0.1f, 0.3f));
					
					Location loc2 = entity.getLocation().clone().add(0, 1, 0);
					loc2.getWorld().spawnParticle(Particle.SOUL, loc2, 12,
							0.4f, 0.8f, 0.4f, 0.03);
				},
				loc -> { });
	}

}
