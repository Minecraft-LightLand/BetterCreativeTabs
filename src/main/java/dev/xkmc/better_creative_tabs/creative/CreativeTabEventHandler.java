package dev.xkmc.better_creative_tabs.creative;

import dev.xkmc.better_creative_tabs.init.BCTClient;
import dev.xkmc.better_creative_tabs.init.BetterCreativeTabs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = BetterCreativeTabs.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CreativeTabEventHandler {

	@SubscribeEvent
	public static void inputEvent(InputEvent.Key event) {
		if (event.getKey() != BCTClient.CREATIVE.getKey().getValue()) return;
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		if (!player.isCreative()) return;
		var screen = Minecraft.getInstance().screen;
		if (!(screen instanceof CreativeModeInventoryScreen)) return;
		CreativeTabGroup.GROUPS = null;
		CreativeTabList.TABS = null;
		Minecraft.getInstance().setScreen(new CreativeIndexScreen(0));
	}

	@SubscribeEvent
	public static void onScreenInit(ScreenEvent.Init.Post event) {
		if (event.getScreen() instanceof CreativeModeInventoryScreen screen) {
			int w = 20;
			int h = 20;
			int x = screen.getGuiLeft() + 30;
			int y = screen.getGuiTop() - 50;
			event.addListener(new AllTabsButton(x, y, w, h,
					Component.translatable("better-creative-tabs.creative_index"),
					e -> Minecraft.getInstance().setScreen(new CreativeIndexScreen(0))));
		}
	}

}
