package me.Vark123.EpicRPG.BoosterSystem.Commands.Impl;

import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.command.CommandSender;

import me.Vark123.EpicRPG.BoosterSystem.BoosterManager;
import me.Vark123.EpicRPG.BoosterSystem.Commands.ABoostCommand;

public class RudaBoosterCommand extends ABoostCommand {

	public RudaBoosterCommand() {
		super("ruda", new String[] {});
	}

	@Override
	public boolean canUse(CommandSender sender) {
		return true;
	}

	@Override
	public boolean useCommand(CommandSender sender, String... args) {
		if(args.length < 3)
			return false;
		if(!NumberUtils.isCreatable(args[1]) || !NumberUtils.isCreatable(args[2]))
			return false;
		
		String player = args[0];
		double modifier = Double.parseDouble(args[1]);
		long time = Long.parseLong(args[2]) * 1000;
		
		BoosterManager.get().registerBooster(player, "ruda", "brylki rudy", modifier, time);
		return true;
	}

	@Override
	public void showCorrectUsage(CommandSender sender) {
		sender.sendMessage("  /epicboost ruda <player> <modifier> <seconds>");
	}

}
