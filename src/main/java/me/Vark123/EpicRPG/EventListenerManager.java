package me.Vark123.EpicRPG;

import org.bukkit.Bukkit;
import org.bukkit.event.EventPriority;

import me.Vark123.EpicRPG.Chat.ChatMsgSendEvent;
import me.Vark123.EpicRPG.FightSystem.LavaDamageEvent;
import me.Vark123.EpicRPG.FightSystem.RpgDamageEvent;
import me.Vark123.EpicRPG.FightSystem.RpgDeathEvent;
import me.Vark123.EpicRPG.FightSystem.ShootEvent;
import me.Vark123.EpicRPG.FightSystem.Modifiers.DamageModifierManager;
import me.Vark123.EpicRPG.FightSystem.Modifiers.DamageModifyEvent;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.BeeStingModifier;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.EntityTauntModifier;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.HPInfo;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.LodowyBlokModifier;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.ManekinDamage;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.NoDamageTicksModifier;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.RytualKrwiModifier;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.ScaleDamageModifier;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.ShulkerModifier;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.StrengthModifier;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.TransfuzjaModifier;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.WeaknessModifier;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.WedrownyCienModifier;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.ZewKrwiModifier;
import me.Vark123.EpicRPG.HealthSystem.PlayerHealEvent;
import me.Vark123.EpicRPG.HorseSystem.HorseDismountEvent;
import me.Vark123.EpicRPG.HorseSystem.HorseInventoryEvent;
import me.Vark123.EpicRPG.HorseSystem.HorseRemoveOnPlayerDeathEvent;
import me.Vark123.EpicRPG.HorseSystem.HorseStickUseEvent;
import me.Vark123.EpicRPG.MMExtension.CustomConditionLoadEvent;
import me.Vark123.EpicRPG.MMExtension.CustomMechanicsLoadEvent;
import me.Vark123.EpicRPG.MMExtension.CustomTargeterLoadEvent;
import me.Vark123.EpicRPG.MMExtension.Misc.ProtectorDropKillEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerJoinEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerQuitEvent;
import me.Vark123.EpicRPG.Potions.PotionDrinkEvent;
import me.Vark123.EpicRPG.RubySystem.RubyUseEvent;
import me.Vark123.EpicRPG.RuneSystem.RuneInteractEvent;
import me.Vark123.EpicRPG.RuneSystem.Events.EksplodujacaStrzalaHitEffectEvent;
import me.Vark123.EpicRPG.RuneSystem.Events.GradStrzalLaunchEvent;
import me.Vark123.EpicRPG.RuneSystem.Events.KamiennyObserwatorEvent;
import me.Vark123.EpicRPG.RuneSystem.Events.LodowyBlokDisableMoveEvent;
import me.Vark123.EpicRPG.RuneSystem.Events.WedrownyCienTargetEvent;

public class EventListenerManager {

	private static final Main inst = Main.getInstance();
	
	public static void registerEvents() {
		Bukkit.getPluginManager().registerEvents(new PlayerJoinEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new ChatMsgSendEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new PlayerHealEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new ShootEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new RpgDamageEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new RpgDeathEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new DamageModifyEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new LavaDamageEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new RuneInteractEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new PotionDrinkEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new RubyUseEvent(), inst);
		
		Bukkit.getPluginManager().registerEvents(new EksplodujacaStrzalaHitEffectEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new GradStrzalLaunchEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new KamiennyObserwatorEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new LodowyBlokDisableMoveEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new WedrownyCienTargetEvent(), inst);
		
		Bukkit.getPluginManager().registerEvents(new CustomMechanicsLoadEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new CustomTargeterLoadEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new CustomConditionLoadEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new ProtectorDropKillEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new HorseDismountEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new HorseInventoryEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new HorseRemoveOnPlayerDeathEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new HorseStickUseEvent(), inst);
		
		//Damage Modifiers
		DamageModifierManager.getInstance().registerModifier(new WedrownyCienModifier(), EventPriority.LOWEST);
		DamageModifierManager.getInstance().registerModifier(new WeaknessModifier(), EventPriority.LOW);
		DamageModifierManager.getInstance().registerModifier(new StrengthModifier(), EventPriority.LOW);
		DamageModifierManager.getInstance().registerModifier(new ShulkerModifier(), EventPriority.NORMAL);
		DamageModifierManager.getInstance().registerModifier(new ScaleDamageModifier(), EventPriority.HIGH);
		DamageModifierManager.getInstance().registerModifier(new LodowyBlokModifier(), EventPriority.HIGHEST);
		DamageModifierManager.getInstance().registerModifier(new TransfuzjaModifier(), EventPriority.HIGHEST);
		DamageModifierManager.getInstance().registerModifier(new EntityTauntModifier(), EventPriority.MONITOR);
		DamageModifierManager.getInstance().registerModifier(new ManekinDamage(), EventPriority.MONITOR);
		DamageModifierManager.getInstance().registerModifier(new ZewKrwiModifier(), EventPriority.MONITOR);
		DamageModifierManager.getInstance().registerModifier(new RytualKrwiModifier(), EventPriority.MONITOR);
		DamageModifierManager.getInstance().registerModifier(new HPInfo(), EventPriority.MONITOR);
		DamageModifierManager.getInstance().registerModifier(new NoDamageTicksModifier(), EventPriority.MONITOR);
		DamageModifierManager.getInstance().registerModifier(new BeeStingModifier(), EventPriority.MONITOR);
	}
	
}
