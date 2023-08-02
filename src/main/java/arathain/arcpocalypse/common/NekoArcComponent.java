package arathain.arcpocalypse.common;

import arathain.arcpocalypse.Arcpocalypse;
import arathain.arcpocalypse.ArcpocalypseComponents;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import virtuoel.pehkui.api.ScaleData;

import java.util.Objects;

public class NekoArcComponent implements AutoSyncedComponent {
	public static final float ARC_WIDTH = 0.6f / EntityType.PLAYER.getWidth();
	public static final float ARC_HEIGHT = 1.375f / EntityType.PLAYER.getHeight();
	private final PlayerEntity obj;
	private boolean arc = false;
	private boolean flying = false;

	private TypeNeco necoType = TypeNeco.ARC;

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
		boolean formerArcButMaybeItWorksProperlyPlease = this.arc;
		this.arc = arc;
		if(arc && !formerArcButMaybeItWorksProperlyPlease) {
			scaleDown(obj);
		} else if (!arc) {
			ScaleData width = NekoArcScaleType.MODIFY_WIDTH_TYPE.getScaleData(obj);
			ScaleData height = NekoArcScaleType.MODIFY_HEIGHT_TYPE.getScaleData(obj);
			width.setScale(1);
			height.setScale(1);
		}
		if (shouldSync)
			obj.syncComponent(ArcpocalypseComponents.ARC_COMPONENT);
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		this.necoType = TypeNeco.getNecoFromString(tag.getString("neco"));
		arc = tag.getBoolean("nekoarcueidbrunestud");
		flying = tag.getBoolean("flying");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putString("neco", necoType.toString().toLowerCase());
		tag.putBoolean("nekoarcueidbrunestud", arc);
		tag.putBoolean("flying", flying);
	}

	public TypeNeco getNecoType() {
		return necoType;
	}

	public void setNecoType(TypeNeco neco) {
		this.necoType = neco;
		obj.syncComponent(ArcpocalypseComponents.ARC_COMPONENT);
	}

	public enum TypeNeco {
		ARC("neko_arc", false),
		CIEL("neco_ciel", false),
		AKIHA("neco_akiha",  false),
		HISUI("neco_hisui",  true),
		KOHAKU("neco_kohaku",  true);

		public final String textureName;
		public final boolean maidModel;

		TypeNeco(String textureName, boolean maidModel) {
			this.textureName = textureName;
			this.maidModel = maidModel;
		}

		public static TypeNeco getNecoFromString(String string) {
			return switch (string) {
				case "ciel" -> CIEL;
				case "akiha" -> AKIHA;
				case "hisui" -> HISUI;
				case "kohaku" -> KOHAKU;
				default -> ARC;
			};
		}

		public String toString() {
			return super.toString();
		}
	}
}
