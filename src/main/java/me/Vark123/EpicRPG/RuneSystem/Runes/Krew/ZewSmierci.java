package me.Vark123.EpicRPG.RuneSystem.Runes.Krew;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.Vark123.EpicRPG.HealthSystem.RpgPlayerHealEvent;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgStats;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.ProjectileRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPRuneHitCondition;

public class ZewSmierci extends ACastableRune {

	private static final Random rand = new Random();
	private static final DustOptions dust = new DustOptions(Color.RED, 0.8f);
	private IRuneHitCondition hitCondition;
	private IRuneHitCondition enemyHitCondition;
	private BoundingBox boundingBox;
	
	public ZewSmierci(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = (Player caster, LivingEntity hit) -> {
			if(hit instanceof Player)
				return true;

			if(!MythicBukkit.inst().getMobManager().isMythicMob(hit))
				return false;
			
			ActiveMob aMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(hit);
			if(aMob.isDead())
				return false;
			
			return true;
		};
		enemyHitCondition = rune.getPvp() == 1 ? 
				new PvPRuneHitCondition() : new NonPvPRuneHitCondition();
		boundingBox = new BoundingBox(
				-0.4, -0.4, -0.4, 
				0.4, 0.4, 0.4);
	}

	@Override
	public void castSpell() {
		Location loc = player.getLocation().clone().add(0, 1, 0);
		loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_SHOOT, 0.7f, 0.4f);

		double radius = rune.getObszar();
		for(int i = 0; i < 30; ++i) {
			double angle = rand.nextDouble(Math.PI*2);
			double x = Math.sin(angle) * radius;
			double z = Math.cos(angle) * radius;
			
			Location source = loc.clone().add(x,0,z);
			castProjectile(source, loc);
		}
	}
	
	private void castProjectile(Location source, Location target) {
		Vector vec = new Vector(
				target.getX() - source.getX(),
				target.getY() - source.getY(),
				target.getZ() - source.getZ()
				).normalize();
		
		ProjectileRuneTemplate.castProjectile(
				this, 
				source, 
				vec,
				0.06, 
				2,
				1,
				rune.getObszar(), 
				boundingBox,
				loc -> { }, 
				loc -> {
					loc.getWorld().spawnParticle(Particle.ENTITY_EFFECT, loc, 6, 
							0.15f, 0.15f, 0.15f, 0.25f, Color.fromRGB(138, 3, 3));
				}, 
				hitCondition, 
				(loc, e) -> {
					if(enemyHitCondition.check(player, e)) {
						if(RuneUtils.damage(player, e, rune)) {
							e.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1, 1.2f);
							e.getWorld().spawnParticle(Particle.DUST, e.getLocation().clone().add(0,1,0), 9, 
									0.4f, 0.5f, 0.4f, 0.4f, dust);
						}
					} else {
						RpgStats stats = rpgPlayer.getStats();
						double value = 0.01*rpgPlayer.getInfo().getLevel() + 0.025*stats.getFinalMana() + 0.06*stats.getFinalInteligencja();
						
						Player player = (Player) e;
						RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);

						RpgPlayerHealEvent event = new RpgPlayerHealEvent(rpg, value);
						Bukkit.getPluginManager().callEvent(event);
						if(!event.isCancelled()) {
							e.getWorld().playSound(loc, Sound.ENTITY_CAT_HISS, 0.9f, 1.25f);
							e.getWorld().spawnParticle(Particle.HEART, e.getLocation().clone().add(0,1,0), 9, 
									0.5f, 0.5f, 0.5f, 0.1f);
						}
					}
				}, 
				loc -> { });
	}

}
