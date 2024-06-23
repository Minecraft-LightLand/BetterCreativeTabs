package dev.xkmc.better_creative_tabs.creative;

import dev.xkmc.better_creative_tabs.init.BetterCreativeTabs;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class AllTabsButton extends Button {

	public static final ResourceLocation TEX = new ResourceLocation(BetterCreativeTabs.MODID, "textures/gui/icon.png");

	protected AllTabsButton(int x, int y, int w, int h, Component tex, OnPress action) {
		super(x, y, w, h, tex, action, DEFAULT_NARRATION);
		setTooltip(Tooltip.create(tex));
	}

	@Override
	protected void renderWidget(GuiGraphics g, int x, int y, float pt) {
		super.renderWidget(g, x, y, pt);
	}

	@Override
	public void renderString(GuiGraphics g, Font f, int i) {
		g.blit(TEX, getX() + 2, getY() + 2, 0, 0, 16, 16, 16, 16);
	}

}
