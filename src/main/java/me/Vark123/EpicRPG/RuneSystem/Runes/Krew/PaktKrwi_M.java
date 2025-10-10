package me.Vark123.EpicRPG.RuneSystem.Runes.Krew;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.Sound;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;
import me.Vark123.EpicRPG.Utils.Utils;

public class PaktKrwi_M extends ACastableRune {

	private static final double red = 138./255.;
	private static final double green = 3./255.;
	private static final double blue = 3./255.;
	private static final DustOptions dust = new DustOptions(Color.fromRGB(128, 3, 3), 1.2f);
	private static final Random rand = new Random();
	
	private static final double PACT_HP = 450.;
	
	public PaktKrwi_M(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
	}

	@Override
	public void castSpell() {
		rpgPlayer.getModifiers().setPaktKrwiHp_m(PACT_HP);
		
		rpgPlayer.getModifiers().addActiveLocker(RuneLockerTypes.PAKT_KRWI);

		BossBar hpBar = Bukkit.createBossBar("§4KRWAWA ZAPLATA", BarColor.RED, BarStyle.SOLID);{
			hpBar.setVisible(true);
			hpBar.setProgress(0);
			hpBar.addPlayer(player);
		}
		
		BufferRuneTemplate.castEffect(
				this,
				"§4KRWAWA ZAPLATA",
				EpicModifierTypes.PAKT_KRWI_MEASURE_M,
				player,
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 1f, 0.75f);
				}, 
				target -> {
					target.sendMessage(Main.getInstance().getPrefix()+" §c§lTWOJA DUSZA NALEZY DO MNIE");
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1f);
					
					rpgPlayer.getModifiers().removeActiveLocker(RuneLockerTypes.PAKT_KRWI);
					
					hpBar.removeAll();
					hpBar.setVisible(false);
				
					if(Utils.hasEntityBuff(target, EpicModifierTypes.PAKT_KRWI_MEASURE_M))
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
							target.damage(1);
							target.setHealth(0);
						}, 10);
				},
				new TimingRuneEffect(4, target -> {
					Location _loc = target.getLocation().clone().add(0, 1, 0);
					for(int i = 0; i < 6; ++i) {
						double x = rand.nextDouble(1.2) - 0.6;
						double y = rand.nextDouble(1.2) - 0.6;
						double z = rand.nextDouble(1.2) - 0.6;
						
						Location tmp = _loc.clone().add(x,y,z);
						target.getWorld().spawnParticle(Particle.ENTITY_EFFECT, tmp, 0, red, green, blue, 1);
					}
					_loc.getWorld().spawnParticle(Particle.DUST, _loc, 6,
							0.4, 0.9, 0.4, rand.nextDouble(0.08, 0.2), dust);
				}),
				new TimingRuneEffect(2, target -> {
					if(rpgPlayer.getModifiers().getPaktKrwiHp_m() <= 0) {
						Utils.unsetEntityBuff(target, EpicModifierTypes.PAKT_KRWI_MEASURE_M);
						
						hpBar.removeAll();
						hpBar.setVisible(false);
						
						castPactEffect();
					}
					
					hpBar.setProgress(Utils.limitValue(0, 1, 1 - rpgPlayer.getModifiers().getPaktKrwiHp() / PACT_HP));
				}));
	}
	
	private void castPactEffect() {
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.PAKT_KRWI_M,
				player,
				target -> {
					rpgPlayer.getModifiers().removeActiveLocker(RuneLockerTypes.PAKT_KRWI);
					
					target.sendMessage(Main.getInstance().getPrefix()+" §c§lPAKT DOKONANY");
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, 1f, 0.6f);
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
				}));
	}

}
