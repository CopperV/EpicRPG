package me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.RuneEffectType;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneEntityEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;
import me.Vark123.EpicRPG.Utils.Utils;

public class RuneEffectRuneTemplate {

	private RuneEffectRuneTemplate() { }
	
	public static void castEffect(
			ACastableRune castableRune,
			RuneEffectType effect,
			LivingEntity target,
			IRuneEntityEffect startEffect,
			IRuneEntityEffect endEffect,
			TimingRuneEffect... effects) {
		int duration = castableRune.getRune().getDurationTime();
		
		Player caster = castableRune.getPlayer();
		
		if(startEffect != null)
			startEffect.playEffect(target);
		
		Utils.setEntityEffect(caster, target, effect);
		new BukkitRunnable() {
			double timer = duration;
			@Override
			public void run() {
				if(!Utils.hasEntityEffect(caster, target, effect)) {
					cancel();
					return;
				}
				
				if(!castableRune.casterInCastWorld() || timer <= 0 
						|| target.isDead() || caster.isDead()
						|| (target instanceof Player && !((Player)target).isOnline()) || !caster.isOnline()
						|| !target.getWorld().getName().equals(caster.getWorld().getName())
						|| !Utils.hasEntityEffect(caster, target, effect)) {
								
					Utils.unsetEntityEffect(target, effect);
					
					if(endEffect != null)
						endEffect.playEffect(target);
					
					cancel();
					return;
				}
				
				--timer;
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		for(var eff : effects) {
			new BukkitRunnable() {
				int timer = (int) (duration * (20. / (double) eff.interval));
				@Override
				public void run() {
					if(isCancelled())
						return;
					if(!castableRune.casterInCastWorld() || timer <= 0 
							|| target.isDead() || caster.isDead()
							|| !target.getWorld().getName().equals(caster.getWorld().getName())) {
						cancel();
						return;
					}
					--timer;
					
					eff.effect.playEffect(target);
				}
			}.runTaskTimer(Main.getInstance(), 0, eff.interval);
		}
		
	}
	
}
