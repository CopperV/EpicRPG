package me.Vark123.EpicRPG.Utils;

import lombok.Getter;

@Getter
public enum ComboClick {

	LEFT("L"),
	RIGHT("P");

	private String clickId;
	
	ComboClick(String clickId) {
		this.clickId = clickId;
	}
	
}
