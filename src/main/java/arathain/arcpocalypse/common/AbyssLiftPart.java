package arathain.arcpocalypse.common;

import net.minecraft.util.math.Vec3d;
import org.quiltmc.qsl.entity.multipart.api.AbstractEntityPart;

public class AbyssLiftPart extends AbstractEntityPart<AbyssLiftEntity> {
	public AbyssLiftPart(AbyssLiftEntity owner, float width, float height) {
		super(owner, width, height);
		this.setRelativePosition(Vec3d.ZERO);
	}

	@Override
	public boolean collides() {
		return true;
	}

	@Override
	public boolean isCollidable() {
		return true;
	}
}
