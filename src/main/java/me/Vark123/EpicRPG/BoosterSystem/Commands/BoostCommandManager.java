package me.Vark123.EpicRPG.BoosterSystem.Commands;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;

@Getter
public class BoostCommandManager {

	private static final BoostCommandManager inst = new BoostCommandManager();
	
	private final Map<String, ABoostCommand> boostSubcommands;
	
	private BoostCommandManager() {
		boostSubcommands = new LinkedHashMap<>();
	}
	
	public static final BoostCommandManager get() {
		return inst;
	}
	
	public void registerSubcommand(ABoostCommand subcmd) {
		boostSubcommands.put(subcmd.getCmd().toLowerCase(), subcmd);
		for(String alias : subcmd.getAliases())
			boostSubcommands.put(alias, subcmd);
	}
	
	public Optional<ABoostCommand> getBoostSubcommand(String subcmd) {
		return Optional.ofNullable(boostSubcommands.get(subcmd.toLowerCase()));
	}
	
}
