package arathain.arcpocalypse.common;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

import static arathain.arcpocalypse.Arcpocalypse.MODID;

public class ArcpocalypseEntities {
	private static final Map<Identifier, EntityType<?>> ENTITY_TYPES = new LinkedHashMap<>();
	// entities
	public static final EntityType<AbyssLiftEntity> ABYSS_LIFT = register("abyss_lift", EntityType.Builder.<AbyssLiftEntity>create((e, w)-> new AbyssLiftEntity(w), SpawnGroup.MISC).setDimensions(0.995F, 2.3F).build("arcpocalypse$abyss_lift"));
	public static <T extends Entity> EntityType<T> register(String id, EntityType<T> type) {
		ENTITY_TYPES.put(new Identifier(MODID, id), type);
		return type;
	}

	private static <T extends LivingEntity> EntityType<T> createEntity(String name, DefaultAttributeContainer.Builder attributes, EntityType<T> type) {
		FabricDefaultAttributeRegistry.register(type, attributes);
		ENTITY_TYPES.put(new Identifier(MODID, name), type);
		return type;
	}

	public static void init() {
		ENTITY_TYPES.forEach((id, entityType) -> Registry.register(Registry.ENTITY_TYPE, id, entityType));
	}
}
