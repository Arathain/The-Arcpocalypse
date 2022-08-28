package arathain.arcpocalypse.common;

import net.minecraft.sound.SoundEvent;

import static arathain.arcpocalypse.common.ArcpocalypseSoundEvents.*;

public class ArcpocalypseSoundHandler {
	//Ambient Taunt Hurt Death Beam

	public static SoundEvent getAmbient(NekoArcComponent.TypeNeco neco) {
		return ENTITY_ARC_AMBIENT;
	}

	public static SoundEvent getTaunt(NekoArcComponent.TypeNeco neco) {
		return ENTITY_ARC_TAUNT;
	}

	public static SoundEvent getHurt(NekoArcComponent.TypeNeco neco) {
		return ENTITY_ARC_HURT;
	}

	public static SoundEvent getDeath(NekoArcComponent.TypeNeco neco) {
		return ENTITY_ARC_DEATH;
	}

	public static SoundEvent getBeam(NekoArcComponent.TypeNeco neco) {
		return ENTITY_ARC_AMBIENT;
	}
}
