package me.Vark123.EpicRPG.BoosterSystem.Commands;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Vark123.EpicRPG.Main;

public class BaseBoostCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("epicboost"))
			return false;
		if(!(sender.hasPermission("epicrpg.admin")))
			return false;
		if(args.length == 0) {
			showCorrectUsage(sender);
			return false;
		}
		
		MutableBoolean returnValue = new MutableBoolean(true);
		BoostCommandManager.get().getBoostSubcommand(args[0])
			.ifPresentOrElse(subcmd -> {
				if(!subcmd.canUse(sender)) {
					showCorrectUsage(sender);
					returnValue.setFalse();
					return;
				}
				if(args.length > 1) {
					String[] newArgs = new String[args.length - 1];
					for(int i = 0; i < newArgs.length; ++i)
						newArgs[i] = args[i+1];
					boolean res = subcmd.useCommand(sender, newArgs);
					if(!res)
						subcmd.showCorrectUsage(sender);
					returnValue.setValue(res);
				} else {
					boolean res = subcmd.useCommand(sender);
					if(!res)
						subcmd.showCorrectUsage(sender);
					returnValue.setValue(res);
				}
			}, () -> {
				showCorrectUsage(sender);
				returnValue.setFalse();
			});
		return returnValue.booleanValue();
	}

	private void showCorrectUsage(CommandSender sender) {
		sender.sendMessage("["+Main.getInstance().getPrefix()+"] Poprawne uzycie komendy /epicboost");
		BoostCommandManager.get().getBoostSubcommands().keySet().stream()
			.filter(key -> {
				ABoostCommand cmd = BoostCommandManager.get().getBoostSubcommand(key).get();
				return cmd.getCmd().equals(key)
						&& cmd.canUse(sender);
			}).forEach(key -> {
				BoostCommandManager.get().getBoostSubcommand(key).get().showCorrectUsage(sender);
			});
	}
	
}
