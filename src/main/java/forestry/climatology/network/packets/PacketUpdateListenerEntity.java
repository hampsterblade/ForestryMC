package forestry.climatology.network.packets;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import forestry.api.climate.IClimateState;
import forestry.api.climatology.ClimateCapabilities;
import forestry.api.climatology.IClimateListener;
import forestry.core.network.ForestryPacket;
import forestry.core.network.IForestryPacketClient;
import forestry.core.network.IForestryPacketHandlerClient;
import forestry.core.network.PacketBufferForestry;
import forestry.core.network.PacketIdClient;

public class PacketUpdateListenerEntity extends ForestryPacket implements IForestryPacketClient {
	private final Entity entity;
	private final IClimateState state;

	public PacketUpdateListenerEntity(Entity entity, IClimateState state) {
		this.entity = entity;
		this.state = state;
	}

	@Override
	protected void writeData(PacketBufferForestry data) throws IOException {
		data.writeEntityById(entity);
		data.writeClimateState(state);
	}

	@Override
	public PacketIdClient getPacketId() {
		return PacketIdClient.UPDATE_LISTENER;
	}

	@SideOnly(Side.CLIENT)
	public static class Handler implements IForestryPacketHandlerClient {
		@Override
		public void onPacketData(PacketBufferForestry data, EntityPlayer player) throws IOException {
			Entity entity = data.readEntityById(player.world);
			IClimateState state = data.readClimateState();
			if (entity != null && entity.hasCapability(ClimateCapabilities.CLIMATE_LISTENER, null)) {
				IClimateListener listener = entity.getCapability(ClimateCapabilities.CLIMATE_LISTENER, null);
				if (listener != null) {
					listener.setClientState(state);
				}
			}
		}
	}
}
