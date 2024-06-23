package dev.xkmc.better_creative_tabs.init;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = BetterCreativeTabs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
