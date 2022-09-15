package arathain.arcpocalypse.common;

import arathain.arcpocalypse.ArcpocalypseComponents;
import carpet.commands.PlayerCommand;
import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.*;
import net.minecraft.server.dedicated.command.BanCommand;
import net.minecraft.text.Text;
import org.quiltmc.qsl.command.api.EnumArgumentType;

import java.util.Collection;
import java.util.Iterator;

public class Necommand {
	public Necommand() {
	}

	@SuppressWarnings("unchecked")
	public static void registerNeco(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder) CommandManager.literal("neco").requires((source) -> source.hasPermissionLevel(2))).executes((context) -> executeNeco((ServerCommandSource)context.getSource(), ImmutableList.of(((ServerCommandSource)context.getSource()).getEntityOrThrow()), NekoArcComponent.TypeNeco.ARC)))
				.then(CommandManager.argument("targets", EntityArgumentType.entities()).executes((context) -> executeNeco((ServerCommandSource)context.getSource(), EntityArgumentType.getEntities(context, "targets"), NekoArcComponent.TypeNeco.ARC)))
				.then(CommandManager.argument("targets", EntityArgumentType.entities()).then(CommandManager.argument("neco_type", EnumArgumentType.enumConstant(NekoArcComponent.TypeNeco.class))
						.executes((context) -> executeNeco((ServerCommandSource) context.getSource(), EntityArgumentType.getEntities(context, "targets"), (NekoArcComponent.TypeNeco.getNecoFromString(EnumArgumentType.getEnum(context, "neco_type"))))))));
	} //EnumArgumentType.enumConstant(NekoArcComponent.TypeNeco.class)
	@SuppressWarnings("unchecked")
	public static void registerUnneco(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder) CommandManager.literal("deneco").requires((source) -> {
			return source.hasPermissionLevel(2);
		})).executes((context) -> {
			return executeUnneco((ServerCommandSource)context.getSource(), ImmutableList.of(((ServerCommandSource)context.getSource()).getEntityOrThrow()));
		})).then(CommandManager.argument("targets", EntityArgumentType.entities()).executes((context) -> {
			return executeUnneco((ServerCommandSource)context.getSource(), EntityArgumentType.getEntities(context, "targets"));
		})));
	}


	private static int executeNeco(ServerCommandSource source, Collection<? extends Entity> targets, NekoArcComponent.TypeNeco neco) {
		Iterator var2 = targets.iterator();
		boolean bl = false;

		while(var2.hasNext()) {
			Entity entity = (Entity)var2.next();
			if(entity instanceof PlayerEntity p) {
				p.getComponent(ArcpocalypseComponents.ARC_COMPONENT).setArc(true);
				p.getComponent(ArcpocalypseComponents.ARC_COMPONENT).setNecoType(neco);
				bl = true;
			}
		}
		if(bl) {
			if (targets.size() == 1) {
				source.sendFeedback(Text.translatable("commands.neco.success.single", new Object[]{((Entity) targets.iterator().next()).getDisplayName()}), true);
			} else {
				source.sendFeedback(Text.translatable("commands.neco.success.multiple", new Object[]{targets.size()}), true);
			}
		}

		return targets.size();
	}
	private static int executeUnneco(ServerCommandSource source, Collection<? extends Entity> targets) {
		Iterator var2 = targets.iterator();
		boolean bl = false;

		while(var2.hasNext()) {
			Entity entity = (Entity)var2.next();
			if(entity instanceof PlayerEntity p) {
				p.getComponent(ArcpocalypseComponents.ARC_COMPONENT).setArc(false);
				p.getComponent(ArcpocalypseComponents.ARC_COMPONENT).setNecoType(NekoArcComponent.TypeNeco.ARC);
				bl = true;
			}
		}
		if(bl) {
			if (targets.size() == 1) {
				source.sendFeedback(Text.translatable("commands.deneco.success.single", new Object[]{((Entity) targets.iterator().next()).getDisplayName()}), true);
			} else {
				source.sendFeedback(Text.translatable("commands.deneco.success.multiple", new Object[]{targets.size()}), true);
			}
		}

		return targets.size();
	}
}
