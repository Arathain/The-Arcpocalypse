package arathain.arcpocalypse;

import net.fabricmc.api.EnvType;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.minecraft.MinecraftQuiltLoader;
import org.quiltmc.qsl.networking.api.client.*;

public class ArcpocalypseNetworking {
	public static Identifier CONFIG_SYNC_PACKET_ID = new Identifier("arcpocalypse", "config_sync");
	public static boolean onServer;
	//public static ArcpocalypseConfig.NetworkSyncableConfig SERVER_NETWORK_CONFIG = new ArcpocalypseConfig.NetworkSyncableConfig(ArcpocalypseConfig.ArcAbilitySettings.DISABLED, ArcpocalypseConfig.ArcAbilitySettings.DISABLED);

	public static void init() {
		if (MinecraftQuiltLoader.getEnvironmentType() == EnvType.CLIENT) {
			ClientPlayNetworking.registerGlobalReceiver(CONFIG_SYNC_PACKET_ID, (client, handler, buf, responseSender) -> {
				client.execute(() -> ArcpocalypseConfig.setServerConfig(ArcpocalypseConfig.NetworkSyncableConfig.fromConfig(buf.readNbt())));
			});
			ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> client.execute(() -> {
				ArcpocalypseConfig.setServerConfig(null);
			}));
		}
	}
}
