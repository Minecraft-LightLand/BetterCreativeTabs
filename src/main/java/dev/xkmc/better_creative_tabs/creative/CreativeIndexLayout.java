package dev.xkmc.bettercreativetabs.creative;

import dev.xkmc.bettercreativetabs.init.BetterCreativeTabs;
import dev.xkmc.bettercreativetabs.util.MenuLayoutConfig;
import net.minecraft.resources.ResourceLocation;

public class CreativeIndexLayout {

	public static MenuLayoutConfig get() {
		var ans = new MenuLayoutConfig();
		ans.id = new ResourceLocation(BetterCreativeTabs.MODID, "creative_index");
		ans.height = 201;
		var rect = new MenuLayoutConfig.Rect();
		rect.x = 8;
		rect.y = 33;
		rect.w = 18;
		rect.h = 18;
		rect.rx = 9;
		rect.ry = 9;
		ans.comp.put("grid", rect);
		return ans;
	}

}