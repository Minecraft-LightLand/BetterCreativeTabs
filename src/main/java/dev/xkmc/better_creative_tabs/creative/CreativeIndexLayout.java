package dev.xkmc.better_creative_tabs.creative;

import dev.xkmc.better_creative_tabs.init.BetterCreativeTabs;
import dev.xkmc.better_creative_tabs.util.MenuLayoutConfig;

public class CreativeIndexLayout {

	public static MenuLayoutConfig get() {
		var ans = new MenuLayoutConfig();
		ans.id = BetterCreativeTabs.loc("creative_index");
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