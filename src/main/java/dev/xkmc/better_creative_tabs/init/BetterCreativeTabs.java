package dev.xkmc.better_creative_tabs.init;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.common.Mod;

@Mod(BetterCreativeTabs.MODID)
public class BetterCreativeTabs {

	public static final String MODID = "better_creative_tabs";

	public BetterCreativeTabs() {
		BCTConfig.init();
	}

	public static ResourceLocation loc(String id) {
		return ResourceLocation.fromNamespaceAndPath(MODID, id);
	}

}
