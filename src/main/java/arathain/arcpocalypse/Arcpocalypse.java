package arathain.arcpocalypse;

import arathain.arcpocalypse.common.*;
import dev.emi.emi.EmiCommands;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.function.Predicate;

public class Arcpocalypse implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Arcpocalypse");
	public static final String MODID = "arcpocalypse";
	public static final Color BURUNYUU_LASER = new Color(100, 10, 30);
	public static final boolean DOES_THE_ITEM_MAKE_YOU_TRANSFORM_ALSO_THIS_WILL_BE_IN_A_CONFIG_MAYBE = false; // Decide if you want a config or if you want this to be devs only

	@Override
	public void onInitialize(ModContainer mod) {
		NekoArcScaleType.init();
		ArcpocalypseEntities.init();
		ArcpocalypseItems.init();
		ArcpocalypseSoundEvents.init();
		CommandRegistrationCallback.EVENT.register(((dispatcher, buildContext, environment) -> Necommand.registerNeco(dispatcher)));
		CommandRegistrationCallback.EVENT.register(((dispatcher, buildContext, environment) -> Necommand.registerUnneco(dispatcher)));
		EntityElytraEvents.CUSTOM.register((entity, tickElytra) -> entity instanceof PlayerEntity && entity.getComponent(ArcpocalypseComponents.ARC_COMPONENT).isArc());

	}
	public static EntityHitResult hitscanEntity(World world, LivingEntity user, double distance, Predicate<Entity> targetPredicate){
		Vec3d vec3d = user.getCameraPosVec(1);
		Vec3d vec3d2 = user.getRotationVec(1);
		Vec3d vec3d3 = vec3d.add(vec3d2.x * distance, vec3d2.y * distance, vec3d2.z * distance);
		double squareDistance = Math.pow(distance, 2);
		return ProjectileUtil.getEntityCollision(world, user, vec3d, vec3d3, user.getBoundingBox().stretch(vec3d2.multiply(squareDistance)).expand(1.0D, 1.0D, 1.0D), targetPredicate);
	}
	public static BlockHitResult hitscanBlock(World world, LivingEntity user, double distance, RaycastContext.FluidHandling fluidHandling, Predicate<Block> targetPredicate){
		Vec3d vec3d = user.getCameraPosVec(1);
		Vec3d vec3d2 = user.getRotationVec(1);
		Vec3d vec3d3 = vec3d.add(vec3d2.x * distance, vec3d2.y * distance, vec3d2.z * distance);
		double squareDistance = Math.pow(distance, 2);
		vec3d3.multiply(squareDistance);
		return world.raycast(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.OUTLINE, fluidHandling, user));
	}
}
