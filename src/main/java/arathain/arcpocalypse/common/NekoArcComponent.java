package arathain.arcpocalypse.common;

import arathain.arcpocalypse.ArcpocalypseComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import virtuoel.pehkui.api.ScaleData;

public class NekoArcComponent implements AutoSyncedComponent {
	public static final float ARC_WIDTH = 0.6f / EntityType.PLAYER.getWidth();
	public static final float ARC_HEIGHT = 1.375f / EntityType.PLAYER.getHeight();
	private final PlayerEntity obj;
	private boolean arc = false;
	private boolean flying = false;

	public NekoArcComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	public boolean isArc() {
		return arc;
	}
	public static void scaleDown(PlayerEntity player) {
		ScaleData width = NekoArcScaleType.MODIFY_WIDTH_TYPE.getScaleData(player);
		ScaleData height = NekoArcScaleType.MODIFY_HEIGHT_TYPE.getScaleData(player);
		width.setScale(width.getBaseScale() * ARC_WIDTH);
		height.setScale(height.getBaseScale() * ARC_HEIGHT);
	}

	public void setArc(boolean arc) {
		boolean shouldSync = arc != this.arc;
		this.arc = arc;
		if(arc) {
			scaleDown(obj);
		} else {
			ScaleData width = NekoArcScaleType.MODIFY_WIDTH_TYPE.getScaleData(obj);
			ScaleData height = NekoArcScaleType.MODIFY_HEIGHT_TYPE.getScaleData(obj);
			width.setScale(1);
			height.setScale(1);
		}
		if(shouldSync)
			obj.syncComponent(ArcpocalypseComponents.ARC_COMPONENT);
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		arc = tag.getBoolean("nekoarcueidbrunestud");
		flying = tag.getBoolean("flying");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("nekoarcueidbrunestud", arc);
		tag.putBoolean("flying", flying);
	}
}
