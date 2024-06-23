package dev.xkmc.better_creative_tabs.init;

import net.minecraftforge.fml.common.Mod;

@Mod(BetterCreativeTabs.MODID)
public class BetterCreativeTabs {

	public static final String MODID = "better_creative_tabs";

	public BetterCreativeTabs() {
		BCTConfig.init();
	}

}
