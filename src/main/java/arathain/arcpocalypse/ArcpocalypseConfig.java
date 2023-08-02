package arathain.arcpocalypse;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.EnvType;
import net.minecraft.nbt.NbtCompound;
import org.quiltmc.loader.api.minecraft.MinecraftQuiltLoader;

public class ArcpocalypseConfig extends MidnightConfig {
	@Client
	@Entry(category = "quality_of_life", min=0, max=100) public static int burenya = 100;
	@Comment(category = "neco_abilities") public static Comment explainTheThing;
	@Entry(category = "neco_abilities") public static ArcAbilitySettings boomboom = ArcAbilitySettings.ENABLED;
	@Entry(category = "neco_abilities") public static ArcAbilitySettings lasertime = ArcAbilitySettings.ENABLED;


	private static NetworkSyncableConfig serverConfig = null;

	public static NetworkSyncableConfig getCurrentNetworkSyncableConfig() {
		if (serverConfig != null && MinecraftQuiltLoader.getEnvironmentType() == EnvType.CLIENT) {
			return serverConfig;
		}
		return new NetworkSyncableConfig(boomboom, lasertime);
	}


	public static void setServerConfig(NetworkSyncableConfig config) {
		serverConfig = config;
	}

	public enum ArcAbilitySettings {
		ENABLED("enabled"),
		WORLD_SAFE("world_safe"),
		FULL_SAFE("full_safe"),
		DISABLED("disabled");

		public final String id;
		ArcAbilitySettings(String id) {
			this.id = id;
		}

		public static ArcAbilitySettings parseSetting(String setting) {
			return switch (setting) {
				case ("enabled") -> ENABLED;
				case ("world_safe") -> WORLD_SAFE;
				case ("full_safe") -> FULL_SAFE;
				case ("disabled") -> DISABLED;
				default -> {
					Arcpocalypse.LOGGER.error("Setting could not be parsed, disabling it for now, please restart your game.");
					yield DISABLED;
				}
			};
		}
	}

	public record NetworkSyncableConfig(ArcAbilitySettings enableExplosions, ArcAbilitySettings enableLasers) {
		public NbtCompound compileNBT() {
			NbtCompound config = new NbtCompound();
			config.putString("enableExplosions", enableExplosions().id);
			config.putString("enableLasers", enableLasers().id);
			return config;
		}

		public static NetworkSyncableConfig fromConfig(NbtCompound configNBT) {
			ArcAbilitySettings enableExplosions = ArcAbilitySettings.parseSetting(configNBT.getString("enableExplosions"));
			ArcAbilitySettings enableLasers = ArcAbilitySettings.parseSetting(configNBT.getString("enableLasers"));
			return new NetworkSyncableConfig(enableExplosions, enableLasers);
		}
	}
}
