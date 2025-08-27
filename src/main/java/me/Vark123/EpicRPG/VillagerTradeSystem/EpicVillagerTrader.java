package me.Vark123.EpicRPG.VillagerTradeSystem;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class EpicVillagerTrader {

	private String id;
	private String display;
	
	private Collection<EpicTrade> trades;
	
}
