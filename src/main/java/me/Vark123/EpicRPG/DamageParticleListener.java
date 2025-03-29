package me.Vark123.EpicRPG;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerParticle;

public class DamageParticleListener implements PacketListener {

	
	
	@Override
	public void onPacketSend(PacketSendEvent event) {
		if(!event.getPacketType().equals(PacketType.Play.Server.PARTICLE))
			return;
		
		WrapperPlayServerParticle particle = new WrapperPlayServerParticle(event);
		if(particle.getParticle().getType().equals(ParticleTypes.DAMAGE_INDICATOR) && particle.getParticleCount() > 25) {
			particle.setParticleCount(25);
		}
	}
	
}
