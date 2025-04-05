package me.Vark123.EpicRPG.RuneSystem.Runes.Mrok;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneUtils;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.StunRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPRuneHitCondition;

public class OdwrocenieUwagi extends ACastableRune {
	
	private IRuneHitCondition hitCondition;

	public OdwrocenieUwagi(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = new NonPvPRuneHitCondition();
	}
	
	@Override
	public void castSpell() {
		ACastableRune castableRune = this;
		
		Location startLoc = player.getLocation().clone().add(0, 0.5, 0);
		InstantRangeRuneTemplate.castEffect(
				this,
				startLoc,
				rune.getObszar(),
				0,
				0,
				loc -> {
					loc.getWorld().playSound(loc, Sound.ITEM_MACE_SMASH_GROUND_HEAVY, 1.5f, 1.4f);
					loc.getWorld().spawnParticle(Particle.WHITE_SMOKE, loc.clone().add(0,1,0), 80, 2, 2, 2, 0.08f);
				},
				hitCondition,
				(loc, entity) -> {
					AbstractEntity ae = BukkitAdapter.adapt(entity);
					if(!ae.hasAI())
						return;
										
					RuneUtils.damage(player, entity, rune, (damager, victim, _rune) -> {
						StunRuneTemplate.castEffect(this, entity);
						ae.setAI(false);
						
						Vector vec = victim.getLocation().toVector().subtract(damager.getLocation().toVector());
						Location eLoc = entity.getLocation().clone().setDirection(vec);
						entity.teleport(eLoc);
						
						new BukkitRunnable() {
							int timer = _rune.getDurationTime() * 20;
							@Override
							public void run() {
								if(isCancelled())
									return;
								if(timer <= 0 
										|| !castableRune.casterInCastWorld() 
										|| !castableRune.entityInCastWorld(victim)
										|| victim.isDead()) {
									ae.setAI(true);
									cancel();
									return;
								}
								--timer;

								MythicBukkit.inst().getVolatileCodeHandler().getEntityHandler().setLocation(
										ae, eLoc.getX(), eLoc.getY(), eLoc.getZ(), 
										eLoc.getYaw(), eLoc.getPitch());
							}
						}.runTaskTimer(Main.getInstance(), 0, 1);
					});
				});
	}

}
