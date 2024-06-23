package dev.xkmc.bettercreativetabs.init;

import net.minecraftforge.fml.common.Mod;

@Mod(BetterCreativeTabs.MODID)
public class BetterCreativeTabs {

	public static final String MODID = "better-creative-tabs";

	public BetterCreativeTabs() {
		BCTConfig.init();
	}

}
