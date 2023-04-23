package me.Vark123.EpicRPG.Chat;

import javax.inject.Singleton;

@Singleton
public class ChatManager {

	private static final ChatManager instance = new ChatManager();
	
	private boolean chatToggle;
	public final String MOD_COLOR;
	public final String PLAYER_COLOR;
	
	private ChatManager() {
		this.chatToggle = true;
		MOD_COLOR = "§x§a§d§f§f§2§f";
		PLAYER_COLOR = "§r";
	}

	public boolean isChatToggle() {
		return chatToggle;
	}

	public void setChatToggle(boolean chatToggle) {
		this.chatToggle = chatToggle;
	}

	public static ChatManager getInstance() {
		return instance;
	}
	
}
