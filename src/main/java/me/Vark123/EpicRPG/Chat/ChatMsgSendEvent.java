package me.Vark123.EpicRPG.Chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.api.pafplayers.PAFPlayerManager;
import de.simonsator.partyandfriends.clan.api.Clan;
import de.simonsator.partyandfriends.clan.api.ClansManager;
import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgPlayerInfo;

public class ChatMsgSendEvent implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		String msg = e.getMessage();
		Bukkit.broadcastMessage(msg);
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
		OnlinePAFPlayer pafPlayer = PAFPlayerManager.getInstance().getPlayer(p);
		Clan klan = ClansManager.getInstance().getClan(pafPlayer);
		String color = p.hasPermission("rpg.mod") ? ChatManager.getInstance().MOD_COLOR : ChatManager.getInstance().PLAYER_COLOR;
		
		StringBuilder sb = new StringBuilder("§8[§e§l" + info.getLevel() + "§8] [" + info.getShortProf() + "§8]");
		if(klan != null)
			sb.append(" [§9"+klan.getColoredClanTag()+"§8]");
		sb.append(" §r%s§7: "+color+"%s");
		
		e.setFormat(sb.toString());
		
	}
	
}
