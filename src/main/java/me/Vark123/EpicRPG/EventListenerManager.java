package me.Vark123.EpicRPG;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.event.EventPriority;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import me.Vark123.EpicRPG.API.EpicRPGApi;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockAddAllEvent;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockAddEvent;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockEntryEvent;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockHealDebuffEvent;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockRemoveAllEvent;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockRemoveEvent;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockResetEvent;
import me.Vark123.EpicRPG.BoosterSystem.Listeners.BoosterModifyListener;
import me.Vark123.EpicRPG.Chat.ChatMsgSendEvent;
import me.Vark123.EpicRPG.Core.Events.PlayerUseDisabledBlockEvent;
import me.Vark123.EpicRPG.Core.Events.PlayerUseLeverEvent;
import me.Vark123.EpicRPG.Core.Listeners.ExecutableItemUseListener;
import me.Vark123.EpicRPG.Core.Listeners.LevelSystemControlListener;
import me.Vark123.EpicRPG.Core.Listeners.VipBoostControlListener;
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
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.ProjectileRuneEffectModifier;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.RytualKrwiModifier;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.ScaleDamageModifier;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.ShulkerModifier;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.StrengthModifier;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.TransfuzjaModifier;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.WeaknessModifier;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.WedrownyCienModifier;
import me.Vark123.EpicRPG.FightSystem.Modifiers.ModList.ZewKrwiModifier;
import me.Vark123.EpicRPG.Gems.GemPlaceProtEvent;
import me.Vark123.EpicRPG.HealthSystem.PlayerHealEvent;
import me.Vark123.EpicRPG.HorseSystem.HorseDismountEvent;
import me.Vark123.EpicRPG.HorseSystem.HorseInventoryEvent;
import me.Vark123.EpicRPG.HorseSystem.HorseRemoveOnPlayerDeathEvent;
import me.Vark123.EpicRPG.HorseSystem.HorseStickUseEvent;
import me.Vark123.EpicRPG.Jewelry.JewelryMenuOpenEvent;
import me.Vark123.EpicRPG.KosturSystem.KosturUseEvent;
import me.Vark123.EpicRPG.MMExtension.CustomConditionLoadEvent;
import me.Vark123.EpicRPG.MMExtension.CustomMechanicsLoadEvent;
import me.Vark123.EpicRPG.MMExtension.CustomTargeterLoadEvent;
import me.Vark123.EpicRPG.MMExtension.Misc.ProtectorDropKillEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerArrowWeaponUseEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerChangeEqEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerDropEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerJoinEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerQuitEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerRespawnEvent;
import me.Vark123.EpicRPG.Players.SkillControllers.HungerSkillEvent;
import me.Vark123.EpicRPG.Potions.PotionDrinkEvent;
import me.Vark123.EpicRPG.RubySystem.RubyPlaceProtEvent;
import me.Vark123.EpicRPG.RubySystem.RubyUseEvent;
import me.Vark123.EpicRPG.RuneSystem.RuneInteractEvent;
import me.Vark123.EpicRPG.RuneSystem.RuneTimeCheckEvent;
import me.Vark123.EpicRPG.RuneSystem.Events.EksplodujacaStrzalaHitEffectEvent;
import me.Vark123.EpicRPG.RuneSystem.Events.GradStrzalLaunchEvent;
import me.Vark123.EpicRPG.RuneSystem.Events.KamiennyObserwatorEvent;
import me.Vark123.EpicRPG.RuneSystem.Events.LodowyBlokDisableMoveEvent;
import me.Vark123.EpicRPG.RuneSystem.Events.ProwokacjaChangeTargetEvent;
import me.Vark123.EpicRPG.RuneSystem.Events.WedrownyCienTargetEvent;
import me.Vark123.EpicRPG.Scrolls.EpicBossScrollEvent;
import me.Vark123.EpicRPG.Scrolls.KatedraScrollEvent;
import me.Vark123.EpicRPG.Scrolls.KlasaResetScrollEvent;
import me.Vark123.EpicRPG.Scrolls.StatResetScrollEvent;

public class EventListenerManager {

	private static final Main inst = Main.getInstance();
	
	public static void registerEvents() {
		Bukkit.getPluginManager().registerEvents(new PlayerJoinEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerRespawnEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerDropEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerChangeEqEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerArrowWeaponUseEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new ChatMsgSendEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new JewelryMenuOpenEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new PlayerHealEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new ShootEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new RpgDamageEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new RpgDeathEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new DamageModifyEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new LavaDamageEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new RuneInteractEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new RuneTimeCheckEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new PotionDrinkEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new RubyUseEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new RubyPlaceProtEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new KosturUseEvent(), inst);
		
		Bukkit.getPluginManager().registerEvents(new EksplodujacaStrzalaHitEffectEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new GradStrzalLaunchEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new KamiennyObserwatorEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new LodowyBlokDisableMoveEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new WedrownyCienTargetEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new ProwokacjaChangeTargetEvent(), inst);
		
		Bukkit.getPluginManager().registerEvents(new CustomMechanicsLoadEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new CustomTargeterLoadEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new CustomConditionLoadEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new ProtectorDropKillEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new HorseDismountEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new HorseInventoryEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new HorseRemoveOnPlayerDeathEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new HorseStickUseEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new BlackrockAddAllEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new BlackrockAddEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new BlackrockEntryEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new BlackrockRemoveAllEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new BlackrockRemoveEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new BlackrockResetEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new BlackrockHealDebuffEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new KatedraScrollEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new EpicBossScrollEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new StatResetScrollEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new KlasaResetScrollEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new GemPlaceProtEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerUseDisabledBlockEvent(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerUseLeverEvent(), inst);
		
		Bukkit.getPluginManager().registerEvents(new HungerSkillEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new VipBoostControlListener(), inst);
		Bukkit.getPluginManager().registerEvents(new LevelSystemControlListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ExecutableItemUseListener(), inst);
		Bukkit.getPluginManager().registerEvents(new BoosterModifyListener(), inst);
		
		//Damage Modifiers
		DamageModifierManager.getInstance().registerModifier(new WedrownyCienModifier(), EventPriority.LOWEST);
		DamageModifierManager.getInstance().registerModifier(new WeaknessModifier(), EventPriority.LOW);
		DamageModifierManager.getInstance().registerModifier(new StrengthModifier(), EventPriority.LOW);
		DamageModifierManager.getInstance().registerModifier(new ShulkerModifier(), EventPriority.NORMAL);
		DamageModifierManager.getInstance().registerModifier(new ScaleDamageModifier(), EventPriority.HIGH);
		DamageModifierManager.getInstance().registerModifier(new LodowyBlokModifier(), EventPriority.HIGHEST);
		DamageModifierManager.getInstance().registerModifier(new TransfuzjaModifier(), EventPriority.HIGHEST);
		DamageModifierManager.getInstance().registerModifier(new ProjectileRuneEffectModifier(), EventPriority.HIGHEST);
		DamageModifierManager.getInstance().registerModifier(new EntityTauntModifier(), EventPriority.MONITOR);
		DamageModifierManager.getInstance().registerModifier(new ManekinDamage(), EventPriority.MONITOR);
		DamageModifierManager.getInstance().registerModifier(new ZewKrwiModifier(), EventPriority.MONITOR);
		DamageModifierManager.getInstance().registerModifier(new RytualKrwiModifier(), EventPriority.MONITOR);
		DamageModifierManager.getInstance().registerModifier(new HPInfo(), EventPriority.MONITOR);
		DamageModifierManager.getInstance().registerModifier(new NoDamageTicksModifier(), EventPriority.MONITOR);
		DamageModifierManager.getInstance().registerModifier(new BeeStingModifier(), EventPriority.MONITOR);
		
		//Calendar Events
		if(EpicRPGApi.getApi().getCalendarManager().isRegisteredEvent("reset_blackrock")) 
			EpicRPGApi.getApi().getCalendarManager().removeEvent("reset_blackrock");
		EpicRPGApi.getApi().getCalendarManager().addEvent("reset_blackrock", "every day", "00:05");
		
		addDisableDamageParticlesPacketListener();
	}
	
	private static void addDisableDamageParticlesPacketListener() {
		Main.getInstance().getProtocolManager().addPacketListener(new PacketAdapter(Main.getInstance(), ListenerPriority.HIGH, PacketType.Play.Server.WORLD_PARTICLES) {
			@Override
			public void onPacketSending(PacketEvent event) {
				PacketContainer packet = event.getPacket();
				if(event.getPacketType().equals(PacketType.Play.Server.WORLD_PARTICLES)) {
					if(packet.getNewParticles().read(0).getParticle().equals(Particle.DAMAGE_INDICATOR)) {
						packet.getIntegers().write(0, 0);
						event.setCancelled(true);
					}
				}
			}
		});
	}
	
}
