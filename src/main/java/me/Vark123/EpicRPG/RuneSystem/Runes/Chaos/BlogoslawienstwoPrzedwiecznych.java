package me.Vark123.EpicRPG.RuneSystem.Runes.Chaos;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.RuneManager;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.NonPvPAllyRuneCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PvPAllyRuneCondition;
import me.Vark123.EpicRPG.Utils.Utils;

public class BlogoslawienstwoPrzedwiecznych extends ACastableRune {

	private static final Random rand = new Random();
	private IRuneHitCondition hitCondition;
	
	@Getter
	private static Map<UUID, BlogoslawienstwoPrzedwiecznychEffect> affected = new ConcurrentHashMap<>();
	
	public BlogoslawienstwoPrzedwiecznych(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = rune.getPvp() == 1 ? 
				new PvPAllyRuneCondition() : new NonPvPAllyRuneCondition();
	}

	@Override
	public void castSpell() {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
		RpgModifiers modifiers = rpg.getModifiers();
		
		if(rand.nextDouble() < modifiers.getBlogoslawienstwoPrzedwiecznychRenewChance()) {
			player.sendMessage(Main.getInstance().getPrefix()+" §aPrzedwieczni obdarzyli Ciebie swoim blogoslawienstwem!");
			
			player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 0.6f, 0.8f);
			
			modifiers.setBlogoslawienstwoPrzedwiecznychRenewChance(modifiers.getBlogoslawienstwoPrzedwiecznychRenewChance()*0.9);
			
			RuneManager.get().getRuneCd().get(player.getUniqueId()).remove(rune.getMythicType());
		} else {
			player.sendMessage(Main.getInstance().getPrefix()+" §aPrzedwieczni odwrocili sie od Ciebie!");
			
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 0.6f, 0.8f);
			
			modifiers.setBlogoslawienstwoPrzedwiecznychRenewChance(RpgModifiers.getBlogoslawienstwoPrzedwiecznychBaseRenewPercent());
			
			Location loc = player.getLocation().clone().add(0,3,0);
			for(int i = 0; i < 15; ++i) {
				double x = rand.nextDouble(1.5) - 0.75;
				double z = rand.nextDouble(1.5) - 0.75;
				float speed = rand.nextFloat(0.23f) + 0.12f;
				Location tmp = loc.clone().add(x,0,z);
				player.getWorld().spawnParticle(Particle.LARGE_SMOKE, tmp, 0, 0, -1, 0, speed);
			}
		}

		player.sendMessage(Main.getInstance().getPrefix()+" §aLaska Przedwiecznych wylewa sie na Ciebie i Twoich kompanow!");
		InstantRangeRuneTemplate.castEffect(
				this,
				castLoc,
				rune.getObszar(),
				0,
				0,
				loc -> { },
				hitCondition,
				(loc, entity) -> {
					UUID uid = entity.getUniqueId();
					if(affected.containsKey(uid)) {
						BlogoslawienstwoPrzedwiecznychEffect effect = affected.get(uid);
						effect.ResetTimer();
						effect.setLevel(effect.getLevel()+1);
					} else {
						BlogoslawienstwoPrzedwiecznychEffect effect = new BlogoslawienstwoPrzedwiecznychEffect(entity, 1, rune.getDurationTime());
						effect.StartTimer();
					}
					
					for(int i = 0; i < 15; ++i) {
						double x = rand.nextDouble(1.5) - 0.75;
						double z = rand.nextDouble(1.5) - 0.75;
						float speed = rand.nextFloat(0.18f) + 0.12f;
						Location tmp = loc.clone().add(x,0,z);
						entity.getWorld().spawnParticle(Particle.DRAGON_BREATH, tmp, 0, 0, -1, 0, speed);
					}
				});
	}
	
	public class BlogoslawienstwoPrzedwiecznychEffect {
		private LivingEntity entity;
		private UUID uid;
		@Getter
		@Setter
		private int level;
		private int duration;
		private MutableDouble timer;
		private BukkitTask task;
		private BossBar bar;
		
		public BlogoslawienstwoPrzedwiecznychEffect(LivingEntity entity, int level, int duration) {
			this.entity = entity;
			this.uid = entity.getUniqueId();
			this.level = level;
			this.duration = duration;

			bar = Bukkit.createBossBar(rune.getName(), BarColor.WHITE, BarStyle.SOLID);
			bar.setProgress(1);
			bar.setVisible(false);
			if(entity instanceof Player) {
				bar.addPlayer((Player) entity);
			}
		}
		
		public void StartTimer() {
			if(task != null && !task.isCancelled())
				task.cancel();
			
			if(bar != null) {
				bar.setTitle(rune.getName()+" §r§7[§f§l"+timer.intValue()+"§r§7]");
				bar.setProgress(1);
				bar.setVisible(true);
			}
			
			Utils.setEntityBuff(entity, EpicModifierTypes.BLOGOSLAWIENSTWO_PRZEDWIECZNYCH);
			
			timer.setValue(duration);
			task = new BukkitRunnable() {
				@Override
				public void run() {
					if(isCancelled()) {
						bar.removeAll();
						affected.remove(uid);
						
						if(entity.equals(player)) {
							RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
							RpgModifiers modifiers = rpg.getModifiers();

							modifiers.setBlogoslawienstwoPrzedwiecznychRenewChance(RpgModifiers.getBlogoslawienstwoPrzedwiecznychBaseRenewPercent());
						}
					}
					
					if(timer.intValue() <= 0 || entity.isDead()
						|| (entity instanceof Player && !((Player)entity).isOnline()) 
						|| !Utils.hasEntityBuff(entity, EpicModifierTypes.BLOGOSLAWIENSTWO_PRZEDWIECZNYCH)) {
						bar.removeAll();
						affected.remove(uid);
						
						entity.sendMessage(Main.getInstance().getPrefix()+" §aEfekt dzialania runy §r"+rune.getName()+" §askonczyl sie");
						entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_WITHER_DEATH, 0.9f, 0.7f);
						
						Utils.unsetEntityBuff(entity, EpicModifierTypes.BLOGOSLAWIENSTWO_PRZEDWIECZNYCH);
						
						if(entity.equals(player)) {
							RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(player);
							RpgModifiers modifiers = rpg.getModifiers();

							modifiers.setBlogoslawienstwoPrzedwiecznychRenewChance(RpgModifiers.getBlogoslawienstwoPrzedwiecznychBaseRenewPercent());
						}
						
						cancel();
						return;
					}
					
					entity.getWorld().spawnParticle(Particle.DRAGON_BREATH, entity.getLocation().clone().add(0,1,0), 4,
							0.6, 0.6, 0.6, 0.12);
					
					bar.setTitle(rune.getName()+" §r§7[§f§l"+timer.intValue()+"§r§7]");
					bar.setProgress(Utils.limitValue(0, 1, timer.doubleValue() / (double)duration));
					
					timer.subtract(0.25);
				}
			}.runTaskTimer(Main.getInstance(), 0, 5);
			
			affected.put(uid, this);
		}
		
		public void ResetTimer() {
			timer.setValue(duration);
		}
	}

}
