package dev.xkmc.bettercreativetabs.creative;

import net.minecraft.world.item.CreativeModeTab;

import java.util.ArrayList;
import java.util.List;

public class CreativeTabList {

	public static List<CreativeModeTab> TABS;

	public static List<CreativeModeTab> getTabs() {
		if (TABS == null) {
			TABS = new ArrayList<>();
			for (var e : CreativeTabGroup.getGroups()) {
				TABS.addAll(e.tabs);
			}
		}
		return TABS;
	}

}
