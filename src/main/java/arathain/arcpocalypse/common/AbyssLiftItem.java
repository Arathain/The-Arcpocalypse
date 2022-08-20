package arathain.arcpocalypse.common;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class AbyssLiftItem extends Item {
	public AbyssLiftItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if(context.getSide() == Direction.DOWN && !context.getWorld().isClient) {
			AbyssLiftEntity entity = new AbyssLiftEntity(context.getWorld());
			entity.setPosition(Vec3d.ofBottomCenter(context.getBlockPos()).add(0, -3, 0));
			entity.setTargetPos(context.getBlockPos());
			float yaw = context.getPlayer().getYaw() + 180;
			yaw += 45;
			yaw /= 90;
			int truYaw = (int) yaw;
			truYaw *= 90;
			entity.setYaw(truYaw);
			context.getWorld().spawnEntity(entity);
			context.getStack().decrement(1);
			return ActionResult.SUCCESS;
		}
		return super.useOnBlock(context);
	}
}
