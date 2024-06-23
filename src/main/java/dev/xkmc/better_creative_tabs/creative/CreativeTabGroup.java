package dev.xkmc.better_creative_tabs.creative;

import net.minecraft.world.item.CreativeModeTab;

import java.util.ArrayList;
import java.util.List;

public class CreativeTabGroup {

	public static List<CreativeTabGroup> GROUPS;

	public static List<CreativeTabGroup> getGroups() {
		if (GROUPS == null) {
			GROUPS = DependencySorter.init();
		}
		return GROUPS;
	}

	public ArrayList<CreativeModeTab> tabs = new ArrayList<>();


}
