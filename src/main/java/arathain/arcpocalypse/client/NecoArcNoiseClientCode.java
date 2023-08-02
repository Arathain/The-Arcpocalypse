package arathain.arcpocalypse.client;

import arathain.arcpocalypse.ArcpocalypseComponents;
import arathain.arcpocalypse.ArcpocalypseNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class NecoArcNoiseClientCode {
    public static void packetProxy(PlayerEntity player, int lastAttackedTicks) {
        PacketByteBuf buf = PacketByteBufs.create();
        new ArcpocalypseNetworking.ArcSoundEmitterPacket(player.getUuid(), player.getPos(), (player.getAttacking() != null || player.getAttacker() != null || lastAttackedTicks < 100), player.getComponent(ArcpocalypseComponents.ARC_COMPONENT).getNecoType().toString().toLowerCase()).write(buf);
        ClientPlayNetworking.send(new Identifier("arcpocalypse", "burenya_packet_c2s"), buf);
    }
}
