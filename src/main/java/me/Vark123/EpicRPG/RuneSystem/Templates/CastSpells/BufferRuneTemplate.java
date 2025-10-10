package me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneDisplayGetter;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneEntityEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;
import me.Vark123.EpicRPG.Utils.Utils;

public class BufferRuneTemplate {

	private BufferRuneTemplate() { }
	
	public static void castEffect(
			ACastableRune castableRune,
			String title,
			EpicModifierTypes modifier,
			LivingEntity target,
			IRuneEntityEffect startEffect,
			IRuneEntityEffect endEffect,
			TimingRuneEffect... effects) {
		castEffect(
				castableRune,
				(__1, __2) -> title,
				modifier, target,
				startEffect,
				endEffect,
				effects);
	}
	
	public static void castEffect(
			ACastableRune castableRune,
			IRuneDisplayGetter title,
			EpicModifierTypes modifier,
			LivingEntity target,
			IRuneEntityEffect startEffect,
			IRuneEntityEffect endEffect,
			TimingRuneEffect... effects) {
		int duration = castableRune.getRune().getDurationTime();
		
		Player caster = castableRune.getPlayer();
		target.sendMessage(Main.getInstance().getPrefix()+" §aUzyto runy §r"+castableRune.getRune().getName());
		
		if(startEffect != null)
			startEffect.playEffect(target);
		
		Utils.setEntityBuff(target, modifier);
		new BukkitRunnable() {
			double timer = duration;
			@Override
			public void run() {
				
				if(!castableRune.casterInCastWorld() || timer <= 0 
						|| target.isDead() || caster.isDead()
						|| (target instanceof Player && !((Player)target).isOnline()) || !caster.isOnline()
						|| !target.getWorld().getName().equals(caster.getWorld().getName())
						|| !Utils.hasEntityBuff(target, modifier)) {
					
					target.sendMessage(Main.getInstance().getPrefix()+" §aEfekt dzialania runy §r"+castableRune.getRune().getName()+" §askonczyl sie");
					
					if(endEffect != null)
						endEffect.playEffect(target);
					
					Utils.unsetEntityBuff(target, modifier);
					
					cancel();
					return;
				}
				
				--timer;
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
		
		if(target instanceof Player) {
			new BukkitRunnable() {
				double timer = duration;
				BossBar bar = Bukkit.createBossBar(title+" §r§7[§f§l"+duration+"§r§7]", BarColor.WHITE, BarStyle.SOLID);{
					bar.setVisible(true);
					bar.setProgress(1);
					bar.addPlayer((Player)target);
				}
				@Override
				public void run() {
					if(!castableRune.casterInCastWorld() || timer <= 0 
							|| target.isDead() || caster.isDead()
							|| !((Player)target).isOnline() || !caster.isOnline()
							|| !target.getWorld().getName().equals(caster.getWorld().getName())
							|| !Utils.hasEntityBuff(target, modifier)) {
						bar.removeAll();
						bar.setVisible(false);
						
						cancel();
						return;
					}
					
					bar.setTitle(title.getDisplay(castableRune, target)+" §r§7[§f§l"+(int)timer+"§r§7]");
					bar.setProgress(timer / (double)duration);
					
					--timer;
				}
			}.runTaskTimer(Main.getInstance(), 0, 20);
		}
		
		for(var effect : effects) {
			new BukkitRunnable() {
				int timer = (int) (duration * (20. / (double) effect.interval));
				@Override
				public void run() {
					if(isCancelled())
						return;
					if(!castableRune.casterInCastWorld() || timer <= 0 
							|| target.isDead() || caster.isDead()
							|| !target.getWorld().getName().equals(caster.getWorld().getName())
							|| !Utils.hasEntityBuff(target, modifier)) {
						cancel();
						return;
					}
					--timer;
					
					effect.effect.playEffect(target);
				}
			}.runTaskTimer(Main.getInstance(), 0, effect.interval);
		}
		
	}
	
}
