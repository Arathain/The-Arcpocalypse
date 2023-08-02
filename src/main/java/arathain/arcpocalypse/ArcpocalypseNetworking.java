package arathain.arcpocalypse;

import arathain.arcpocalypse.common.ArcpocalypseSoundEvents;
import arathain.arcpocalypse.common.NekoArcComponent;
import net.fabricmc.api.EnvType;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.quiltmc.loader.api.minecraft.MinecraftQuiltLoader;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.ServerPlayConnectionEvents;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.quiltmc.qsl.networking.api.client.*;
import team.lodestar.lodestone.helpers.NBTHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArcpocalypseNetworking {
	public static Identifier S2C_CONFIG_SYNC_PACKET = new Identifier("arcpocalypse", "s2c_config_sync");

	public static void init() {
		if (MinecraftQuiltLoader.getEnvironmentType() == EnvType.CLIENT) {
			clientSync();
		}

		serverSync();
	}

	public static void clientSync() {
		ClientPlayNetworking.registerGlobalReceiver(S2C_CONFIG_SYNC_PACKET, (client, handler, buf, responseSender) -> {
			NbtCompound nbt = buf.readNbt();
			client.execute(() -> ArcpocalypseConfig.setServerConfig(ArcpocalypseConfig.NetworkSyncableConfig.fromConfig(nbt)));
		});
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> client.execute(() -> {
			ArcpocalypseConfig.setServerConfig(null);
		}));

		ClientPlayNetworking.registerGlobalReceiver(new Identifier("arcpocalypse", "burenya_packet_s2c"), (client, handler, buf, responseSender) -> {
			ArcSoundEmitterPacket thePacket = ArcSoundEmitterPacket.read(buf);

			client.execute(() -> {
				client.world.playSound(
					thePacket.pos.getX(),
					thePacket.pos.getY(),
					thePacket.pos.getZ(),
					thePacket.isTaunting ? ArcpocalypseSoundEvents.getNecoTaunt(NekoArcComponent.TypeNeco.getNecoFromString(thePacket.arcType)) : ArcpocalypseSoundEvents.getNecoAmbient(NekoArcComponent.TypeNeco.getNecoFromString(thePacket.arcType)),
					SoundCategory.PLAYERS,
					((float)ArcpocalypseConfig.burenya)/100f,
					1f,
					false);
			});
		});
	}

	public static void serverSync() {
		// S2C Sync Event
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayNetworking.send(handler.player, S2C_CONFIG_SYNC_PACKET, PacketByteBufs.create().writeNbt(ArcpocalypseConfig.getCurrentNetworkSyncableConfig().compileNBT()));
		});

		// It needs to go through server packet weirdness
		ServerPlayNetworking.registerGlobalReceiver(new Identifier("arcpocalypse", "burenya_packet_c2s"), ((server, player, handler, buf, responseSender) -> {
			PacketByteBuf realBufEnergy = PacketByteBufs.copy(buf);
			server.execute(() -> ServerPlayNetworking.send(server.getOverworld().getPlayers(), new Identifier("arcpocalypse", "burenya_packet_s2c"), realBufEnergy));
		}));
	 }

	// Based on: https://github.com/Sweet-Berry-Collective/foxbox/blob/1.20/src/main/java/dev/sweetberry/foxbox/FoxBoxNetworking.java. Used with express permission from the author.
	public static class ArcSoundEmitterPacket {
		public UUID player;
		public Vec3d pos;
		public boolean isTaunting;

		public String arcType;

		public ArcSoundEmitterPacket(UUID player, Vec3d pos, boolean isTaunting, String arcType) {
			this.player = player;
			this.pos = pos;
			this.isTaunting = isTaunting;
			this.arcType = arcType;
		}

		public ArcSoundEmitterPacket(PlayerEntity player, Vec3d pos, boolean isTaunting, String arcType) {
			this(player.getUuid(), pos, isTaunting, arcType);
		}

		public void write(PacketByteBuf buf) {
			buf.writeUuid(player);
			buf.writeDouble(pos.x);
			buf.writeDouble(pos.y);
			buf.writeDouble(pos.z);
			buf.writeBoolean(isTaunting);
			buf.writeString(arcType);
		}

		public static ArcSoundEmitterPacket read(PacketByteBuf buf) {
			UUID player = buf.readUuid();
			Vec3d pos = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
			boolean isTaunting = buf.readBoolean();
			String arcType = buf.readString();
			return new ArcSoundEmitterPacket(player, pos, isTaunting, arcType);
		}
	}
}
