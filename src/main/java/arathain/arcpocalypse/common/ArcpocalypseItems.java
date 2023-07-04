package arathain.arcpocalypse.common;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.LinkedHashMap;
import java.util.Map;

import static arathain.arcpocalypse.Arcpocalypse.MODID;

public class ArcpocalypseItems {
	private static final Map<Identifier, Item> ITEMS = new LinkedHashMap<>();
	// entities
	public static final Item ABYSS_LIFT = createItem("abyss_lift", new AbyssLiftItem(new QuiltItemSettings().maxCount(1)));

	// Neco transform items
	public static final Item FUNNY_MARBLE = createItem("true_ancestor_marble", new NecoItem(new QuiltItemSettings().maxCount(1), NekoArcComponent.TypeNeco.ARC)); // RENAME TO SOMETHING MORE CLEVER
	public static final Item NOT_A_CROSS = createItem("holy_math_symbol", new NecoItem(new QuiltItemSettings().maxCount(1), NekoArcComponent.TypeNeco.CIEL));
	public static final Item CRIMSON_GEM = createItem("crimson_gem", new NecoItem(new QuiltItemSettings().maxCount(1), NekoArcComponent.TypeNeco.AKIHA));
	public static final Item HYPNOTIC_STOPWATCH = createItem("hypnotic_stopwatch", new NecoItem(new QuiltItemSettings().maxCount(1), NekoArcComponent.TypeNeco.HISUI)); // Iron
	public static final Item SUSPICIOUS_BROOM = createItem("suspicious_broom", new NecoItem(new QuiltItemSettings().maxCount(1), NekoArcComponent.TypeNeco.KOHAKU)); // HAY and Stick


	private static <T extends Item> T createItem(String name, T item) {
		ITEMS.put(new Identifier(MODID, name), item);
		return item;
	}

	public static void init() {
		ITEMS.forEach((id, entityType) -> Registry.register(Registries.ITEM, id, entityType));
	}
}
