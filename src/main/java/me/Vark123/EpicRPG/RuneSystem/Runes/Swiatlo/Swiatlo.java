package me.Vark123.EpicRPG.RuneSystem.Runes.Swiatlo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgModifiers.EpicModifierTypes;
import me.Vark123.EpicRPG.RuneSystem.ACastableRune;
import me.Vark123.EpicRPG.RuneSystem.EpicRune;
import me.Vark123.EpicRPG.RuneSystem.Functional.IRuneHitCondition;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.BufferRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.InstantRangeRuneTemplate;
import me.Vark123.EpicRPG.RuneSystem.Templates.CastSpells.TimingEffectRuneTemplate.TimingRuneEffect;
import me.Vark123.EpicRPG.RuneSystem.Templates.EntityHits.PlayerRuneCondition;

public class Swiatlo extends ACastableRune {

	private static final BlockData lightData = Bukkit.createBlockData(Material.LIGHT, consumer -> {
		if(consumer instanceof Levelled levelled)
			levelled.setLevel(15);
	});
	private IRuneHitCondition hitCondition;
	
	public Swiatlo(RpgPlayer rpgPlayer, EpicRune rune) {
		super(rpgPlayer, rune);
		
		hitCondition = new PlayerRuneCondition();
		
	}

	@Override
	public void castSpell() {
		ACastableRune castableRune = this;
		
		BufferRuneTemplate.castEffect(
				this,
				rune.getName(),
				EpicModifierTypes.ZAKLETA_STRZALA,
				player,
				target -> {
					target.getWorld().playSound(target.getLocation(), Sound.ENTITY_EVOKER_PREPARE_SUMMON, 2.5f, 1.3f);
				
					new BukkitRunnable() {
						int timer = rune.getDurationTime()*20;
						Block lastBlock = null;
						BlockData lastBlockData = null;
						@Override
						public void run() {
							if(isCancelled())
								return;
							if(!castableRune.casterInCastWorld() || timer <= 0) {
								if(lastBlock != null)
									sendBlockChange(lastBlock, lastBlockData);
								
								cancel();
								return;
							}
							--timer;

							Block block = player.getLocation().clone().add(0,1,0).getBlock();
							if(lastBlock == null) {
								if(block.getType().equals(Material.AIR) || block.getType().equals(Material.LIGHT)) {
									lastBlock = block;
									lastBlockData = block.getBlockData();
									
									sendBlockChange(block, lightData);
								}
							} else {
								if(block.getType().equals(Material.AIR) || block.getType().equals(Material.LIGHT)) {
									sendBlockChange(lastBlock, lastBlockData);
									lastBlock = block;
									lastBlockData = block.getBlockData();
									sendBlockChange(block, lightData);
								} else {
									sendBlockChange(lastBlock, lightData);
								}
							}
							
						}
					}.runTaskTimer(Main.getInstance(), 0, 1);
				}, 
				target -> { },
				new TimingRuneEffect(4, target -> {
					Location _loc = target.getEyeLocation().clone().add(0, 1, 0);
					_loc.getWorld().spawnParticle(Particle.END_ROD, _loc, 5, 0.15f, 0.15f, 0.15f, 0.02f);
				}));
		
	}
	
	private void sendBlockChange(Block block, BlockData blockData) {
		InstantRangeRuneTemplate.castEffect(
				this,
				block.getLocation(),
				16*4,
				0,
				0,
				loc -> { },
				hitCondition,
				(loc, entity) -> {
					if(!(entity instanceof Player target))
						return;
					
					target.sendBlockChange(block.getLocation(), blockData);
				});
	}

}
