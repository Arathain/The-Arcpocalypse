package arathain.arcpocalypse.common;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.LinkedHashMap;
import java.util.Map;

import static arathain.arcpocalypse.Arcpocalypse.MODID;

public class ArcpocalypseItems {
	private static final Map<Identifier, Item> ITEMS = new LinkedHashMap<>();
	// entities
	public static final Item ABYSS_LIFT = createItem("abyss_lift", new AbyssLiftItem(new QuiltItemSettings().maxCount(1).group(ItemGroup.MISC)));

	// Neco transform items
	public static final Item FUNNY_MARBLE = createItem("funny_marble", new NecoItem(new QuiltItemSettings().maxCount(1).group(ItemGroup.MISC), NekoArcComponent.TypeNeco.ARC)); // RENAME TO SOMETHING MORE CLEVER
	public static final Item NOT_A_CROSS = createItem("holy_math_symbol", new NecoItem(new QuiltItemSettings().maxCount(1).group(ItemGroup.MISC), NekoArcComponent.TypeNeco.CIEL));
	public static final Item CRIMSON_GEM = createItem("crimson_gem", new NecoItem(new QuiltItemSettings().maxCount(1).group(ItemGroup.MISC), NekoArcComponent.TypeNeco.AKIHA));
	public static final Item HYPNOTIC_STOPWATCH = createItem("hypnotic_stopwatch", new NecoItem(new QuiltItemSettings().maxCount(1).group(ItemGroup.MISC), NekoArcComponent.TypeNeco.HISUI)); // Iron
	public static final Item SUSPICIOUS_BROOM = createItem("suspicious_broom", new NecoItem(new QuiltItemSettings().maxCount(1).group(ItemGroup.MISC), NekoArcComponent.TypeNeco.KOHAKU)); // HAY and Stick


	private static <T extends Item> T createItem(String name, T item) {
		ITEMS.put(new Identifier(MODID, name), item);
		return item;
	}

	public static void init() {
		ITEMS.forEach((id, entityType) -> Registry.register(Registry.ITEM, id, entityType));
	}
}
