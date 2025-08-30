package me.Vark123.EpicRPG.RuneSystem.SummonSystem;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import me.Vark123.EpicComponentAPI.EpicComponent;
import me.Vark123.EpicRPG.Main;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.Commands.AttackFurthestCommand;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.Commands.AttackHighestHpCommand;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.Commands.AttackHighestPercentageHpCommand;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.Commands.AttackLowestHpCommand;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.Commands.AttackLowestPercentageHpCommand;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.Commands.AttackNearestCommand;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.Commands.AttackRandomCommand;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.Commands.BreakThroughCommand;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.Commands.DefaultCommand;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.Commands.DismissCommand;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.Commands.GatheringCommand;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.Commands.PassiveCommand;
import me.Vark123.EpicRPG.RuneSystem.SummonSystem.Commands.ResetTargetCommand;
import me.Vark123.EpicRPG.Utils.ComboClick;

@Getter
public final class SummonControllerManager {

	private static final SummonControllerManager inst = new SummonControllerManager();

	private final long clickCooldown = 100l;
	private final long maxCastTime = 3000l;
	
	private final Map<String, ASummonCommand> commands = new LinkedHashMap<>();
	
	private final Map<UUID, ComboController> currentCombos = new ConcurrentHashMap<>();
	
	private SummonControllerManager() {
		registerCommand(new PassiveCommand());
		registerCommand(new DismissCommand());
		registerCommand(new DefaultCommand());
		registerCommand(new AttackRandomCommand());
		registerCommand(new AttackNearestCommand());
		registerCommand(new AttackFurthestCommand());
		registerCommand(new AttackLowestHpCommand());
		registerCommand(new AttackHighestHpCommand());
		registerCommand(new AttackLowestPercentageHpCommand());
		registerCommand(new AttackHighestPercentageHpCommand());
		registerCommand(new GatheringCommand());
		registerCommand(new BreakThroughCommand());
		registerCommand(new ResetTargetCommand());
	}
	
	public static final SummonControllerManager getInst() {
		return inst;
	}
	
	public void registerCommand(ASummonCommand cmd) {
		commands.put(cmd.getCommandId(), cmd);
	}
	
	public Optional<ASummonCommand> getCommand(String id) {
		return Optional.ofNullable(commands.get(id));
	}
	
	public boolean isInCombo(Player caster, ItemStack controlItem) {
		UUID casterUID = caster.getUniqueId();
		return currentCombos.containsKey(casterUID) 
				&& currentCombos.get(casterUID).getItemController().equals(controlItem)
				&& (System.currentTimeMillis() - currentCombos.get(casterUID).getLastUse()) <= maxCastTime;
	}
	
	public boolean isInClickCooldown(Player caster, ItemStack controlItem) {
		UUID casterUID = caster.getUniqueId();
		if(!isInCombo(caster, controlItem))
			return false;
		
		ComboController combo = currentCombos.get(casterUID);
		return (System.currentTimeMillis() - combo.lastUse) < clickCooldown;
	}
	
	public boolean updateCombo(Player caster, ItemStack controlItem, ComboClick click) {
		UUID casterUID = caster.getUniqueId();
		ComboController currentCombo;
		if(!isInCombo(caster, controlItem)) {
			if(click.equals(ComboClick.LEFT))
				return false;
			
			currentCombo = new ComboController(caster, controlItem, click);
			currentCombos.put(casterUID, currentCombo);
		} else {
			currentCombo = currentCombos.get(casterUID);
			currentCombo.getCombo().append(click.getClickId());
		}
		
		currentCombo.lastUse = System.currentTimeMillis();
		
		StringBuilder comboTitle = new StringBuilder("*-*-*");
		String combo = currentCombo.combo.toString();
		for(int i = 0; i < combo.length(); ++i) {
			char comboClick = combo.charAt(i);
			comboTitle.setCharAt(i * 2, comboClick);
		}
		
		String title = comboTitle.toString();
		float pitch = 0.6f + 0.15f * (combo.length() - 1);
		
		caster.playSound(caster.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8f, pitch);
		caster.sendTitle("§b§l"+title, null, 10, 40, 10);
		
		return combo.length() >= 3;
	}
	
	public void finishCombo(Player caster, ItemStack controlItem) {
		UUID casterUID = caster.getUniqueId();
		if(!isInCombo(caster, controlItem))
			return;
		
		var currentCombo = currentCombos.get(casterUID);
		String combo = currentCombo.combo.toString().toLowerCase();
		
		currentCombos.remove(casterUID);
		
		EpicComponent comp = new EpicComponent(controlItem, MythicBukkit.inst());
		if(!comp.hasKey(combo)) {
			sendDenyMessage(caster, "§cNie masz przypisanej komendy pod kombinacja §4§o"+combo.toUpperCase());
			return;
		}
		
		String commandId = comp.getString(combo);
		if(!commands.containsKey(commandId)) {
			sendDenyMessage(caster, "§4Nieprawidlowa komenda! Zglos to §6§ladministratorowi§4!");
			return;
		}
		
		ASummonCommand command = commands.get(commandId);
		SummonManager.get().applyCommand(caster, command);
	}
	
	private void sendDenyMessage(Player caster, String message) {
		caster.playSound(caster, Sound.ENTITY_PIGLIN_AMBIENT, 1f, 0.9f);
		caster.getWorld().spawnParticle(Particle.SMOKE, caster.getLocation().clone().add(0,1,0), 15, 0.3, 0.9, 0.3, 0.03);
		caster.sendMessage(Main.getInstance().getPrefix()+" "+message);
	}
	
	@Getter
	private class ComboController {
		private StringBuilder combo;
		
		private Player player;
		private ItemStack itemController;
		
		private long lastUse = System.currentTimeMillis();

		public ComboController(Player player, ItemStack itemController, ComboClick click) {
			super();
			this.player = player;
			this.itemController = itemController;
			this.combo = new StringBuilder(click.getClickId());
		}
		
	}
	
}
