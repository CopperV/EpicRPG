package me.Vark123.EpicRPG.Chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;
import me.clip.placeholderapi.PlaceholderAPI;

public class ChatMsgSendEvent implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(e.isCancelled())
			return;
		String msg = e.getMessage();
		if(msg.contains("${")) {
			e.setCancelled(true);
			return;
		}
		
		Player p = e.getPlayer();
		if(!PlayerManager.getInstance().playerExists(p))
			return;
		
		if(!ChatManager.getInstance().isChatToggle() && !p.hasPermission("rpg.chatoff")){
			p.sendMessage("§6==========");
			p.sendMessage("§cChat jest wylaczony. Nie mozesz na nim pisac!");
			p.sendMessage("§6==========");
			e.setCancelled(true);
			return;
		}
		
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgPlayerInfo info = rpg.getInfo();
		String color = p.hasPermission("rpg.mod") ? ChatManager.getInstance().MOD_COLOR : ChatManager.getInstance().PLAYER_COLOR;
		
		StringBuilder sb = new StringBuilder("§8[§e§l" + info.getLevel() + "§8] [" + info.getShortProf() + "§8]");
		String placeholder = PlaceholderAPI.setPlaceholders(p, "%epicclans_name%");
		if(placeholder != null && !placeholder.isBlank())
			sb.append(" §8["+placeholder+"§8]");
		sb.append(" §r%s§7: "+color+"%s");
		
		e.setFormat(sb.toString());
		
	}
	
}
