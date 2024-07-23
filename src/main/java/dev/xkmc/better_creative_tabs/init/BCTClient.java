package dev.xkmc.better_creative_tabs.init;

import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(value = Dist.CLIENT, modid = BetterCreativeTabs.MODID, bus = EventBusSubscriber.Bus.MOD)
public class BCTClient {

	public static KeyMapping CREATIVE = new KeyMapping("better-creative-tabs.creative_index", GLFW.GLFW_KEY_TAB, "better-creative-tabs.creative_index");

	@SubscribeEvent
	public static void client(FMLClientSetupEvent event) {
	}

	@SubscribeEvent
	public static void keybind(RegisterKeyMappingsEvent event) {
		event.register(CREATIVE);
	}

}
