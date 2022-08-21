package arathain.arcpocalypse.common;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

import static arathain.arcpocalypse.Arcpocalypse.MODID;

public class ArcpocalypseSoundEvents {
	private static final Map<SoundEvent, Identifier> SOUND_EVENTS = new LinkedHashMap<>();
	public static final SoundEvent ENTITY_ARC_AMBIENT = createSoundEvent("entity.nekoarcplayer.idle");
	public static final SoundEvent ENTITY_ARC_TAUNT = createSoundEvent("entity.nekoarcplayer.taunt");
	public static final SoundEvent ENTITY_ARC_HURT = createSoundEvent("entity.nekoarcplayer.hurt");
	public static final SoundEvent ENTITY_ARC_DEATH = createSoundEvent("entity.nekoarcplayer.death");
	public static final SoundEvent ENTITY_ARC_BEAM = createSoundEvent("entity.nekoarcplayer.beam");

	private static SoundEvent createSoundEvent(String name) {
		Identifier id = new Identifier(MODID, name);
		SoundEvent soundEvent = new SoundEvent(id);
		SOUND_EVENTS.put(soundEvent, id);
		return soundEvent;
	}
	public static void init() {
		SOUND_EVENTS.keySet().forEach(effect -> Registry.register(Registry.SOUND_EVENT, SOUND_EVENTS.get(effect), effect));
	}
}
