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

	private static <T extends Item> T createItem(String name, T item) {
		ITEMS.put(new Identifier(MODID, name), item);
		return item;
	}

	public static void init() {
		ITEMS.forEach((id, entityType) -> Registry.register(Registry.ITEM, id, entityType));
	}
}
