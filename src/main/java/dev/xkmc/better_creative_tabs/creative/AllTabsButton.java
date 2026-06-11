package dev.xkmc.better_creative_tabs.creative;

import dev.xkmc.better_creative_tabs.init.BetterCreativeTabs;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class AllTabsButton extends Button.Plain {

	public static final Identifier TEX = BetterCreativeTabs.loc("textures/gui/icon.png");

	protected AllTabsButton(int x, int y, int w, int h, Component tex, OnPress action) {
		super(x, y, w, h, tex, action, DEFAULT_NARRATION);
		setTooltip(Tooltip.create(tex));
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor g, int i, int i1, float v) {
		this.extractDefaultSprite(g);
		g.blit(RenderPipelines.GUI_TEXTURED, TEX, getX() + 2, getY() + 2, 0, 0, 16, 16, 16, 16, 16, 16);
	}

}
