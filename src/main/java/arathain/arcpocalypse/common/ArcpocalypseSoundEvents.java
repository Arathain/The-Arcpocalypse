package arathain.arcpocalypse.common;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import java.util.LinkedHashMap;
import java.util.Map;

import static arathain.arcpocalypse.Arcpocalypse.MODID;

public class ArcpocalypseSoundEvents {
	private static final Map<SoundEvent, Identifier> SOUND_EVENTS = new LinkedHashMap<>();
	public static final SoundEvent ENTITY_NECO_BEAM = createSoundEvent("entity.neco.beam");

	public static final SoundEvent ENTITY_ARC_AMBIENT = createSoundEvent("entity.neco.arc.idle");
	public static final SoundEvent ENTITY_ARC_TAUNT = createSoundEvent("entity.neco.arc.taunt");
	public static final SoundEvent ENTITY_ARC_HURT = createSoundEvent("entity.neco.arc.hurt");
	public static final SoundEvent ENTITY_ARC_DEATH = createSoundEvent("entity.neco.arc.death");

	public static final SoundEvent ENTITY_CIEL_AMBIENT = createSoundEvent("entity.neco.ciel.idle");
	public static final SoundEvent ENTITY_CIEL_TAUNT = createSoundEvent("entity.neco.ciel.taunt");
	public static final SoundEvent ENTITY_CIEL_HURT = createSoundEvent("entity.neco.ciel.hurt");
	public static final SoundEvent ENTITY_CIEL_DEATH = createSoundEvent("entity.neco.ciel.death");

	public static final SoundEvent ENTITY_AKIHA_AMBIENT = createSoundEvent("entity.neco.akiha.idle");
	public static final SoundEvent ENTITY_AKIHA_TAUNT = createSoundEvent("entity.neco.akiha.taunt");
	public static final SoundEvent ENTITY_AKIHA_HURT = createSoundEvent("entity.neco.akiha.hurt");
	public static final SoundEvent ENTITY_AKIHA_DEATH = createSoundEvent("entity.neco.akiha.death");

	public static final SoundEvent ENTITY_HISUI_AMBIENT = createSoundEvent("entity.neco.hisui.idle");
	public static final SoundEvent ENTITY_HISUI_TAUNT = createSoundEvent("entity.neco.hisui.taunt");
	public static final SoundEvent ENTITY_HISUI_HURT = createSoundEvent("entity.neco.hisui.hurt");
	public static final SoundEvent ENTITY_HISUI_DEATH = createSoundEvent("entity.neco.hisui.death");

	public static final SoundEvent ENTITY_KOHAKU_AMBIENT = createSoundEvent("entity.neco.kohaku.idle");
	public static final SoundEvent ENTITY_KOHAKU_TAUNT = createSoundEvent("entity.neco.kohaku.taunt");
	public static final SoundEvent ENTITY_KOHAKU_HURT = createSoundEvent("entity.neco.kohaku.hurt");
	public static final SoundEvent ENTITY_KOHAKU_DEATH = createSoundEvent("entity.neco.kohaku.death");

	private static SoundEvent createSoundEvent(String name) {
		Identifier id = new Identifier(MODID, name);
		SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(id);
		SOUND_EVENTS.put(soundEvent, id);
		return soundEvent;
	}

	public static SoundEvent getNecoAmbient(NekoArcComponent.TypeNeco neco) {
		return switch (neco) {
			case CIEL -> ENTITY_CIEL_AMBIENT;
			case AKIHA -> ENTITY_AKIHA_AMBIENT;
			case HISUI -> ENTITY_HISUI_AMBIENT;
			case KOHAKU -> ENTITY_KOHAKU_AMBIENT;
			default -> ENTITY_ARC_AMBIENT;
		};
	}

	public static SoundEvent getNecoTaunt(NekoArcComponent.TypeNeco neco) {
		return switch (neco) {
			case CIEL -> ENTITY_CIEL_TAUNT;
			case AKIHA -> ENTITY_AKIHA_TAUNT;
			case HISUI -> ENTITY_HISUI_TAUNT;
			case KOHAKU -> ENTITY_KOHAKU_TAUNT;
			default -> ENTITY_ARC_TAUNT;
		};
	}

	public static SoundEvent getNecoHurt(NekoArcComponent.TypeNeco neco) {
		return switch (neco) {
			case CIEL -> ENTITY_CIEL_HURT;
			case AKIHA -> ENTITY_AKIHA_HURT;
			case HISUI -> ENTITY_HISUI_HURT;
			case KOHAKU -> ENTITY_KOHAKU_HURT;
			default -> ENTITY_ARC_HURT;
		};
	}

	public static SoundEvent getNecoDeath(NekoArcComponent.TypeNeco neco) {
		return switch (neco) {
			case CIEL -> ENTITY_CIEL_DEATH;
			case AKIHA -> ENTITY_AKIHA_DEATH;
			case HISUI -> ENTITY_HISUI_DEATH;
			case KOHAKU -> ENTITY_KOHAKU_DEATH;
			default -> ENTITY_ARC_DEATH;
		};
	}

	public static void init() {
		SOUND_EVENTS.keySet().forEach(effect -> Registry.register(Registries.SOUND_EVENT, SOUND_EVENTS.get(effect), effect));
	}
}
