package dev.xkmc.bettercreativetabs.creative;

import dev.xkmc.bettercreativetabs.init.BCTConfig;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.CreativeModeTabRegistry;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;
import java.util.*;

public class DependencySorter {

	public static List<CreativeTabGroup> init() {
		var config = BCTConfig.CLIENT.priorityModTabs.get();
		Set<String> forced = new LinkedHashSet<>(config);
		Map<String, Entry> mods = new LinkedHashMap<>();
		mods.put("minecraft", new Entry(null));
		for (var c : config)
			ModList.get().getModContainerById(c)
					.ifPresent(e -> mods.put(c, new Entry(e)));
		List<CreativeModeTab> missing = new ArrayList<>();
		var raw = new ArrayList<>(CreativeModeTabRegistry.getSortedCreativeModeTabs());
		var sorted = raw.stream().map(e -> Pair.of(BuiltInRegistries.CREATIVE_MODE_TAB.getKey(e), e))
				.sorted(Comparator.comparing(e -> e.first().getNamespace())).toList();
		for (var tab : sorted) {
			String id = tab.first().getNamespace();
			var mod = mods.get(id);
			if (mod == null) {
				var cont = ModList.get().getModContainerById(id);
				if (cont.isEmpty()) {
					missing.add(tab.second());
					continue;
				}
				mod = new Entry(cont.get());
				mods.put(id, mod);
			}
			mod.tabs.add(tab.second());
		}
		Set<Entry> modSet = new LinkedHashSet<>(mods.values());
		for (var e : modSet) {
			if (e.mod == null) continue;
			for (var dep : e.mod.getModInfo().getDependencies()) {
				if (!dep.isMandatory()) continue;
				var par = mods.get(dep.getModId());
				if (par == null) continue;
				if (par.mod == null) continue;
				if (!forced.contains(par.mod.getModId()) && par.tabs.isEmpty()) continue;
				par.testDeps.add(e);
				e.testParents.add(par);
			}
		}
		for (var e : modSet) {
			Entry par = null;
			int max = 0;
			for (var p : e.testParents) {
				if (p.testDeps.size() > max) {
					max = p.testDeps.size();
					par = p;
				}
			}
			if (par != null) {
				e.parent = par;
				par.dep.add(e);
			}
		}
		modSet.removeIf(e -> e.parent != null);
		List<CreativeTabGroup> groups = new ArrayList<>();
		CreativeTabGroup other = new CreativeTabGroup();
		for (var e : modSet) {
			if (!e.dep.isEmpty() || e.tabs.size() > 1) {
				var group = new CreativeTabGroup();
				e.addAll(group);
				groups.add(group);
			} else {
				other.tabs.addAll(e.tabs);
			}
		}
		other.tabs.addAll(missing);
		groups.add(other);
		return groups;
	}

	private static final class Entry {

		@Nullable
		private final ModContainer mod;

		private final List<Entry> testDeps = new ArrayList<>();
		private final List<Entry> testParents = new ArrayList<>();
		private final List<CreativeModeTab> tabs = new ArrayList<>();
		private Entry parent;
		private final List<Entry> dep = new ArrayList<>();

		private Entry(@Nullable ModContainer mod) {
			this.mod = mod;
		}

		public void addAll(CreativeTabGroup group) {
			group.tabs.addAll(tabs);
			for (var e : dep) e.addAll(group);
		}

	}

}
