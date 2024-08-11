package dev.xkmc.better_creative_tabs.init;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class BCTConfig {

	public static class Client {

		public final ModConfigSpec.ConfigValue<List<? extends String>> priorityModTabs;

		Client(ModConfigSpec.Builder builder) {
			priorityModTabs = builder.comment("List of mod id for which tab will appear first.")
					.comment(" If you add a library mod without tab here, dependencies of the library mods will be grouped together")
					.defineListAllowEmpty("priorityModTabs", new ArrayList<>(List.of("l2library", "farmersdelight")),
							() -> "create", e -> ModList.get().isLoaded((String) e));
		}

	}

	public static final ModConfigSpec CLIENT_SPEC;
	public static final Client CLIENT;

	static {
		final Pair<Client, ModConfigSpec> client = new ModConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = client.getRight();
		CLIENT = client.getLeft();
	}

	/**
	 * Registers any relevant listeners for config
	 */
	public static void init() {
		register(ModConfig.Type.CLIENT, CLIENT_SPEC);
	}

	private static void register(ModConfig.Type type, IConfigSpec spec) {
		var mod = ModLoadingContext.get().getActiveContainer();
		String path = "l2configs/" + mod.getModId() + "-" + type.extension() + ".toml";
		mod.registerConfig(type, spec, path);
		if (FMLEnvironment.dist == Dist.CLIENT) {
			mod.<IConfigScreenFactory>registerExtensionPoint(IConfigScreenFactory.class, () -> ConfigurationScreen::new);
		}
	}


}
