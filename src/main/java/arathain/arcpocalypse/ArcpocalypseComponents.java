package arathain.arcpocalypse;

import arathain.arcpocalypse.common.NekoArcComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public class ArcpocalypseComponents implements EntityComponentInitializer {
	public static final ComponentKey<NekoArcComponent> ARC_COMPONENT = ComponentRegistry.getOrCreate(new Identifier(Arcpocalypse.MODID, "nekoarc"), NekoArcComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(ARC_COMPONENT, NekoArcComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
	}
}
