package me.Vark123.EpicRPG;

import org.bukkit.Bukkit;
import org.bukkit.Particle;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import me.Vark123.EpicRPG.API.EpicRPGApi;
import me.Vark123.EpicRPG.AdvancedBuySystem.AdvancedBuyListener;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockAddAllEvent;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockAddEvent;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockEntryEvent;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockHealDebuffEvent;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockRemoveAllEvent;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockRemoveEvent;
import me.Vark123.EpicRPG.BlackrockSystem.Events.BlackrockResetEvent;
import me.Vark123.EpicRPG.BoosterSystem.Listeners.BoosterModifyListener;
import me.Vark123.EpicRPG.Chat.ChatMsgSendEvent;
import me.Vark123.EpicRPG.Core.CPS.CPSClickListener;
import me.Vark123.EpicRPG.Core.Events.PlayerUseDisabledBlockEvent;
import me.Vark123.EpicRPG.Core.Events.PlayerUseLeverEvent;
import me.Vark123.EpicRPG.Core.Listeners.ExecutableItemUseListener;
import me.Vark123.EpicRPG.Core.Listeners.LevelSystemControlListener;
import me.Vark123.EpicRPG.Core.Listeners.PlayerJumpModifyListener;
import me.Vark123.EpicRPG.Core.Listeners.VipBoostControlListener;
import me.Vark123.EpicRPG.Dungeons.Listeners.KoszmarKrukaPotionDebuffListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.CustomProjectileDamageListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.EntityDamageListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.EntityDeathListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.FallDamageListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.FireworkdDamageListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.InvincibleControllerListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.LavaDamageListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.MagicDamageListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.MeleeDragonAttackListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.PlayerDeathListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.ProjectileLaunchListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.SelfShootProtectionListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.VoidDamageListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.WeaknessAttackListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Magic.PotionMagicModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Magic.ProfMagicModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Magic.RuneMagicModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Melee.PotionMeleeModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Melee.ProfMeleeModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Melee.RuneMeleeModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Melee.SlugaBeliaraModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Misc.CiosKrytycznyModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Misc.ProwokacjaModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Misc.ZadzaKrwiModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Misc.ZyciodajnaZiemiaModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Projectile.CrossbowModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Projectile.PotionProjectileModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Projectile.ProfProjectileModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Attack.Projectile.RuneProjectileModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Crits.CiosKrytycznyCritCalcListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Crits.PotionCritCalcListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Crits.ProfCritCalcListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Crits.StatsCritCalcListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Defense.PotionDefenseModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Defense.ProfDefenseModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Defense.ProwokacjaDefenseModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Defense.RuneDefenseModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Custom.BeeStingEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Custom.DollInfoEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Custom.EntityTauntEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Custom.HpDisplayEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Custom.ScaleDamageModifierEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Custom.ShulkerModifierEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Custom.VanillaPotionModifierEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Defense.LodowyBlokEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Defense.PvPListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Defense.TransfuzjaEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Magic.KlatwaKrwiEffectModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Magic.KrewPrzodkowEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Magic.OgnistaSferaEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Magic.PaktKrwiEffectModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Magic.RytualKrwiEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Magic.SilaJednosciEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Magic.TarczaCieniaEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Magic.TotemObronnyEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Magic.WedrownyCienEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Magic.ZewKrwiEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Melee.CienAssasynaEffectModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Melee.RytualWzniesieniaEffectModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Melee.ThreatTableModifyEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Melee.WampiryzmEffectModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Misc.AnubRekhanDamageEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Misc.ConditionalDebuffEffectModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Misc.CriticalEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Misc.DodgeEffectModifierListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Misc.DuchAkashyDamageEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Misc.EligorDamageEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Misc.EsAlareMeDamageEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Misc.KyraDamageEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Misc.TrujacaAuraEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Projectile.NoDamageTicksEffectListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Projectile.ProjectileEffectsListener;
import me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Projectile.SwietaStrzalaAreaEffectListener;
import me.Vark123.EpicRPG.Gems.GemPlaceProtEvent;
import me.Vark123.EpicRPG.HealthSystem.PlayerHealEvent;
import me.Vark123.EpicRPG.HorseSystem.HorseDismountEvent;
import me.Vark123.EpicRPG.HorseSystem.HorseInventoryEvent;
import me.Vark123.EpicRPG.HorseSystem.HorseProtectionListener;
import me.Vark123.EpicRPG.HorseSystem.HorseRemoveOnPlayerDeathEvent;
import me.Vark123.EpicRPG.HorseSystem.HorseStickUseEvent;
import me.Vark123.EpicRPG.Jewelry.JewelryMenuOpenEvent;
import me.Vark123.EpicRPG.Klejnoty.GrindstoneUseEvent;
import me.Vark123.EpicRPG.KosturSystem.KosturUseEvent;
import me.Vark123.EpicRPG.MMExtension.CustomConditionLoadEvent;
import me.Vark123.EpicRPG.MMExtension.CustomMechanicsLoadEvent;
import me.Vark123.EpicRPG.MMExtension.CustomTargeterLoadEvent;
import me.Vark123.EpicRPG.MMExtension.Misc.ProtectorDropKillEvent;
import me.Vark123.EpicRPG.Options.Listeners.CompassOptionRegistryListener;
import me.Vark123.EpicRPG.Options.Listeners.HorseOptionRegistryListener;
import me.Vark123.EpicRPG.Options.Listeners.MarkerRegistryListener;
import me.Vark123.EpicRPG.Options.Listeners.ResourceInfoRegistryListener;
import me.Vark123.EpicRPG.Options.Listeners.ScoreboardOptionRegistryListener;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerArrowWeaponUseEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerChangeEqEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerDropEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerJoinEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerQuitEvent;
import me.Vark123.EpicRPG.Players.BaseEvents.PlayerRespawnEvent;
import me.Vark123.EpicRPG.Players.Components.Compass.Listeners.PlayerCompassUpdateEvent;
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
import me.Vark123.EpicRPG.ScriptedFightsAndSkills.Loatheb.LoathebHealDebuffListener;
import me.Vark123.EpicRPG.ScriptedFightsAndSkills.Loatheb.LoathebProjectileNeutralizeListener;
import me.Vark123.EpicRPG.ScriptedFightsAndSkills.Loatheb.LoathebProjectileReflectListener;
import me.Vark123.EpicRPG.ScriptedFightsAndSkills.Loatheb.LoathebWaeponDebuffListener;
import me.Vark123.EpicRPG.Scrolls.EpicBossScrollEvent;
import me.Vark123.EpicRPG.Scrolls.Katedra2ScrollEvent;
import me.Vark123.EpicRPG.Scrolls.KatedraScrollEvent;
import me.Vark123.EpicRPG.Scrolls.KlasaResetScrollEvent;
import me.Vark123.EpicRPG.Scrolls.StatResetScrollEvent;
import me.Vark123.EpicRPG.UpgradableSystem.Listeners.InhibitorInventoryClickListener;
import me.Vark123.EpicRPG.UpgradableSystem.Listeners.InhibitorInventoryCloseListener;
import me.Vark123.EpicRPG.UpgradableSystem.Listeners.UpgradableAnvilUseListener;

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

//		Bukkit.getPluginManager().registerEvents(new ShootEvent(), inst);
//		Bukkit.getPluginManager().registerEvents(new RpgDamageEvent(), inst);
//		Bukkit.getPluginManager().registerEvents(new RpgDeathEvent(), inst);
//		Bukkit.getPluginManager().registerEvents(new DamageModifyEvent(), inst);
//		Bukkit.getPluginManager().registerEvents(new LavaDamageEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new InvincibleControllerListener(), inst);
		Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), inst);
		Bukkit.getPluginManager().registerEvents(new MagicDamageListener(), inst);
		Bukkit.getPluginManager().registerEvents(new CustomProjectileDamageListener(), inst);
		Bukkit.getPluginManager().registerEvents(new LavaDamageListener(), inst);
		Bukkit.getPluginManager().registerEvents(new FallDamageListener(), inst);
		Bukkit.getPluginManager().registerEvents(new FireworkdDamageListener(), inst);
		Bukkit.getPluginManager().registerEvents(new VoidDamageListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ProjectileLaunchListener(), inst);
		Bukkit.getPluginManager().registerEvents(new EntityDeathListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), inst);
		Bukkit.getPluginManager().registerEvents(new SelfShootProtectionListener(), inst);
		Bukkit.getPluginManager().registerEvents(new MeleeDragonAttackListener(), inst);
		Bukkit.getPluginManager().registerEvents(new WeaknessAttackListener(), inst);
		
		Bukkit.getPluginManager().registerEvents(new CiosKrytycznyCritCalcListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PotionCritCalcListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ProfCritCalcListener(), inst);
		Bukkit.getPluginManager().registerEvents(new StatsCritCalcListener(), inst);

		Bukkit.getPluginManager().registerEvents(new PvPListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PotionMagicModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ProfMagicModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new RuneMagicModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PotionMeleeModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ProfMeleeModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new RuneMeleeModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new SlugaBeliaraModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new CiosKrytycznyModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ProwokacjaModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ProwokacjaDefenseModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ZadzaKrwiModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ZyciodajnaZiemiaModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new CrossbowModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PotionProjectileModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ProfProjectileModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new RuneProjectileModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PotionDefenseModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ProwokacjaModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new RuneDefenseModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ProfDefenseModifierListener(), inst);

		Bukkit.getPluginManager().registerEvents(new BeeStingEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new DollInfoEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new EntityTauntEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new HpDisplayEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ScaleDamageModifierEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ShulkerModifierEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new VanillaPotionModifierEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new LodowyBlokEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new KrewPrzodkowEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new OgnistaSferaEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new RytualKrwiEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new SilaJednosciEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new TarczaCieniaEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new TotemObronnyEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new TransfuzjaEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new WedrownyCienEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ZewKrwiEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new CienAssasynaEffectModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new RytualWzniesieniaEffectModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new me.Vark123.EpicRPG.FightSystem.Listeners.Effects.Magic.RytualWzniesieniaEffectModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new WampiryzmEffectModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ConditionalDebuffEffectModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new CriticalEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new DodgeEffectModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new NoDamageTicksEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ProjectileEffectsListener(), inst);
		Bukkit.getPluginManager().registerEvents(new SwietaStrzalaAreaEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new KlatwaKrwiEffectModifierListener(), inst);
		Bukkit.getPluginManager().registerEvents(new TrujacaAuraEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ThreatTableModifyEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new AnubRekhanDamageEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new EligorDamageEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new DuchAkashyDamageEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new KyraDamageEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new EsAlareMeDamageEffectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PaktKrwiEffectModifierListener(), inst);

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
		Bukkit.getPluginManager().registerEvents(new Katedra2ScrollEvent(), inst);
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

		Bukkit.getPluginManager().registerEvents(new CompassOptionRegistryListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ScoreboardOptionRegistryListener(), inst);
		Bukkit.getPluginManager().registerEvents(new ResourceInfoRegistryListener(), inst);
		Bukkit.getPluginManager().registerEvents(new MarkerRegistryListener(), inst);
		Bukkit.getPluginManager().registerEvents(new HorseOptionRegistryListener(), inst);
		
		Bukkit.getPluginManager().registerEvents(new PlayerCompassUpdateEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new AdvancedBuyListener(), inst);
		Bukkit.getPluginManager().registerEvents(new GrindstoneUseEvent(), inst);

		Bukkit.getPluginManager().registerEvents(new LoathebHealDebuffListener(), inst);
		Bukkit.getPluginManager().registerEvents(new LoathebProjectileNeutralizeListener(), inst);
		Bukkit.getPluginManager().registerEvents(new LoathebProjectileReflectListener(), inst);
		Bukkit.getPluginManager().registerEvents(new LoathebWaeponDebuffListener(), inst);

		Bukkit.getPluginManager().registerEvents(new KoszmarKrukaPotionDebuffListener(), inst);
		Bukkit.getPluginManager().registerEvents(new CPSClickListener(), inst);
		Bukkit.getPluginManager().registerEvents(new HorseProtectionListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerJumpModifyListener(), inst);

		Bukkit.getPluginManager().registerEvents(new InhibitorInventoryClickListener(), inst);
		Bukkit.getPluginManager().registerEvents(new InhibitorInventoryCloseListener(), inst);
		Bukkit.getPluginManager().registerEvents(new UpgradableAnvilUseListener(), inst);
		
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
